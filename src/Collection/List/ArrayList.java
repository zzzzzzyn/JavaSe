package Collection.List;

import java.util.*;

/**
 * @author xyn
 */
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    /** 序列化ID */
    private static final long serialVersionUID = 8683452581122892189L;

    /** 默认容量 */
    private static final int DEFAULT_CAPACITY = 10;

    /** 空数据元素 */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /** 默认空数据元素 */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /** 存储数据的数组引用 */
    transient Object[] elementData;

    /** 集合的长度 */
    private int size;

    /** 数组最大长度 */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /** 构造方法 */
    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * 初始化一个长度为initialCapacity的数据元素
     * 推荐在创建ArrayList时给出容量以尽量避免扩容来提高效率
     * @param initialCapacity  初始容量
     */
    public ArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            // 数据元素引用赋值
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            // 数据元素引用空数据元素
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    /**
     * 构造时传入集合c
     * @param c
     */
    public ArrayList(Collection<? extends E> c) {
        elementData = c.toArray();
        if ((size = elementData.length) != 0) {
            // 集合c的size不为0
            if (elementData.getClass() != Object[].class)
                elementData = Arrays.copyOf(elementData, size, Object[].class);
        } else {
            // 集合c的size为0则赋值空数据元素
            this.elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * 集合的大小(也就是当前数组的长度)
     * @return int
     */
    public int size() {
        return size;
    }

    /**
     * 是否为空(长度是否为0)
     * @return boolean
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 是否包含对象o
     * @param o object
     * @return boolean
     */
    public boolean contains(Object o) {
        // 内部调用index获取o的索引并与0做比较
        return indexOf(o) >= 0;
    }

    /**
     * 对象o首次出现的位置
     * @param o object
     * @return int
     */
    public int indexOf(Object o) {
        /**
         * ︿(￣︶￣)︿
         * 判空(列出了对象o所有的可能性-》null或非null)
         * 因为首次出现位置，所以正向遍历
         */
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * 对象o最后一次出现的位置(同首次相对应)
     * @param o object
     * @return int
     */
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = size-1; i >= 0; i--)
                if (o.equals(elementData[i]))
                    return i;
        }
        return -1;
    }

    /**
     * 克隆操作
     * @return Object
     */
    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    /**
     * 集合转换为数组
     * @return Object[]
     */
    public Object[] toArray() {
        // 调用Arrays.copyOf方法
        return Arrays.copyOf(elementData, size);
    }

    //
    /**
     * 传入泛型数组接受集合元素
     * @param a T[]
     * @return T
     */
    public <T> T[] toArray(T[] a) {
        // 长度判断
        if (a.length < size)
            // 返回一个长度为size,数组元素为elementData的新数组
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        // 元素拷贝
        System.arraycopy(elementData, 0, a, 0, size);
        // a数组长度>size,则a[size]置null
        if (a.length > size)
            a[size] = null;
        return a;
    }

    /**
     * 通过index获取元素
     * @param index
     * @return E
     */
    public E get(int index) {
        // 范围检查(查看index是否大于0)
        rangeCheck(index);
        return elementData(index);
    }

    /**
     * 覆盖index位置元素为新element,返回旧值
     * @param index
     * @param element
     * @return E
     */
    public E set(int index, E element) {
        // 范围检查
        rangeCheck(index);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /**
     * 元素添加
     * @param e
     * @return boolean
     */
    public boolean add(E e) {
        // 内部容量担保
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    /**
     * 传入最小容量minCapacity
     * @param minCapacity
     */
    private void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    /**
     * 计算容量
     * @param elementData
     * @param minCapacity
     * @return int
     */
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            /**
             * 无参构造首次添加元素会进入此判断
             * DEFAULT_CAPACITY:10===>默认容量,
             */
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }

    /**
     * 明确内部容量
     * @param minCapacity
     */
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;
        // 若目前最小所需容量>elementdata的长度则扩容
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

    /**
     * 扩容
     * @param minCapacity
     */
    private void grow(int minCapacity) {
        // 旧容量
        int oldCapacity = elementData.length;
        // 新容量 = 旧容量 + 旧容量 >> 1(也就是除以2)
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            // 新容量扩容后还是<最小容量,则为新容量赋值为最小容量
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            // 新容量>最大数组容量
            newCapacity = hugeCapacity(minCapacity);
        // 数组拷贝操作(最耗时间的行为,耗时与elementData长度成正比)
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0)
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    /**
     * @param index
     * @param element
     */
    public void add(int index, E element) {
        // 添加的范围检查
        rangeCheckForAdd(index);

        // 内部容量担保
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        elementData[index] = element;
        size++;
    }

    //
    /**
     * 移除index位置的元素
     * @param index
     * @return E
     */
    public E remove(int index) {
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        // 移动元素数量
        int numMoved = size - index - 1;
        if (numMoved > 0)
            // 从index+1位置开始向后取numMoved个值覆盖在从index位置开始
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        // --size位置的元素置空
        elementData[--size] = null;

        return oldValue;
    }

    /**
     * 移除对象o
     * @param o Object
     * @return boolean
     */
    public boolean remove(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index] == null) {
                    fastRemove(index);
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index])) {
                    fastRemove(index);
                    return true;
                }
        }
        return false;
    }

    /**
     * 专用删除方法(大致等同remove(int index))
     * @param index
     */
    private void fastRemove(int index) {
        modCount++;
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                    numMoved);
        elementData[--size] = null;
    }

    // 删除所有元素(置空操作)
    public void clear() {
        modCount++;

        for (int i = 0; i < size; i++)
            elementData[i] = null;
        // size的置0可以使在get()时校验
        size = 0;
    }

    /**
     * 添加元素c(集合)到ArrayList集合中
     * @param c Collcetion
     * @return boolean
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        // 内部容量担保
        ensureCapacityInternal(size + numNew);
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * 添加元素c(集合)到ArrayList集合中的指定位置
     * 涉及数组扩容检查和数组拷贝,效率较低
     * @param index
     * @param c Collection
     * @return boolean
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index, elementData, index + numNew,
                    numMoved);

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * 移除ArrayList集合中c集合包含的元素
     * @param c Collection
     * @return boolean
     */
    public boolean removeAll(Collection<?> c) {
        // 非空校验
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    /**
     * 移除ArrayList集合中c集合未包含的元素(removeAll的兄弟方法)
     * 本人眼拙,实在没看出来此方法有啥意义,若只是排除,为什么不用ArrayList的构造?
     * @param c Collection
     * @return boolean
     */
    public boolean retainAll(Collection<?> c) {
        // 非空校验
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    /**
     * 批量删除
     * @param c Collection
     * @param complement
     * @return boolean
     */
    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] elementData = this.elementData;
        //
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                // 判断集合c是否包含elementData[r]
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
        } finally {
            if (r != size) {
                System.arraycopy(elementData, r,
                        elementData, w,
                        size - r);
                w += size - r;
            }
            // 判断w与size的大小关系
            if (w != size) {
                // 从w位置到size-1位置的元素置空
                for (int i = w; i < size; i++)
                    elementData[i] = null;
                modCount += size - w;
                // 更新size
                size = w;
                modified = true;
            }
        }
        return modified;
    }

    E elementData(int index) {
        return (E) elementData[index];
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

}
