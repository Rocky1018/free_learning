package concurrent;

import java.util.concurrent.*;

public class Primer {
    public static class Demo extends Thread {
        @Override
        public void run() {
            System.out.println("extends Thread");
        }
    }


    public static void main(String[] args) throws Exception {
        Thread myThread = new Demo();
        myThread.start();
        new Thread(() -> System.out.println("implements runnable")).start();
        ExecutorService executor = Executors.newCachedThreadPool();
        //callable有返回值 支持泛型
        Future<Integer> result = executor.submit(() -> {
            System.out.println("implements callable");
            return 1;
        });
        //获取callable返回值
        System.out.println("callable returns " + result.get(3L, TimeUnit.SECONDS));
        System.out.println("all finished");
        //线程池
        //executor.shutdown();

        //另外有个FutureTask也差不多 主要区别就是submit不反悔future FutureTask可以直接get取值
    }
}
