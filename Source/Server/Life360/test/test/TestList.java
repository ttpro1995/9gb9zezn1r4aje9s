package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class TestList {

    public static void main(String[] args) {

        List<Integer> lst = new ArrayList<>(10);

        Integer[] arr = new Integer[5];

        arr[3] = 5;

        lst = Arrays.asList(arr);

        System.out.println("lst: " + lst);
    }
}
