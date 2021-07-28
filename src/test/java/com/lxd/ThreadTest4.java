package com.lxd;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * Author: Cris
 * Date: 2021/07/15
 * Time: 15:49
 * Project: blog
 * Description：
 **/
public class ThreadTest4 {
    private final static int CORE_POOL_SIZE = 5;
    private final static int MAXIMUM_POOL_SIZE = 8;
    private final static long KEEP_ALIVE_TIME = 0l;

    @Test
    public void NoThreadTest() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            System.out.println(Thread.currentThread().getName() + "线程执行了==================");
        }
        long end = System.currentTimeMillis();
        System.out.println("共执行====================================" + (end - start) + "ms");
    }

    @Test
    public void ThreadTest() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(5));
        long d1 = 0l;
        for (int i = 0; i < 1000000; i++) {
            long start = System.currentTimeMillis();
            FutureTask futureTask = (FutureTask) threadPoolExecutor.submit(new MyThread(start));
            long d = (long) futureTask.get();
            d1 += d;
        }
        System.out.println("共执行====================================" + d1 + "ms");

    }

    class MyThread implements Callable {
        private long start = 0;

        public MyThread(long start) {
            this.start = start;
        }

        @Override
        public Object call() throws Exception {
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "线程执行了==================" + (end - this.start) + "ms");
            return (end - this.start);
        }
    }
}