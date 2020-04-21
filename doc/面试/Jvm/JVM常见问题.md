#### 常见问题

1. JVM 年轻代到年老代晋升过程？
   - 对象在Form和To之间来回复制，年龄达到15（默认值，可通过 MaxTenuringThreshold 修改），晋升老年代
   - 分配对象超过eden内存的一半，直接进入老年代。小于eden一半但没有内存空间，进行minor GC，survivor也放不下，进入老年代
   - 动态年龄判断，大于等于某个年龄对象超过survivor空间的一半，大于等于某个年龄的对象直接进入老年代
2. Minor GC，Major GC和Full GC区别？
   - Minor GC：年轻代GC
   - Major GC：年老代GC
   - Full GC：整个堆空间GC（年轻代和年老代）
3. **发生 Full GC 的几种情况？**
   - System.gc() 方法显示调用（只是建议进行GC）
   - **老年代空间不足**：只有在**新生代对象转入**或**大对象（数组等）**分配时，老年代才会出现内存不足情况，若 Full GC后空间依旧不足，抛出**内存溢出异常**
   - CMS GC时出现 **promotion failed** 和 **concurrent mode failure**
     - promotion failed：发生Minor GC时，survivor space放不下了，只能晋升老年代，老年代也放不下
     - concurrent mode failure：执行CMS GC的同时有对象要放入老年代，而此时老年代空间不足造成的（有时候“空间不足”是CMS GC时当前的浮动垃圾过多导致暂时性的空间不足触发Full GC）
   - 统计的到的Minor GC晋升老年代对象平均大小大于老年代剩余空间
     - Hotspot为避免晋升失败，会统计之前每次Minor GC后晋升对象的平均大小是否可以被老年代容纳，不能的话会触发Full GC
     - 例如第一次Minor GC后有6M晋升到老年代，那么下次Minor GC发生时就会检查老年代是否有6M大小的空间
     - 新生代使用PS GC（并发GC收集器）时，PS GC只对当前负责，检查本次要晋升的对象大小能否被老年代容纳，不能的话出触发Full GC
   - 堆中分配很大的对象：特指需要大量连续空间的对象
   - 调用了 jmap -histo:live [pid]：立即出发Full GC
4. **如何拿到 JVM 的 dump 文件？**
   - jstack [pid] > xxx.txt：打印堆栈信息到文件
   - jstack -l [pid] > xxx.txt：打印堆栈信息（包含锁）到文件
   - kill -3
   - 添加 JVM 参数：-XX:+HEAPDUMPONOUTOFMEMORYERROR（在内存溢出是保存堆栈信息），一般配合 -XX:HeapDumpPath=PATH 使用（**可用 jinfo 命令运行时修改**）
   - jmap -dump:format=b,file=xxx.dump [pid]
5. **JVM 出现 fullGC 很频繁，怎么去线上排查问题？**
   - jstat 查看GC信息观察GC后的堆空间大小并判断是否空间不足导致
     - 空间不足导致：拿到 dump 文件，放入 JVisualVM 观察原因
     - 空间充足：观察程序中有没显示调用 System.gc() 方法
6. 垃圾回收算法？
   - 标记清除：先标记再清除，有效率问题且会产生大量不连续的内存碎片
   - 标记整理：先标记，将标记数据移到内存一端，清除其余部分，有效率问题但不会产生内存碎片
   - 复制算法：将内存一份为二，使用一半内存，当垃圾回收时将存活对象放入另一半，然后清理自己的内存。好处是效率高且没有内存碎片，但会牺牲一半内存。在jvm中年轻代使用复制算法，但年轻代对象都是朝生夕死，所以内存分配时eden占用80%，而survivor占用%20来使用
7. 垃圾收集器？
   - 
8. GC对象的判定方法？
   - 引用计数法：为对象添加计数器，有引用该对象的地方时，计数器+1
        - 优点：效率高，简单
        - 缺点：存在循环引用问题
   - 可达性分析算法：通过一系列GcRoots的对象为起点向下搜索，走过的路径为引用链，没在引用链上为废弃对象 
9. 什么是双亲委派模型，为什么使用？

##### 参考：

> [JVM 出现 fullGC 很频繁，怎么去线上排查问题](https://www.jianshu.com/p/e749782fff2b)
>
> [堆内存占用很小 但是 JVM 频繁full gc 问题排查](https://blog.csdn.net/jack85986370/article/details/79892951)
>
> [JVM面试题整理](https://blog.csdn.net/Soinice/article/details/98068404)

