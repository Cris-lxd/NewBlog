package com.lxd;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: Cris
 * Date: 2021/07/14
 * Time: 16:05
 * Project: blog
 * Description：
 **/
public class ThreadTest3 {
    @Test
    public void testThread() {

        final int corePoolSize = 3;
        final int maximumPoolSize = 5;
        long keepAliveTime = 0L;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new ArrayBlockingQueue<>(3));
        threadPoolExecutor.execute(new MyThread(1000));
        threadPoolExecutor.execute(new MyThread(1000));

    }

    public List test(int i) {
        List<Map> list = new ArrayList<>();
        Map map = new HashMap();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        list.add(map);
        System.out.println(Thread.currentThread().getName() + "================================" + i);
        return list;
    }

    class MyThread implements Runnable {
        private int num;

        public MyThread(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                test(i);
            }
            long end = System.currentTimeMillis();
            System.out.println("============================================" + (end - start));
        }
    }
}