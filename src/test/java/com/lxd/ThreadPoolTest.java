package com.lxd;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Author: Cris
 * Date: 2021/07/14
 * Time: 15:32
 * Project: blog
 * Description：
 **/
public class ThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int temp = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "====" + temp);
                }
            });
        }
        Long end = System.currentTimeMillis();
        executorService.shutdown();

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
        }
        long end1 = System.currentTimeMillis();

        System.out.println("线程===========================================================" + (end - start));
        System.out.println(end1 - start1);
    }

}