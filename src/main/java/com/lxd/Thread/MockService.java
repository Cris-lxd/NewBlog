package com.lxd.Thread;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: Cris_liuxd
 * @Date: 2021/05/15/19:39
 * @Description:
 **/
public class MockService {

    /**
     * 可读取总数
     */
    private long canReadTotal;

    /**
     * 写入总数
     */
    private AtomicLong writeTotal = new AtomicLong(0);

    /**
     * 写入休眠单位ms
     */
    private final long sleepTime;

    /**
     * @param canReadTotal
     * @param sleepTime
     */
    public MockService(long canReadTotal, long sleepTime) {
        this.canReadTotal = canReadTotal;
        this.sleepTime = sleepTime;
    }

    /**
     * 读取数据接口
     *
     * @param num
     * @return
     */
    public synchronized long readData(int num) {
        long readNum;
        if (canReadTotal >= num) {
            canReadTotal = num;
            readNum = num;
        } else {
            readNum = canReadTotal;
            canReadTotal = 0;
        }
        return readNum;
    }

    /**
     * 写入数据接口
     */
    public void writeData() {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread:" + Thread.currentThread() + "write data:" + writeTotal.incrementAndGet());
    }

    /**
     * 获取写入的总数
     *
     * @return
     */
    public long getWriteTotal() {
        return writeTotal.get();
    }
}