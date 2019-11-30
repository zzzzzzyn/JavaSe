package concurrent.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * ArrayBlockingQueue ---> 数组实现的有界阻塞队列，FIFO原则对元素进行排序
 * 用ArrayBlockingQueue做消息队列实现一个简单的生产消费模型
 * put()阻塞添加方法
 * task()阻塞移除方法
 *
 * @author xyn
 * @description 描述信息
 * @data 2019/11/30 17:30
 */
public class ArrayBlockingQueueTest {
    public static void main(String[] args) {

        /**
         * 这里的ArrayBlockingQueue可以根据需求替换为其他的BlockingQueue
         * 不过相应的生产消费也需要根据需求进行更替
         */
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(2);
        Produce produce = new Produce(blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);
        Thread t1 = new Thread(produce);
        Thread t2 = new Thread(consumer);
        t1.start();
        t2.start();
    }

    /**
     * 生产者
     */
    static class Produce implements Runnable {
        private BlockingQueue blockingQueue;

        public Produce(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
            System.out.println("Produce: 开始生产");
        }

        @Override
        public void run() {
            int i = 0;
            while (true) {
                try {
                    blockingQueue.put(i);
                    System.out.println("生产: " + i);
                    // 两秒放入一次(这就决定消费者必须等待生产)
                    Thread.sleep(2000);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消费者
     */
    static class Consumer implements Runnable {
        private BlockingQueue blockingQueue;

        public Consumer(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
            System.err.println("Consumer: 开始消费");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 一秒消费一次
                    Thread.sleep(5000);
                    System.err.println("消费: " + blockingQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
