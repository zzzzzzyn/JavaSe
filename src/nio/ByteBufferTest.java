package nio;

import java.nio.ByteBuffer;

/**
 * 参考博客：http://www.tianxiaobo.com/2018/03/04/Java-NIO%E4%B9%8B%E7%BC%93%E5%86%B2%E5%8C%BA/
 * 在了解ByteBuffer时，应该先明确四个属性及作用
 * capacity ==》 容量  确定缓冲区的大小(缓冲区实际上是一个数组)
 * position ==》 位置  下一个被读或被写的位置(读写一体)
 * limit    ==》 上界  被读写的最大位置(limit>position)
 * mark     ==》 标记  记录某一次读写的位置(可通过reset()重新回到标记位置)
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/21 21:46
 */
public class ByteBufferTest {
    public static void main(String[] args) {

        byte[] data = ("今朝郡斋冷，忽念山中客").getBytes();

        /**
         * allocateDirect是创建DirectByteBuffer，申请的空间是在*堆外*的，在元空间中
         * allocate是创建HeapByteBuffer，申请的空间是在*堆内*的
         */
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        byteBuffer.put(data);

        /* 模式切换==》实际是改变position，limit和mark的位置 */
        byteBuffer.flip();

        data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);

        System.out.println(new String(data));
    }
}
