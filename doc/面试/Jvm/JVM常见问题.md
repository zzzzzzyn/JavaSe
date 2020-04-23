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

1. 类加载检查：虚拟机遇到一条new指令，首先检查指令的参数能否在常量池（元空间）中定位到这个类的符号引用，并检查这个类是否被加载、解析和初始化过，如果没有则执行类加载过程
2. **分配内存**：对象所需内存大小在类加载完成后便可确定，为对象在堆中划出一块
   - **分配内存的两种方式**：
     - **指针碰撞**：堆空间使用时是规整的，只需要把分界点的指针移动对象大小位置即可
     - **空闲列表**：堆空间使用时不是规整的，空闲区域维护在一个列表中，分配时先查询列表，分配完之后更新列表
     - **堆空间分配是否规整由所采用的的垃圾收集器的算法决定**
   - 并发情况下内存空间的安全分配的两种方式（跟锁的安全策略其实差不多，原理上相同）
     - **CAS失败重试**，直至成功
     - **TLAB**，预先为每个线程在eden区分配空间，JVM在线程中分配内存时首先在TLAB分配，TLAB剩余空间不足或用尽时，采用CAS失败重试继续进行内存分配
3. 初始化零值：虚拟机需要将内存空间都初始化为零值，保证对象实例字段在java中不经赋初始值就可被调用
4. 设置对象头：设置类的元数据信息、对象的哈希码、GC分代年龄。根据虚拟机当前运行状态的不同，决定是否使用偏向锁
5. 执行init方法：一般来说，执行`new`指令之后会执行`init`方法，把对象按照我们的意愿进行初始化，这样一个真正可用的对象才算完全产生出来

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

[JVM常用参数指南](https://snailclimb.gitee.io/javaguide/#/docs/java/jvm/最重要的JVM参数指南)

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

  <img src="https://img-blog.csdnimg.cn/20200423112106659.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3h4eHh4eHh4eHh4eW4=,size_16,color_FFFFFF,t_70" style="zoom:67%;" >

##### 16. 类卸载条件？

类卸载即该类的class对象被GC

1. 该类的所有的实例对象都被GC，即堆中不存在该类的实例对象
2. 该类没有在其他地方被引用
3. 该类的类加载器的实例已被GC

##### 17. 类加载器？

对任意一个类，都需要加载它的类加载器和这个类本身一同确立其在Java虚拟机中的唯一性，每一个类加载器都有一个独立的类名称空间

JVM内置三个重要的类加载器：

- 启动类加载器（Bootstrap ClassLoader）：使用C++语言实现，是虚拟机自身的一部分
- 扩展类加载器（Extension CLassLoader）：加载`<JAVA_HOME>\lib'ext`目录中的，或被`java.ext.dirs`系统变量所指定的路径中的所有类库
- 应用程序类加载器（Application ClassLoader）：这个类加载器是`ClassLoader`中的`getSystemClassLoader()`方法的返回值，**负责加载用户类路径（ClassPath）上的指定类库，我们写的类文件被这个类所加载**

##### 18. 什么是双亲委派模型，为什么使用？

类加载器之间的层次关系称为双亲委派模型，双亲委派模型要求除了顶层的启动类加载器外，其余的类加载器都要有自己的父类加载器

双亲委派模型工作过程：如果一个类加载器收到类加载请求，它首先不会自己去加载这个类，而是委托给父类加载器加载，因此所有加载请求都会传送到最顶层启动类加载器中，父类加载器不能加载时会反馈自己无法完成加载请求，子类加载器才会尝试加载。大致就是（**有事往上报，上头处理不了，会扔回来让你自己处理**）

为什么使用：上面说过一个类的唯一性由加载它的类加载器和类本身一同决定。举个栗子：假如没有使用双亲委派模型，你自己写了一个java.lang.Object类，你自己加载，我使用的时候怎么知道到底用哪个Object。所以使用双亲委派模型是为了**保证程序的稳定运行**

<img src="https://img-blog.csdnimg.cn/20200423120155116.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3h4eHh4eHh4eHh4eW4=,size_16,color_FFFFFF,t_70" style="zoom:67%;" >

#### 参考：

> [JVM 出现 fullGC 很频繁，怎么去线上排查问题](https://www.jianshu.com/p/e749782fff2b)
>
> [堆内存占用很小 但是 JVM 频繁full gc 问题排查](https://blog.csdn.net/jack85986370/article/details/79892951)
>
> [JVM面试题整理](https://blog.csdn.net/Soinice/article/details/98068404)
>
> [深入理解Java虚拟机(第2版)](https://book.douban.com/subject/24722612/)

