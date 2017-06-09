package com.life.common;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class HRequest {

    private static String _getParameterAsString(HttpServletRequest req, String paramName) throws NotExistException, InvalidParamException {
        if (paramName == null) {
            throw new InvalidParamException("Parameter name is null");
        }
        paramName = paramName.trim();
        if (paramName.isEmpty()) {
            throw new InvalidParamException("Parameter name is empty");
        }

        String strVal = req.getParameter(paramName);
        if (strVal == null) {
            throw new NotExistException();
        }
        return strVal;
    }

    private static String _getParameterAsTrimString(HttpServletRequest req, String paramName) throws NotExistException, InvalidParamException {
        return _getParameterAsString(req, paramName).trim();
    }

    ////////////////////////////////////////////////////////////////////////////
    ///specdatatype read with throwing exceptions
    ///
    public static Boolean getBoolean(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        String strVal = _getParameterAsString(req, paramName);

        Boolean ret = Boolean.parseBoolean(strVal);
        if (null != ret) {
            return ret;
        } else {
            throw new NumberFormatException("Wrong format while parsing boolean");
        }
    }

    public static Byte getByte(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        return Byte.parseByte(_getParameterAsTrimString(req, paramName));
    }

    public static Double getDouble(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        return Double.parseDouble(_getParameterAsTrimString(req, paramName));
    }

    public static Float getFloat(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        return Float.parseFloat(_getParameterAsTrimString(req, paramName));
    }

    public static Integer getInt(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        return Integer.parseInt(_getParameterAsTrimString(req, paramName));
    }

    public static Long getLong(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        return Long.parseLong(_getParameterAsTrimString(req, paramName));
    }

    public static Short getShort(HttpServletRequest req, String paramName) throws NotExistException, NumberFormatException, InvalidParamException {
        return Short.parseShort(_getParameterAsTrimString(req, paramName));
    }

    public static String getString(HttpServletRequest req, String paramName) throws NotExistException, InvalidParamException {
        return _getParameterAsString(req, paramName);
    }


    public static Boolean getBoolean(HttpServletRequest req, String paramName, Boolean defaultVal) {
        try {
            return getBoolean(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Byte getByte(HttpServletRequest req, String paramName, Byte defaultVal) {
        try {
            return getByte(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Double getDouble(HttpServletRequest req, String paramName, Double defaultVal) {
        try {
            return getDouble(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Float getFloat(HttpServletRequest req, String paramName, Float defaultVal) {
        try {
            return getFloat(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Integer getInt(HttpServletRequest req, String paramName, Integer defaultVal) {
        try {
            return getInt(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Long getLong(HttpServletRequest req, String paramName, Long defaultVal) {
        try {
            return getLong(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static Short getShort(HttpServletRequest req, String paramName, Short defaultVal) {
        try {
            return getShort(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }

    public static String getString(HttpServletRequest req, String paramName, String defaultVal) {
        try {
            return getString(req, paramName);
        } catch (Exception ex) {
            return defaultVal;
        }
    }


    private static class InvalidParamException extends Exception {

        public InvalidParamException(String parameter_name_is_null) {
            super(parameter_name_is_null);
        }
    }

    private static class NotExistException extends Exception {

        public NotExistException() {
        }
    }
}
