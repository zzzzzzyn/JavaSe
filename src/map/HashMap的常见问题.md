HashMap的常见问题:

- hashmap1.7和1.8在实现上都有什么区别
- hashmap1.8的结构组成(往数组，链表，红黑树优缺点分析)
- 元素的插入，删除和扩容(在这块有很多可以扩展的点,尽情发挥)
- 元素插入是是如何确定位置的
- 为什么数组容量为2^n
- 负载因子的作用
- hashmap和hashtable的区别
- 为什么1.7的实现在多线程情况下会形成链表环而1.8却不会，1.8就是安全的了吗
    - 1.7扩容使用头插入的方式
    - 1.8扩容使用尾插入的方式
- 并发情况下hashmap的安全问题及解决方案(带出ConcurrentHashmap又能扯一波^_^)
    - 使用HashTable
    - 使用Collections.synchronizedMap()方法包装map
    - 使用ConcurrentHashmap(推荐)

> 参考网站: https://juejin.im/post/59e86f9351882521ad0f4147 


