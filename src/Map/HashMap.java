package Map;

/**
 * 因为TreeNode移植不过来,所以注掉了报错代码
 * 可对比源码包查看
 */
//public class HashMap<K, V> extends AbstractMap<K, V>
//        implements Map<K, V>, Cloneable, Serializable {
//
//    /**
//     * 序列化ID
//     */
//    private static final long serialVersionUID = 362498820763181265L;
//
//    /**
//     * 默认初始容量-->16
//     */
//    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
//
//    /**
//     * 最大容量-->2^30
//     */
//    static final int MAXIMUM_CAPACITY = 1 << 30;
//
//    /**
//     * 默认负载因子
//     */
//    static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//    /**
//     * 链表转红黑树的阈值
//     */
//    static final int TREEIFY_THRESHOLD = 8;
//
//    /**
//     * 红黑树转链表的阈值
//     */
//    static final int UNTREEIFY_THRESHOLD = 6;
//
//    /**
//     *
//     */
//    static final int MIN_TREEIFY_CAPACITY = 64;
//
//    /**
//     * 存储元素的数组,2的n次幂
//     */
//    transient Node<K, V>[] table;
//
//    /**
//     * 键值对的个数
//     */
//    transient int size;
//
//    /**
//     * Holds cached entrySet(). Note that AbstractMap fields are used
//     * for keySet() and values().
//     */
//    transient Set<Map.Entry<K, V>> entrySet;
//
//    /**
//     * 对HashMap修改的次数
//     */
//    transient int modCount;
//
//    /**
//     * 临界值(loadFactor * capacity)容量*负载因子
//     * 当size>此值时进行扩容
//     */
//    int threshold;
//
//    /**
//     * 负载因子
//     */
//    final float loadFactor;
//
//    /**
//     * Node实现了Entry接口,存放键值对(Map真正存放数据的内部类)
//     */
//    static class Node<K, V> implements Map.Entry<K, V> {
//        final int hash;
//        final K key;
//        V value;
//        Node<K, V> next;
//
//        Node(int hash, K key, V value, Node<K, V> next) {
//            this.hash = hash;
//            this.key = key;
//            this.value = value;
//            this.next = next;
//        }
//
//        public final K getKey() {
//            return key;
//        }
//
//        public final V getValue() {
//            return value;
//        }
//
//        public final String toString() {
//            return key + "=" + value;
//        }
//
//        // Node节点的hash为hash(key)异或hash(value)
//        public final int hashCode() {
//            return Objects.hashCode(key) ^ Objects.hashCode(value);
//        }
//
//        public final V setValue(V newValue) {
//            V oldValue = value;
//            value = newValue;
//            return oldValue;
//        }
//
//        // 重写了equals方法
//        public final boolean equals(Object o) {
//            if (o == this)
//                return true;
//            if (o instanceof Map.Entry) {
//                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
//                if (Objects.equals(key, e.getKey()) &&
//                        Objects.equals(value, e.getValue()))
//                    return true;
//            }
//            return false;
//        }
//    }
//
//    /**
//     * 重写了hash
//     */
//    static final int hash(Object key) {
//        int h;
//        // key == null 时,hash为0
//        // 否则为key的hashcode异或key的hashcode无符号右移16位
//        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
//    }
//
//    /**
//     * Returns a power of two size for the given target capacity.
//     */
//    static final int tableSizeFor(int cap) {
//        int n = cap - 1;
//        n |= n >>> 1;
//        n |= n >>> 2;
//        n |= n >>> 4;
//        n |= n >>> 8;
//        n |= n >>> 16;
//        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
//    }
//
//    /**
//     * 传入initialCapacity(初始容量)和loadFactor(负载因子)
//     */
//    public HashMap(int initialCapacity, float loadFactor) {
//        if (initialCapacity < 0)
//            throw new IllegalArgumentException("Illegal initial capacity: " +
//                    initialCapacity);
//        if (initialCapacity > MAXIMUM_CAPACITY)
//            initialCapacity = MAXIMUM_CAPACITY;
//        if (loadFactor <= 0 || Float.isNaN(loadFactor))
//            throw new IllegalArgumentException("Illegal load factor: " +
//                    loadFactor);
//        this.loadFactor = loadFactor;
//        this.threshold = tableSizeFor(initialCapacity);
//    }
//
//    /**
//     * 初始容量为initialCapacity
//     */
//    public HashMap(int initialCapacity) {
//        this(initialCapacity, DEFAULT_LOAD_FACTOR);
//    }
//
//    /**
//     * 初始的负载因子为16,其余的都是默认的
//     */
//    public HashMap() {
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//    }
//
//    /**
//     * 传入m,将m中的所有元素放入到hashMap中
//     */
//    public HashMap(Map<? extends K, ? extends V> m) {
//        this.loadFactor = DEFAULT_LOAD_FACTOR;
//        putMapEntries(m, false);
//    }
//
//    /**
//     * m中的元素放入map中
//     */
//    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
//        int s = m.size();
//        if (s > 0) {
//            // m中存在元素
//            if (table == null) {
//                // 判断hashmap需要的最小容量
//                float ft = ((float) s / loadFactor) + 1.0F;
//                int t = ((ft < (float) MAXIMUM_CAPACITY) ? (int) ft : MAXIMUM_CAPACITY);
//                if (t > threshold)
//                    threshold = tableSizeFor(t);
//            } else if (s > threshold)
//                resize();
//
//            // 遍历放入map中
//            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
//                K key = e.getKey();
//                V value = e.getValue();
//                putVal(hash(key), key, value, false, evict);
//            }
//        }
//    }
//
//    // 键值对的个数
//    public int size() {
//        return size;
//    }
//
//    public boolean isEmpty() {
//        return size == 0;
//    }
//
//    /**
//     * 只对getNode()做了注释,get方法基本都是依赖getNode来完成
//     */
//    public V get(Object key) {
//        Node<K, V> e;
//        return (e = getNode(hash(key), key)) == null ? null : e.value;
//    }
//
//    /**
//     * getNode方法,所有的取值都依赖此方法
//     */
//    final Node<K, V> getNode(int hash, Object key) {
//        Node<K, V>[] tab;
//        Node<K, V> first, e;
//        int n;
//        K k;
//        // 和putVal大体相同
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//                (first = tab[(n - 1) & hash]) != null) {
//            // 判断第一个节点
//            if (first.hash == hash && // always check first node
//                    ((k = first.key) == key || (key != null && key.equals(k))))
//                return first;
//            // 判断下一个节点是否为null
//            if ((e = first.next) != null) {
//                // 判断是否为红黑树节点
//                if (first instanceof TreeNode)
//                    return ((TreeNode<K, V>) first).getTreeNode(hash, key);
//                // 链表节点
//                do {
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        return e;
//                } while ((e = e.next) != null);
//            }
//        }
//        return null;
//    }
//
//    public boolean containsKey(Object key) {
//        return getNode(hash(key), key) != null;
//    }
//
//    /**
//     * 插入键值对
//     */
//    public V put(K key, V value) {
//        return putVal(hash(key), key, value, false, true);
//    }
//
//    /**
//     * 插入键值对
//     */
//    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
//                   boolean evict) {
//        Node<K, V>[] tab;
//        Node<K, V> p;
//        int n, i;
//
//        // 判断table为null或table的长度为0
//        if ((tab = table) == null || (n = tab.length) == 0)
//            // 扩容并赋值给n
//            n = (tab = resize()).length;
//        // (n-1)&hash -> 根据hash计算出数组下标
//        // tab[(n-1)&hash]为此位置的值
//        if ((p = tab[i = (n - 1) & hash]) == null)
//            // 为null,赋值新节点
//            tab[i] = newNode(hash, key, value, null);
//        else {
//            // 此位置不为null,即存在值
//            Node<K, V> e;
//            K k;
//            // hash相等且(key相同(1.两个key完全相等,2.equals相等))
//            if (p.hash == hash &&
//                    ((k = p.key) == key || (key != null && key.equals(k))))
//                // 覆盖
//                e = p;
//
//            else if (p instanceof TreeNode)
//                // p属于树节点,e用来记录头节点
//                e = ((TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
//            else {
//                // p为链表首节点
//                for (int binCount = 0; ; ++binCount) {
//                    // 判断p的next节点是否为空
//                    if ((e = p.next) == null) {
//                        // 构建node并与p建立关联
//                        p.next = newNode(hash, key, value, null);
//                        // 判断binCount是否>=7(因为binCount从0开始)
//                        if (binCount >= TREEIFY_THRESHOLD - 1)
//                            // 构建树
//                            treeifyBin(tab, hash);
//                        break;
//                    }
//                    // 判断hash相等且(key相同(1.两个key完全相等,2.equals相等))
//                    if (e.hash == hash &&
//                            ((k = e.key) == key || (key != null && key.equals(k))))
//                        break;
//
//                    p = e;
//                }
//            }
//
//            // 判断e是否为null
//            if (e != null) {
//                V oldValue = e.value;
//                if (!onlyIfAbsent || oldValue == null)
//                    e.value = value;
//                afterNodeAccess(e);
//                return oldValue;
//            }
//        }
//
//        // 更新结构修改次数
//        ++modCount;
//        // 是否需要扩容
//        if (++size > threshold)
//            resize();
//        afterNodeInsertion(evict);
//        return null;
//    }
//
//    /**
//     * 扩容方法
//     * 触发条件:
//     * 1. 默认构造首次插入键值对
//     * 2. 链表转树时,table长度<64
//     * 3. size>threshold时
//     */
//    final Node<K, V>[] resize() {
//        Node<K, V>[] oldTab = table;
//        // 旧容量
//        int oldCap = (oldTab == null) ? 0 : oldTab.length;
//        // 旧阈值
//        int oldThr = threshold;
//        int newCap, newThr = 0;
//        // 旧容量大于0
//        if (oldCap > 0) {
//            if (oldCap >= MAXIMUM_CAPACITY) {
//                // 超过了最大值,不再扩容
//                threshold = Integer.MAX_VALUE;
//                return oldTab;
//                // (新容量(旧容量的2倍)<最大容量) && (旧容量>=默认初始容量)
//            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
//                    oldCap >= DEFAULT_INITIAL_CAPACITY)
//                // 新阈值为旧阈值2倍
//                newThr = oldThr << 1; // double threshold
//        } else if (oldThr > 0) // initial capacity was placed in threshold
//            // 进入这说明是初始化的扩容,阈值为旧阈值即可
//            newCap = oldThr;
//        else {               // zero initial threshold signifies using defaults
//            newCap = DEFAULT_INITIAL_CAPACITY;
//            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
//        }
//
//        if (newThr == 0) {
//            float ft = (float) newCap * loadFactor;
//            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
//                    (int) ft : Integer.MAX_VALUE);
//        }
//        threshold = newThr;
//        @SuppressWarnings({"rawtypes", "unchecked"})
//        Node<K, V>[] newTab = (Node<K, V>[]) new Node[newCap];
//        table = newTab;
//        if (oldTab != null) {
//            for (int j = 0; j < oldCap; ++j) {
//                Node<K, V> e;
//                /**
//                 * 其实和添加元素,获取元素一个逻辑
//                 * 有以下几种可能:
//                 * 1. j位置为null->跳过
//                 * 2. j位置不为null
//                 *    2.1 j位置就一个元素
//                 *    2.2 j位置关联着链表
//                 *    2.3 j位置关联着红黑树
//                 */
//                if ((e = oldTab[j]) != null) {
//                    // 旧表j位置,置null
//                    oldTab[j] = null;
//                    if (e.next == null)
//                        // 添加逻辑,和put查找位置相同
//                        newTab[e.hash & (newCap - 1)] = e;
//                    else if (e instanceof TreeNode)
//                        // 关联着红黑树
//                        ((TreeNode<K, V>) e).split(this, newTab, j, oldCap);
//                    else { // preserve order
//                        /**
//                         * 待添加
//                         */
//                        Node<K, V> loHead = null, loTail = null;
//                        Node<K, V> hiHead = null, hiTail = null;
//                        Node<K, V> next;
//                        do {
//                            next = e.next;
//                            if ((e.hash & oldCap) == 0) {
//                                if (loTail == null)
//                                    loHead = e;
//                                else
//                                    loTail.next = e;
//                                loTail = e;
//                            } else {
//                                if (hiTail == null)
//                                    hiHead = e;
//                                else
//                                    hiTail.next = e;
//                                hiTail = e;
//                            }
//                        } while ((e = next) != null);
//                        if (loTail != null) {
//                            loTail.next = null;
//                            newTab[j] = loHead;
//                        }
//                        if (hiTail != null) {
//                            hiTail.next = null;
//                            newTab[j + oldCap] = hiHead;
//                        }
//                    }
//                }
//            }
//        }
//        return newTab;
//    }
//
//    /**
//     * Replaces all linked nodes in bin at index for given hash unless
//     * table is too small, in which case resizes instead.
//     */
//    final void treeifyBin(Node<K, V>[] tab, int hash) {
//        int n, index;
//        Node<K, V> e;
//        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
//            resize();
//        else if ((e = tab[index = (n - 1) & hash]) != null) {
//            // 该位置的node不为null
//            TreeNode<K, V> hd = null, tl = null;
//            do {
//                TreeNode<K, V> p = replacementTreeNode(e, null);
//                if (tl == null)
//                    hd = p;
//                else {
//                    p.prev = tl;
//                    tl.next = p;
//                }
//                tl = p;
//            } while ((e = e.next) != null);
//            if ((tab[index] = hd) != null)
//                hd.treeify(tab);
//        }
//    }
//
//    /**
//     * 插入m到map中(调用的putVal方法)
//     */
//    public void putAll(Map<? extends K, ? extends V> m) {
//        putMapEntries(m, true);
//    }
//
//    /**
//     * 移除键值对
//     */
//    public V remove(Object key) {
//        Node<K, V> e;
//        return (e = removeNode(hash(key), key, null, false, true)) == null ?
//                null : e.value;
//    }
//
//    /**
//     * Implements Map.remove and related methods
//     *
//     * @param hash       hash for key
//     * @param key        the key
//     * @param value      the value to match if matchValue, else ignored
//     * @param matchValue if true only remove if value is equal
//     * @param movable    if false do not move other nodes while removing
//     * @return the node, or null if none
//     */
//    final Node<K, V> removeNode(int hash, Object key, Object value,
//                                                  boolean matchValue, boolean movable) {
//        Node<K, V>[] tab;
//        Node<K, V> p;
//        int n, index;
//        if ((tab = table) != null && (n = tab.length) > 0 &&
//                (p = tab[index = (n - 1) & hash]) != null) {
//            Node<K, V> node = null, e;
//            K k;
//            V v;
//            if (p.hash == hash &&
//                    ((k = p.key) == key || (key != null && key.equals(k))))
//                node = p;
//            else if ((e = p.next) != null) {
//                if (p instanceof TreeNode)
//                    node = ((TreeNode<K, V>) p).getTreeNode(hash, key);
//                else {
//                    do {
//                        if (e.hash == hash &&
//                                ((k = e.key) == key ||
//                                        (key != null && key.equals(k)))) {
//                            node = e;
//                            break;
//                        }
//                        p = e;
//                    } while ((e = e.next) != null);
//                }
//            }
//            if (node != null && (!matchValue || (v = node.value) == value ||
//                    (value != null && value.equals(v)))) {
//                if (node instanceof TreeNode)
//                    ((TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
//                else if (node == p)
//                    tab[index] = node.next;
//                else
//                    p.next = node.next;
//                ++modCount;
//                --size;
//                afterNodeRemoval(node);
//                return node;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 清空键值对(其实就是把table上的所有位置都置null)
//     */
//    public void clear() {
//        Node<K, V>[] tab;
//        modCount++;
//        if ((tab = table) != null && size > 0) {
//            size = 0;
//            for (int i = 0; i < tab.length; ++i)
//                tab[i] = null;
//        }
//    }
//
//    void afterNodeAccess(Node<K,V> p) { }
//    void afterNodeInsertion(boolean evict) { }
//    void afterNodeRemoval(Node<K,V> p) { }
//
//    @Override
//    public boolean containsValue(Object value) {
//        return super.containsValue(value);
//    }
//
//    @Override
//    public Set<K> keySet() {
//        return super.keySet();
//    }
//
//    @Override
//    public Collection<V> values() {
//        return super.values();
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        return super.equals(o);
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }
//
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }
//
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return null;
//    }
//}

