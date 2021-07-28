package com.lxd.Thread;

/**
 * @Author: Cris_liuxd
 * @Date: 2021/05/18/15:51
 * @Description:
 **/
public class SimpleBatchHandlerTest {
    public static void main(String[] args) {
        long total = 10000;
        long sleepTime = 100;
        int batch = 100;
        int threadNum = 16;
        MockService mockService = new MockService(total, sleepTime);
        SimpleBatchHandler handler = new SimpleBatchHandler(mockService, batch, threadNum);
        handler.startHandler();
    }
}