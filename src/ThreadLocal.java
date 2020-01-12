import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ThreadLocal<T> {
    /**
     *
     */
    private final int threadLocalHashCode = nextHashCode();

    /**
     * 原子类，防止多线程更新hashcode导致覆盖.
     */
    private static AtomicInteger nextHashCode =
            new AtomicInteger();

    /**
     * TODO
     * 散列增加值，这个我也不是太理解，以后再回过来弄吧
     */
    private static final int HASH_INCREMENT = 0x61c88647;

    /**
     * 返回下一个哈希码
     */
    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    /**
     * 初始化value为null，被get调用
     */
    protected T initialValue() {
        return null;
    }

    /**
     *
     */
    public static <S> ThreadLocal<S> withInitial(Supplier<? extends S> supplier) {
        return new SuppliedThreadLocal<>(supplier);
    }

    /**
     * 构造，创建线程本地变量
     */
    public ThreadLocal() {
    }

    /**
     * 获取ThreadLocalMap中的值，如果没有初始化，则返回setInitialValue的值
     */
    public T get() {
        // 获取当前线程
        Thread t = Thread.currentThread();
        // 获取当前线程属性ThreadLocalMap
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            // 获取entry
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T) e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    /**
     * 设置初始值
     * 若线程局部变量threadlocals为null，创建并赋初值
     * 不为null，调用ThreadLocalMap的set()方法赋值
     */
    private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }

    /**
     * 赋值
     */
    public void set(T value) {
        Thread t = Thread.currentThread();
        // 获取当前线程的局部变量ThreadLocalMap
        ThreadLocalMap map = getMap(t);
        if (map != null)
            // 赋值，key为ThreadLocal对象
            map.set(this, value);
        else
            // 创建并赋值
            createMap(t, value);
    }

    /**
     * Removes the current thread's value for this thread-local
     * variable.  If this thread-local variable is subsequently
     * {@linkplain #get read} by the current thread, its value will be
     * reinitialized by invoking its {@link #initialValue} method,
     * unless its value is {@linkplain #set set} by the current thread
     * in the interim.  This may result in multiple invocations of the
     * {@code initialValue} method in the current thread.
     *
     * @since 1.5
     */
    public void remove() {
        ThreadLocalMap m = getMap(Thread.currentThread());
        if (m != null)
            m.remove(this);
    }

    /**
     * 获取线程的局部变量threadLocals
     */
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }

    /**
     * 创建ThreadLocalMap并赋值
     */
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }

    /**
     * Factory method to create map of inherited thread locals.
     * Designed to be called only from Thread constructor.
     *
     * @param parentMap the map associated with parent thread
     * @return a map containing the parent's inheritable bindings
     */
    static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
        return new ThreadLocalMap(parentMap);
    }

    /**
     * Method childValue is visibly defined in subclass
     * InheritableThreadLocal, but is internally defined here for the
     * sake of providing createInheritedMap factory method without
     * needing to subclass the map class in InheritableThreadLocal.
     * This technique is preferable to the alternative of embedding
     * instanceof tests in methods.
     */
    T childValue(T parentValue) {
        throw new UnsupportedOperationException();
    }

    static class ThreadLocalMap {

        /**
         * 继承WeakReference，所以key在垃圾回收时可能会为null
         * 如果为null，表示这个entry就不再使用
         */
        static class Entry extends WeakReference<ThreadLocal<?>> {

            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        /**
         * 初始容量
         */
        private static final int INITIAL_CAPACITY = 16;

        /**
         * entry表的引用
         */
        private Entry[] table;

        /**
         * 表的长度
         */
        private int size = 0;

        /**
         * 扩容阈值
         */
        private int threshold; // Default to 0

        /**
         * 调整扩容阈值为table长度的2/3，可以和hashmap类比一下，hashmap的为3/4
         * 至于为什么会取长度为2/3，可以想一想
         */
        private void setThreshold(int len) {
            threshold = len * 2 / 3;
        }

        /**
         * 返回下一个索引下标
         */
        private static int nextIndex(int i, int len) {
            return ((i + 1 < len) ? i + 1 : 0);
        }

        /**
         * 返回上一个索引下标
         */
        private static int prevIndex(int i, int len) {
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        /**
         * 构造ThreadLocalMap
         */
        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            table = new Entry[INITIAL_CAPACITY];
            // 此处INITIAL_CAPACITY为2的n次幂，所以可以用位运算与操作
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }

        /**
         * Construct a new map including all Inheritable ThreadLocals
         * from given parent map. Called only by createInheritedMap.
         *
         * @param parentMap the map associated with parent thread.
         */
        private ThreadLocalMap(ThreadLocalMap parentMap) {
            Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new Entry[len];

            for (int j = 0; j < len; j++) {
                Entry e = parentTable[j];
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
                    if (key != null) {
                        Object value = key.childValue(e.value);
                        Entry c = new Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null)
                            h = nextIndex(h, len);

                        table[h] = c;
                        size++;
                    }
                }
            }
        }

        /**
         * 获取Entry
         *
         * @param key 线程本地对象
         * @return 与key关联的entry，如果没有，返回null
         */
        private Entry getEntry(ThreadLocal<?> key) {
            // 确定位置
            int i = key.threadLocalHashCode & (table.length - 1);
            // 获取entry
            Entry e = table[i];
            if (e != null && e.get() == key)
                return e;
            else
                return getEntryAfterMiss(key, i, e);
        }

        /**
         * 在数组中找不到对应key的entry时，使用此版本
         *
         * @param key thread local
         * @param i   数组下标
         * @param e   the entry at table[i]
         * @return key对应的entry存在，返回entry，否则返回null
         */
        private Entry getEntryAfterMiss(ThreadLocal<?> key, int i, Entry e) {
            Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                ThreadLocal<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    // 清理i位置的entry
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }

        /**
         * 设置key，value
         */
        private void set(ThreadLocal<?> key, Object value) {

            // We don't use a fast path as with get() because it is at
            // least as common to use set() to create new entries as
            // it is to replace existing ones, in which case, a fast
            // path would fail more often than not.

            Entry[] tab = table;
            int len = tab.length;
            // 位运算确定下标
            int i = key.threadLocalHashCode & (len - 1);

            /**
             * 从i开始向后遍历，直至e为null
             * 此处可能有问题，如果内存足够，一直没有进行垃圾回收，这就会是个死循环
             */
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                ThreadLocal<?> k = e.get();

                // key相等确定位置
                if (k == key) {
                    e.value = value;
                    return;
                }

                // key为空，进行替换
                if (k == null) {
                    // 替换原来的entry
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }

            tab[i] = new Entry(key, value);
            int sz = ++size;
            // 超出阈值，不在rehash
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                rehash();
        }

        /**
         * 移除key，value
         */
        private void remove(ThreadLocal<?> key) {
            Entry[] tab = table;
            int len = tab.length;
            // 位运算获取数组下标
            int i = key.threadLocalHashCode & (len - 1);
            for (Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                if (e.get() == key) {
                    // 清空key的引用
                    e.clear();
                    // 清空entry的引用
                    expungeStaleEntry(i);
                    return;
                }
            }
        }

        /**
         * 替换此位置的kv
         * <p>
         * As a side effect, this method expunges all stale entries in the
         * "run" containing the stale entry.  (A run is a sequence of entries
         * between two null slots.)
         *
         * @param key       the key
         * @param value     the value to be associated with key
         * @param staleSlot index of the first stale entry encountered while
         *                  searching for key.
         */
        private void replaceStaleEntry(ThreadLocal<?> key, Object value,
                                       int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;
            Entry e;

            // Back up to check for prior stale entry in current run.
            // We clean out whole runs at a time to avoid continual
            // incremental rehashing due to garbage collector freeing
            // up refs in bunches (i.e., whenever the collector runs).
            int slotToExpunge = staleSlot;
            // 从尾到头获取第一个为null
            for (int i = prevIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = prevIndex(i, len))
                if (e.get() == null)
                    slotToExpunge = i;

            // Find either the key or trailing null slot of run, whichever
            // occurs first
            for (int i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();

                // If we find key, then we need to swap it
                // with the stale entry to maintain hash table order.
                // The newly stale slot, or any other stale slot
                // encountered above it, can then be sent to expungeStaleEntry
                // to remove or rehash all of the other entries in run.
                if (k == key) {
                    e.value = value;

                    tab[i] = tab[staleSlot];
                    tab[staleSlot] = e;

                    // Start expunge at preceding stale entry if it exists
                    if (slotToExpunge == staleSlot)
                        slotToExpunge = i;
                    cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                    return;
                }

                // If we didn't find stale entry on backward scan, the
                // first stale entry seen while scanning for key is the
                // first still present in the run.
                if (k == null && slotToExpunge == staleSlot)
                    slotToExpunge = i;
            }

            // If key not found, put new entry in stale slot
            tab[staleSlot].value = null;
            tab[staleSlot] = new Entry(key, value);

            // If there are any other stale entries in run, expunge them
            if (slotToExpunge != staleSlot)
                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
        }

        /**
         * 清理过时的entry
         *
         * @param staleSlot index of slot known to have null key
         * @return the index of the next null slot after staleSlot
         * (all between staleSlot and this slot will have been checked
         * for expunging).
         */
        private int expungeStaleEntry(int staleSlot) {
            Entry[] tab = table;
            int len = tab.length;

            // expunge entry at staleSlot
            // 清理entry和value(key已经在垃圾回收时回收了)
            tab[staleSlot].value = null;
            tab[staleSlot] = null;
            size--;

            // Rehash until we encounter null
            Entry e;
            int i;
            // 遍历staleSlot的所有后续节点
            for (i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal<?> k = e.get();
                // key为null，干掉
                if (k == null) {
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else { // key不为null重新计算下标
                    int h = k.threadLocalHashCode & (len - 1);
                    if (h != i) {
                        // 不在同一个位置，将i位置的entry删除
                        tab[i] = null;

                        // Unlike Knuth 6.4 Algorithm R, we must scan until
                        // null because multiple entries could have been stale.
                        while (tab[h] != null)
                            // 找出h之后，entry为null的下标
                            h = nextIndex(h, len);
                        tab[h] = e;
                    }
                }
            }
            return i;
        }

        /**
         * Heuristically scan some cells looking for stale entries.
         * This is invoked when either a new element is added, or
         * another stale one has been expunged. It performs a
         * logarithmic number of scans, as a balance between no
         * scanning (fast but retains garbage) and a number of scans
         * proportional to number of elements, that would find all
         * garbage but would cause some insertions to take O(n) time.
         *
         * @param i a position known NOT to hold a stale entry. The
         *          scan starts at the element after i.
         * @param n scan control: {@code log2(n)} cells are scanned,
         *          unless a stale entry is found, in which case
         *          {@code log2(table.length)-1} additional cells are scanned.
         *          When called from insertions, this parameter is the number
         *          of elements, but when from replaceStaleEntry, it is the
         *          table length. (Note: all this could be changed to be either
         *          more or less aggressive by weighting n instead of just
         *          using straight log n. But this version is simple, fast, and
         *          seems to work well.)
         * @return true if any stale entries have been removed.
         */
        private boolean cleanSomeSlots(int i, int n) {
            boolean removed = false;
            Entry[] tab = table;
            int len = tab.length;
            do {
                i = nextIndex(i, len);
                Entry e = tab[i];
                if (e != null && e.get() == null) {
                    n = len;
                    removed = true;
                    i = expungeStaleEntry(i);
                }
            } while ((n >>>= 1) != 0);
            return removed;
        }

        /**
         * rehash，先清理过时的entry，然后在判断是否需要扩容
         */
        private void rehash() {
            expungeStaleEntries();

            // Use lower threshold for doubling to avoid hysteresis
            if (size >= threshold - threshold / 4)
                resize();
        }

        /**
         * 扩容，和hashmap一样，容量增加一倍
         */
        private void resize() {
            Entry[] oldTab = table;
            int oldLen = oldTab.length;
            int newLen = oldLen * 2;
            Entry[] newTab = new Entry[newLen];
            int count = 0;

            for (int j = 0; j < oldLen; ++j) {
                Entry e = oldTab[j];
                if (e != null) {
                    ThreadLocal<?> k = e.get();
                    if (k == null) {
                        e.value = null; // Help the GC
                    } else {
                        // 确定k的插入位置h
                        int h = k.threadLocalHashCode & (newLen - 1);
                        // 若h处存在entry，则冲突，寻找下一个为null的位置插入
                        while (newTab[h] != null)
                            h = nextIndex(h, newLen);
                        newTab[h] = e;
                        // size
                        count++;
                    }
                }
            }

            setThreshold(newLen);
            size = count;
            table = newTab;
        }

        /**
         * 清除表中所有过时的entry
         */
        private void expungeStaleEntries() {
            Entry[] tab = table;
            int len = tab.length;
            for (int j = 0; j < len; j++) {
                Entry e = tab[j];
                // 找到entry不为null，但entry的key被清理
                if (e != null && e.get() == null)
                    // 清除表中过时的entry
                    expungeStaleEntry(j);
            }
        }
    }

    /**
     * An extension of ThreadLocal that obtains its initial value from
     * the specified {@code Supplier}.
     */
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }
}
