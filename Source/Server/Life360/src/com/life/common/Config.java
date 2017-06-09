package com.life.common;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Config {

    public static Config instance = new Config();
    private INIConfiguration config;

    private Config() {
        File f = new File("conf/development.config.ini");
        Configurations c = new Configurations();
        try {
            config = c.ini(f);
        } catch (ConfigurationException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getInt">
    public int getInt(String key) {
        return config.getInt(key);
    }

    public int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    public int getInt(Class clazz, String name, String key) {
        return config.getInt(clazz.getSimpleName() + "@" + name + "." + key);
    }

    public int getInt(Class clazz, String name, String key, int defaultValue) {
        return config.getInt(clazz.getSimpleName() + "@" + name + "." + key, defaultValue);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getInt">
    public long getLong(String key) {
        return config.getLong(key);
    }

    public long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    public long getLong(Class clazz, String name, String key) {
        return config.getLong(clazz.getSimpleName() + "@" + name + "." + key);
    }

    public long getLong(Class clazz, String name, String key, long defaultValue) {
        return config.getLong(clazz.getSimpleName() + "@" + name + "." + key, defaultValue);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getDouble">
    public double getDouble(String key) {
        return config.getDouble(key);
    }

    public double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    public double getDouble(Class clazz, String name, String key) {
        return config.getInt(clazz.getSimpleName() + "@" + name + "." + key);
    }

    public double getDouble(Class clazz, String name, String key, double defaultValue) {
        return config.getDouble(clazz.getSimpleName() + "@" + name + "." + key, defaultValue);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getString">
    public String getString(String key) {
        return config.getString(key);
    }

    public String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    public String getString(Class clazz, String name, String key) {
        return config.getString(clazz.getSimpleName() + "@" + name + "." + key);
    }

    public String getString(Class clazz, String name, String key, String defaultValue) {
        return config.getString(clazz.getSimpleName() + "@" + name + "." + key, defaultValue);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getBoolean">
    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    public boolean getBoolean(Class clazz, String name, String key) {
        return config.getBoolean(clazz.getSimpleName() + "@" + name + "." + key);
    }

    public boolean getBoolean(Class clazz, String name, String key, boolean defaultValue) {
        return config.getBoolean(clazz.getSimpleName() + "@" + name + "." + key, defaultValue);
    }
    //</editor-fold>

}
