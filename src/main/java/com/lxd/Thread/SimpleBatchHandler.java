package com.lxd.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Cris_liuxd
 * @Date: 2021/05/18/15:15
 * @Description:
 **/
public class SimpleBatchHandler {
    private ExecutorService executorService;
    private MockService mockService;

    private int batch;

    private int ThreadNum;

    public SimpleBatchHandler(MockService service, int batch, int threadNum) {
        this.mockService = service;
        this.batch = batch;
        this.executorService = Executors.newFixedThreadPool(threadNum);
    }

    public void startHandler() {
        long startTime = System.currentTimeMillis();
        System.out.println("start handle time:" + startTime);
        long readData;
        while ((readData = mockService.readData(batch)) != 0) {
            for (int i = 0; i < readData; i++) {
                executorService.execute(() -> mockService.writeData());
            }
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {

        }
        long endTime = System.currentTimeMillis();
        System.out.println("total:" + mockService.getWriteTotal());
    }
}