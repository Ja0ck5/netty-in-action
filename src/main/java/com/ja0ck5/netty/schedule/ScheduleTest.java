package com.ja0ck5.netty.schedule;

import java.util.concurrent.*;

/**
 * Created by Ja0ck5 on 2017/9/7.
 */
public class ScheduleTest {

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        executorService.schedule(() ->
        {
            System.out.println("60 seconds later.");
        }, 60, TimeUnit.SECONDS);
        executorService.shutdown();
    }

}
