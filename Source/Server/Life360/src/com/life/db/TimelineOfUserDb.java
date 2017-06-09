package com.life.db;

import com.life.data.k32list32.K32List32;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TimelineOfUserDb extends K32List32 {

    private static final Logger LOGGER = LogManager.getLogger(TimelineOfUserDb.class);

    public static TimelineOfUserDb instance = new TimelineOfUserDb("TimelineOfUser");

    private TimelineOfUserDb(String name) {
        super(name);
    }

    public void init() {

    }

    public static void main(String[] args) {
        int key = -1;
//		System.out.println("put: " + instance.putEntries(key, 10));
//		System.out.println("put: " + instance.putEntries(key, 3));
//		System.out.println("put: " + instance.putEntries(key, 2));
//		System.out.println("put: " + instance.putEntries(key, 5));
//		System.out.println("put: " + instance.putEntries(key, 9));
//        System.out.println("put: " + instance.putEntries(key, 7));
//        System.out.println("put: " + instance.putEntries(key, 8));
//        System.out.println("put: " + instance.putEntries(key, 6));
//        System.out.println("put: " + instance.putEntries(key, 4));

        System.out.println("get: " + instance.get(key));
        System.out.println("");

        System.out.println("get last: " + instance.getLast(key, -3, -5));
        System.out.println("get last: " + instance.getLast(key, -3, 5));
        System.out.println("get last: " + instance.getLast(key, 0, -5));
        System.out.println("get last: " + instance.getLast(key, 0, 5));
        System.out.println("");
        System.out.println("get last: " + instance.getLast(key, 0, 20));
        System.out.println("get last: " + instance.getLast(key, 3, 3));
        System.out.println("get last: " + instance.getLast(key, 3, 9));
        System.out.println("get last: " + instance.getLast(key, 15, 3));

//		System.out.println("");
//		System.out.println("get: " + instance.get(key));
//
//		System.out.println("get last: " + instance.get(key, -3, -5));
//		System.out.println("get last: " + instance.get(key, -3, 5));
//		System.out.println("get last: " + instance.get(key, 0, -5));
//		System.out.println("get last: " + instance.get(key, 0, 5));
//		System.out.println("get last: " + instance.get(key, 3, 3));
//		System.out.println("get last: " + instance.get(key, 3, 15));
//		System.out.println("get last: " + instance.get(key, 15, 3));
    }
}
