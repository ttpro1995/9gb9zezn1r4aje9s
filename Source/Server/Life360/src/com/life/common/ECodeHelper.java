package com.life.common;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ECodeHelper {

    public static boolean isSuccess(ECode eCode) {
        return eCode.getValue() >= 0;
    }

    public static boolean isFail(ECode eCode) {
        return eCode.getValue() < 0;
    }

    public static boolean isSuccess(int eCode) {
        return eCode >= 0;
    }

    public static boolean isFail(int eCode) {
        return eCode < 0;
    }

    public static int mkfail(ECode eCode) {
        return -eCode.getValue();
    }

    public static int mkSuccess(ECode eCode) {
        return eCode.getValue();
    }

}
