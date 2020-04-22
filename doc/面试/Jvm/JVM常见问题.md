#### 常见问题

##### 1. JVM 年轻代到年老代晋升过程？

- 对象在Form和To之间来回复制，年龄达到15（默认值，可通过 MaxTenuringThreshold 修改），晋升老年代

- 分配对象超过eden内存的一半，直接进入老年代。小于eden一半但没有内存空间，进行minor GC，survivor也放不下，进入老年代

- 动态年龄判断，某个年龄对象超过survivor空间的一半，大于等于某个年龄的对象直接进入老年代

##### 2. Minor GC，Major GC和Full GC区别？

- Minor GC：年轻代GC
- Major GC：年老代GC
- Full GC：整个堆空间GC（年轻代和年老代）

##### 3. 发生 Full GC 的几种情况？

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

##### 4. 如何拿到 JVM 的 dump 文件？

- jstack [pid] > xxx.txt：打印堆栈信息到文件
- jstack -l [pid] > xxx.txt：打印堆栈信息（包含锁）到文件
- kill -3
- 添加 JVM 参数：-XX:+HEAPDUMPONOUTOFMEMORYERROR（在内存溢出是保存堆栈信息），一般配合 -XX:HeapDumpPath=PATH 使用（**可用 jinfo 命令运行时修改**）
- jmap -dump:format=b,file=xxx.dump [pid]

##### 5. JVM 出现 fullGC 很频繁，怎么去线上排查问题？

- jstat 查看GC信息观察GC后的堆空间大小并判断是否空间不足导致
  - 空间不足导致：拿到 dump 文件，放入 JVisualVM 观察原因
  - 空间充足：观察程序中有没显示调用 System.gc() 方法

##### 6. 对象如何进行访问定位？

- 句柄：在堆空间中开辟一块空间用来存放句柄池，句柄池存放对象的类型指针和对象的实例指针，reference指针指向句柄

  - 好处：reference指向稳定的句柄，当堆空间进行gc时，对象的移动只会改变句柄中的对象实例指针，对reference没有影响

    <img src="https://img-blog.csdnimg.cn/20200422222425338.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3h4eHh4eHh4eHh4eW4=,size_16,color_FFFFFF,t_70" style="zoom:67%;" >

- 直接指针：堆中实例对象存放实例类型，reference指针直接指向实例对象

  - 好处：速度更快，节省一次指针定位的开销

    <img src="https://img-blog.csdnimg.cn/2020042222245792.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3h4eHh4eHh4eHh4eW4=,size_16,color_FFFFFF,t_70" style="zoom:67%;" >

##### 7. 对象创建过程？

##### 8. GC对象的判定方法？

- 引用计数法：为对象添加计数器，有引用该对象的地方时，计数器+1
  - 优点：效率高，简单
  - 缺点：存在循环引用问题
- 可达性分析算法：通过一系列GcRoots的对象为起点向下搜索，走过的路径为引用链，没在引用链上为废弃对象 
  - **可做为GCRoot的对象**（jdk1.7）：
    - 虚拟机栈引用的对象
    - 方法区中类静态属性引用的变量（static对象）
    - 方法区中常量引用的对象（final对象）
    - 本地方法栈中Native方法引用的变量

##### 9. 四种引用？

- 强引用：new对象就是强引用，JVM抛出OOM也不会清除
- 软引用：JVM内存不足时会回收软引用对象
- 弱引用：JVM只要进行GC就会回收弱引用对象
- 虚引用：幽灵引用，虚引用不会对对象生命周期产生印象，也无法通过虚引用获取对象

##### 10. 垃圾回收算法？

- 标记清除：先标记再清除，有效率问题且会产生大量不连续的内存碎片
- 标记整理：先标记，将标记数据移到内存一端，清除其余部分，有效率问题但不会产生内存碎片
- 复制算法：将内存一份为二，使用一半内存，当垃圾回收时将存活对象放入另一半，然后清理自己的内存。好处是效率高且没有内存碎片，但会牺牲一半内存。在jvm中年轻代使用复制算法，但年轻代对象都是朝生夕死，所以内存分配时eden占用80%，而survivor占用%20来使用
- 分代算法：新生代复制算法，老年代标记整理或标记清除

##### 11. 垃圾收集器？

- Serial收集器：年轻代串行收集器，当GC收集时，工作线程必须暂停(**stop the world**)直至收集结束，使用**复制**算法
- Serial Old收集器：年老代串行收集器，特性和Serial一样，使用**标记整理**算法
- Parallel Scavenge收集器：年轻代并行收集器，吞吐量优先收集器，使用**复制**算法
- Parallel Old收集器：年老代并行收集器，使用**标记整理**算法
- ParNew收集器：年轻代并行收集器，Serial收集器的多线程版本，为了配合CMS而生，**复制**算法
- CMS收集器：年老代并发收集器，目标是最短停顿回收时间，注重用户体验，**标记清除**算法
- 运行过程：
     - 初始标记（Stop-The-World）：仅标记GCRoot能关联到的对象
          - 并发标记：标记GCRoot关联的所有不相关对象（从**并发**就可以看出，他是和用户线程一起工作的）
          - 重新标记（Stop-The-World）：对并发标记期间因用户操作而导致标记变动的对象进行重新标记
          - 并发清除：进行清除
     - **优点：最短停顿回收时间，注重用户体验**
     - **缺点：**
          - **CPU资源敏感**
          - **无法处理浮动垃圾**，浮动垃圾过多时会启用Serial Old收集器
          - **大量内存碎片**，再次分配大对象分配不下时，会引发一次FullGC，通过-XX：+UseCMSCompactAtFullCollection，在要进行FullGC时，进行内存整理（无法并发，慢）
- **G1收集器：**
  <img src="https://img-blog.csdnimg.cn/20200422222524587.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3h4eHh4eHh4eHh4eW4=,size_16,color_FFFFFF,t_70" style="zoom:67%;" >

##### 12. 理解GC日志？

##### 13. JVM常用参数？

[Oracle的官方文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html#BGBCIEFC)

##### 14. 分配担保机制？

在发生Minor GC前，JVM检查新生代总大小是否可被老年代最大连续空间容纳，可容纳时进行Minor GC，否则检查是否允许分配担保失败，如果允许，判断老年代最大连续空间是否大于以往晋升对象的平均大小，若大于，进行Minor GC（有风险，可能这次超出平均大小，老年代接不住）。若小于或不允许分配担保失败，则转为执行Full GC

##### 15. 类加载机制？

- **加载**：

  1. 通过全类名获取类的二进制字节流

  2. 将字节流所代表的的静态存储结构转换为方法区（元空间）的运行时数据结构
  3. 在内存中生成代表该类的Class文件，作为方法区（元空间）这些数据的访问入口

- **验证**：

  1. 文件格式验证
  2. 元数据验证
  3. 字节码验证
  4. 符号引用验证

- **准备**：正式为类变量分配内存并设置类变量初始值（就是默认值），例如`public static int val = 111;`，这里分配的是初值0；

- **解析**：**将虚拟机常量池内的符号引用替换为直接引用的过程**，主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用限定符7种符号引用进行

- **初始化**：执行类构造器`<client>()`方法过程，比如上面的静态变量`val`会被赋值为111

##### 16. 类卸载条件？

类卸载即该类的class对象被GC

1. 该类的所有的实例对象都被GC，即堆中不存在该类的实例对象
2. 该类没有在其他地方被引用
3. 该类的类加载器的实例已被GC

##### 17. 什么是双亲委派模型，为什么使用？

#### 参考：

> [JVM 出现 fullGC 很频繁，怎么去线上排查问题](https://www.jianshu.com/p/e749782fff2b)
>
> [堆内存占用很小 但是 JVM 频繁full gc 问题排查](https://blog.csdn.net/jack85986370/article/details/79892951)
>
> [JVM面试题整理](https://blog.csdn.net/Soinice/article/details/98068404)
>
> [深入理解Java虚拟机(第2版)](https://book.douban.com/subject/24722612/)

