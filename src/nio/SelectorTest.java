package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 简单到令人发指的http服务器，目前只能返回200^_^
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/24 22:44
 */
public class SelectorTest {

    public static String RESPONSE_TEXT = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\nhello";

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
        ssc.configureBlocking(false);

        // 创建selector
        Selector selector = Selector.open();

        // 注册selector并制定感兴趣的事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {

            // 就绪的通道数量
            int readyNum = selector.select();
            if (readyNum == 0)
                continue;

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {

                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {

                    ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    socketChannel.read(byteBuffer);
                    byteBuffer.flip();
                    byte[] data = new byte[byteBuffer.limit()];
                    byteBuffer.get(data);
                    System.out.println(new String(data));
                    selectionKey.interestOps(SelectionKey.OP_WRITE);

                } else if (selectionKey.isWritable()) {

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.put(RESPONSE_TEXT.getBytes());
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                    socketChannel.close();

                }
            }
        }
    }
}
