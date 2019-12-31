##### JVM GC 相关参数	

```java
-XX:+PrintGC 输出 GC 日志
-XX:+PrintGCDetails 输出 GC 的详细日志
-XX:+PrintGCTimeStamps 输出 GC 的时间戳（以基准时间的形式）
-XX:+PrintGCDateStamps 输出 GC 的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
-XX:+PrintHeapAtGC 在进行 GC 的前后打印出堆的信息
-Xloggc:D:/gc.log 日志文件的输出路径
```

示例

```java
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:D:/gc.log
```

打印的GC日志如下：

##### YongGC

> 2019-04-18T14:52:06.790+0800: 2.653: [GC (Allocation Failure) [PSYoungGen: 33280K->5113K(38400K)] 33280K->5848K(125952K), 0.0095764 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]

含义：

> 2019-04-18T14:52:06.790+0800（当前时间戳）: 2.653（应用启动基准时间）: [GC (Allocation Failure) [PSYoungGen（表示 Young GC）: 33280K（年轻代回收前大小）->5113K（年轻代回收后大小）(38400K（年轻代总大小）)] 33280K（整个堆回收前大小）->5848K（整个堆回收后大小）(125952K（堆总大小）), 0.0095764（耗时） secs] [Times: user=0.00（用户耗时） sys=0.00（系统耗时）, real=0.01（实际耗时） secs]

##### Full GC

> 2019-04-18T14:52:15.359+0800: 11.222: [Full GC (Metadata GC Threshold) [PSYoungGen: 6129K->0K(143360K)] [ParOldGen: 13088K->13236K(55808K)] 19218K->13236K(199168K), [Metaspace: 20856K->20856K(1069056K)], 0.1216713 secs] [Times: user=0.44 sys=0.02, real=0.12 secs]

含义：

> 2019-04-18T14:52:15.359+0800（当前时间戳）: 11.222（应用启动基准时间）: [Full GC (Metadata GC Threshold) [PSYoungGen: 6129K（年轻代回收前大小）->0K（年轻代回收后大小）(143360K（年轻代总大小）)] [ParOldGen: 13088K（老年代回收前大小）->13236K（老年代回收后大小）(55808K（老年代总大小）)] 19218K（整个堆回收前大小）->13236K（整个堆回收后大小）(199168K（堆总大小）), [Metaspace: 20856K（持久代回收前大小）->20856K（持久代回收后大小）(1069056K（持久代总大小）)], 0.1216713（耗时） secs] [Times: user=0.44（用户耗时） sys=0.02（系统耗时）, real=0.12（实际耗时） secs]

> 原文: [方志朋](https://mp.weixin.qq.com/s?__biz=MzAxNjk4ODE4OQ==&mid=2247487895&idx=3&sn=017615c91b090335488716702888db01&chksm=9bed30e5ac9ab9f355be25f69d3a5a20f6b253a209161c53a637164ae8efd7afc8b496b042c0&mpshare=1&scene=1&srcid=&sharer_sharetime=1577532750774&sharer_shareid=3bb946bb3f112036e8ee16cfda55ce1b&key=48aea93bd5a329e5e30708e546f1d6a57d808e546fb9bfeadfbe6f857ad7487509bcbc82c05670070f607290079e9c6d5674837214a46d5d64483f8e2d5b8ecbe59583bbf51b695baf2b0c282b51e6db&ascene=1&uin=OTcwNzU3OTAy&devicetype=Windows+10&version=62070158&lang=zh_CN&exportkey=AenqWh8xGxheakkqqR05s1M%3D&pass_ticket=3zseOruDQRiA2RjWl2f9Y25uMAKa%2Bd3%2BJaCQ5qth6L5JTeeIRHr%2BBZC5mpgqEWoa)