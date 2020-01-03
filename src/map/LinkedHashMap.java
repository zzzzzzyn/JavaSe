//package map;
//
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 在HashMap的基础上维护了一个双向链表
// * 此处只有LinkedHashMap的少量方法
// */
//public class LinkedHashMap<K,V>
//        extends HashMap<K,V>
//        implements Map<K,V>
//{
//    /**
//     * 继承自HashMap的Node
//     * 在Node基础上添加before和after来构成链表
//     */
//    static class Entry<K,V> extends HashMap.Node<K,V> {
//        Entry<K,V> before, after;
//        Entry(int hash, K key, V value, Node<K,V> next) {
//            super(hash, key, value, next);
//        }
//    }
//
//    private static final long serialVersionUID = 3801124242820219131L;
//
//    /**
//     * 链表头
//     */
//    transient Entry<K,V> head;
//
//    /**
//     * 链表尾
//     */
//    transient Entry<K,V> tail;
//
//    /**
//     * 此属性为true时会调用afterNodeAccess方法
//     */
//    final boolean accessOrder;
//
//    // internal utilities
//
//    // 链接到尾部
//    private void linkNodeLast(Entry<K,V> p) {
//        Entry<K,V> last = tail;
//        tail = p;
//        if (last == null)
//            head = p;
//        else {
//            p.before = last;
//            last.after = p;
//        }
//    }
//
//    // 将src的首尾链接到dst
//    private void transferLinks(Entry<K,V> src,
//                               Entry<K,V> dst) {
//        Entry<K,V> b = dst.before = src.before;
//        Entry<K,V> a = dst.after = src.after;
//        if (b == null)
//            head = dst;
//        else
//            b.after = dst;
//        if (a == null)
//            tail = dst;
//        else
//            a.before = dst;
//    }
//
//    // 在链表中干掉e
//    void afterNodeRemoval(Node<K,V> e) { // unlink
//        Entry<K,V> p =
//                (Entry<K,V>)e, b = p.before, a = p.after;
//        p.before = p.after = null;
//        if (b == null)
//            head = a;
//        else
//            b.after = a;
//        if (a == null)
//            tail = b;
//        else
//            a.before = b;
//    }
//
//    void afterNodeInsertion(boolean evict) { // possibly remove eldest
//        Entry<K,V> first;
//        if (evict && (first = head) != null && removeEldestEntry(first)) {
//            K key = first.key;
//            removeNode(hash(key), key, null, false, true);
//        }
//    }
//
//    /**
//     * 将e放到链尾,accessOrder为true时才会调用
//     * 查阅资料得知:在做LRUCache(最近最久未使用)时,会使用
//     */
//    void afterNodeAccess(Node<K,V> e) { // move node to last
//        Entry<K,V> last;
//        if (accessOrder && (last = tail) != e) {
//            Entry<K,V> p =
//                    (Entry<K,V>)e, b = p.before, a = p.after;
//            p.after = null;
//            if (b == null)
//                head = a;
//            else
//                b.after = a;
//            if (a != null)
//                a.before = b;
//            else
//                last = b;
//            if (last == null)
//                head = p;
//            else {
//                p.before = last;
//                last.after = p;
//            }
//            tail = p;
//            ++modCount;
//        }
//    }
//
//    void internalWriteEntries(java.io.ObjectOutputStream s) throws IOException {
//        for (Entry<K,V> e = head; e != null; e = e.after) {
//            s.writeObject(e.key);
//            s.writeObject(e.value);
//        }
//    }
//
//    /**
//     * 构造
//     * 都是通过调用hashmap构造来进行初始化的
//     */
//    public LinkedHashMap(int initialCapacity, float loadFactor) {
//        super(initialCapacity, loadFactor);
//        accessOrder = false;
//    }
//
//    /**
//     * 构造
//     */
//    public LinkedHashMap(int initialCapacity) {
//        super(initialCapacity);
//        accessOrder = false;
//    }
//
//    /**
//     * 构造
//     */
//    public LinkedHashMap() {
//        super();
//        accessOrder = false;
//    }
//
//    /**
//     * 构造
//     */
//    public LinkedHashMap(Map<? extends K, ? extends V> m) {
//        super();
//        accessOrder = false;
//        putMapEntries(m, false);
//    }
//
//    /**
//     * 构造
//     */
//    public LinkedHashMap(int initialCapacity,
//                         float loadFactor,
//                         boolean accessOrder) {
//        super(initialCapacity, loadFactor);
//        this.accessOrder = accessOrder;
//    }
//
//
//    /**
//     * 通过遍历链表来查看是否存在value
//     */
//    public boolean containsValue(Object value) {
//        for (Entry<K,V> e = head; e != null; e = e.after) {
//            V v = e.value;
//            if (v == value || (value != null && value.equals(v)))
//                return true;
//        }
//        return false;
//    }
//
//    /**
//     * 调用hashmap的getNode获取值
//     */
//    public V get(Object key) {
//        Node<K,V> e;
//        if ((e = getNode(hash(key), key)) == null)
//            return null;
//        if (accessOrder)
//            afterNodeAccess(e);
//        return e.value;
//    }
//
//    /**
//     * 获取值(未取到空返回默认值)
//     */
//    public V getOrDefault(Object key, V defaultValue) {
//        Node<K,V> e;
//        if ((e = getNode(hash(key), key)) == null)
//            return defaultValue;
//        if (accessOrder)
//            afterNodeAccess(e);
//        return e.value;
//    }
//
//    /**
//     * 清空
//     */
//    public void clear() {
//        super.clear();
//        head = tail = null;
//    }
//
//    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
//        return false;
//    }
//}