package concurrent.lock.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 这个是并发编程艺术里的demo
 * 和join的用法类似，不过这个功能更多一些
 * 使用时可以把CountDownLatch的引用传入线程或同步资源中
 */
public class CountDownLatchTest {

    static CountDownLatch c = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(1);
                // 调用cuntDown后，c内部sync的同步状态就-1
                c.countDown();
                System.out.println(2);
                c.countDown();
                System.out.println(3);
                int i = 1;
                while (true) {
                    System.out.println("循环" + i);
                    if (i > 20)
                        break;
                    i++;
                }
            }
        }).start();
        // 调用await后，线程等待，直至c内部sync的同步状态为0为止
        c.await();
        // 等待一秒后返回(防止等待时间过长)
        // c.await(1, TimeUnit.SECONDS);
        System.out.println("主线程执行完成");
    }
}
