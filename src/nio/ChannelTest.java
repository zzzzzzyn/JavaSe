package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 套接字通道的学习
 * 服务端: ServerSocketChannel
 * 客户端: SocketChannel
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/23 9:22
 */
public class ChannelTest {}


/**
 * 服务端
 */
class Sever {
    private static final String EXIT = "exit";
    private int port;

    public Sever(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        System.out.println("*****服务端已启动*****");
        SocketChannel socketChannel = serverSocketChannel.accept();

        Scanner sc = new Scanner(System.in);
        while (true) {
            // 接收信息
            System.out.println("客户端消息: " + MsgUtil.recvMsg(socketChannel));

            System.out.print("请输入: ");
            String msg = sc.nextLine();

            if (EXIT.equals(msg))
                break;

            // 发送信息
            MsgUtil.sendMsg(socketChannel, msg);
        }
        socketChannel.close();
        serverSocketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new Sever(9527).start();
    }
}

/**
 * 客户端
 */
class Client {

    private static final String EXIT = "exit";
    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(address, port));

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("请输入: ");
            String msg = sc.nextLine();

            if (EXIT.equals(msg))
                break;

            // 发送信息
            MsgUtil.sendMsg(socketChannel, msg);

            // 接收信息
            System.out.println("服务端消息: " + MsgUtil.recvMsg(socketChannel));
        }
        socketChannel.close();
    }

    public static void main(String[] args) throws IOException {
        new Client("127.0.0.1", 9527).start();
    }
}

class MsgUtil{

    public static void sendMsg(ByteChannel channel, String msg) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(122);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();
        channel.write(byteBuffer);
    }

    public static String recvMsg(ByteChannel channel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(122);
        channel.read(byteBuffer);
        byteBuffer.flip();
        /**
         * byte数组的定义一定要放在flip切换之后
         * 否则limit会是不确定的
         */
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        return new String(data);
    }
}