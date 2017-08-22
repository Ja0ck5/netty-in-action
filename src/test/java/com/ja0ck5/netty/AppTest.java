package com.ja0ck5.netty;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    @org.junit.Test
    public void testParallel() throws InterruptedException {
        ArrayList<Integer> list = new ArrayList<>(1000);

        list.stream().parallel().forEach(i -> System.out.println(i + " id: " + Thread.currentThread().getId() + ", name: " + Thread.currentThread().getName()));

        list.parallelStream().forEach(i -> System.out.println("second " + i + " id: " + Thread.currentThread().getId() + ", name: " + Thread.currentThread().getName()));

        Thread.sleep(50000);
    }

}
