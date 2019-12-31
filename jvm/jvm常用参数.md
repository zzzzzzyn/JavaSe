##### jvm常用参数
```java
# 堆分配参数
-Xms512M                    # 堆内存最小为512M
-Xmx512M                    # 堆内存最大为512M(防止抖动)
-Xss1M                      # 堆栈大小，默认1M
-XX:NewRatio=2              # 设置年轻代与年老代的比例为2:1
-XX:SurvivorRatio=8         # 设置年轻代中eden区与survivor区的比例为8：1(年轻代有两个survivor空间)
-XX:MetaspaceSize=64M       # 设置元空间初始化大小为64M(取代-XX:PermSize)
-XX:MaxMetaspaceSize=128M   # 设置元空间最大大小为64M(取代-XX:MaxPermSize)
-XX:TargetSurvivorRatio=50  # 设置survivor区使用率。当survivor区达到50%时，将对象送入老年代
-XX:MaxTenuringThreshold=15 # 对象进入老年代的年龄（Parallel是15，CMS是6）
-XX:+UseTLAB                # 在年轻代空间中使用本地线程分配缓冲区(TLAB)，默认开启
-XX:TLABSize=512k           # 设置TLAB大小为512k
-XX:+UseCompressedOops      # 使用压缩指针，默认开启
# 垃圾收集器
-XX:MaxGCPauseMillis        # 设置最大垃圾收集停顿时间（收集器工作时会调整其他参数大小，尽可能将停顿控制在指定时间内）
-XX:+UseAdaptiveSizePolicy  # 打开自适应GC策略（该摸式下，各项参数都会被自动调整）
-XX:+UseSerialGC            # 在年轻代和年老代使用串行回收器
-XX:+UseParallelGC          # 使用并行垃圾回收收集器，默认会同时启用 -XX:+UseParallelOldGC（默认使用该回收器）
-XX:+UseParallelOldGC       # 开启老年代使用并行垃圾收集器，默认会同时启用 -XX:+UseParallelGC
-XX:ParallelGCThreads=4     # 设置用于垃圾回收的线程数为4（默认与CPU数量相同）
-XX:+UseConcMarkSweepGC     # 使用CMS收集器（年老代）
-XX:CMSInitiatingOccupancyFraction=80   # 设置CMS收集器在年老代空间被使用多少后触发
-XX:+CMSClassUnloadingEnabled   # 允许对类元数据进行回收
-XX:+UseCMSInitiatingOccupancyOnly  # 只在达到阈值的时候，才进行CMS回收
-XX:+UseG1GC                # 使用G1回收器
-XX:G1HeapRegionSize=16m    # 使用G1收集器时设置每个Region的大小（范围1M - 32M）
-XX:MaxGCPauseMillis=500    # 设置最大暂停时间（毫秒）
-XX:+DisableExplicitGC      # 禁止显示GC的调用（即禁止开发者的 System.gc()）
# GC日志
-XX:+PrintGC                # 输出 GC 日志
-XX:+PrintGCDetails         # 输出 GC 的详细日志
-XX:+PrintGCTimeStamps      # 输出 GC 的时间戳（以基准时间的形式）
-XX:+PrintGCDateStamps      # 输出 GC 的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
-XX:+PrintHeapAtGC          # 在进行 GC 的前后打印出堆的信息
-Xloggc:D:/gc.log           # 日志文件的输出路径
# 堆快照
-XX:+HeapDumpOnOutOfMemoryError # 出现内存溢出时存储堆信息，配合 -XX:HeapDumpPath 使用
-XX:HeapDumpPath=D:/oom.log     # 堆快照存储位置
-XX:+UseLargePages              # 使用大页  
-XX:LargePageSizeInBytes=4m     # 指定大页的大小（必须为2的幂）
# 滚动日志记录
-XX:+UseGCLogFileRotation       # 开启滚动日志记录
-XX:NumberOfGCLogFiles=5        # 滚动数量，命名为filename.0, filename.1 .....  filename.n-1,  然后再从filename.0 开始，并覆盖已经存在的文件
-XX:GCLogFileSize=8k            # 每个文件大小，当达到该指定大小时，会写入下一个文件
-Xloggc:/gc/log                 # 日志文件位置
```
> 转载: [CSDN](https://blog.csdn.net/liyongbing1122/article/details/88716400)

> 官网: [Oracle官方文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html#BGBCIEFC)