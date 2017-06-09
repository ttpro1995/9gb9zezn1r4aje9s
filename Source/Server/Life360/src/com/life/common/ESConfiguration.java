package com.life.common;

import org.elasticsearch.action.support.WriteRequest;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public final class ESConfiguration {

    public static ESConfiguration instance = new ESConfiguration();

    private String name;
    private String host;
    private int port;
    private String index;
    private int numberOfShards;
    private int numberOfReplicas;
    private long restartInterval;

    private WriteRequest.RefreshPolicy refreshPolicy;

    private ESConfiguration() {
        reload();
    }

    public void reload() {
        name = Config.instance.getString(this.getClass(), "main", "name", "");
        host = Config.instance.getString(this.getClass(), "main", "host", "127.0.0.1");
        port = Config.instance.getInt(this.getClass(), "main", "port", 9300);
        index = Config.instance.getString(this.getClass(), "main", "index", "ES");
        numberOfShards = Config.instance.getInt(this.getClass(), "main", "numberOfShards", 2);
        numberOfReplicas = Config.instance.getInt(this.getClass(), "main", "numberOfReplicas", 0);
        restartInterval = Config.instance.getLong(this.getClass(), "main", "restartInterval", 1000);
        boolean instantRefresh = Config.instance.getBoolean(this.getClass(), "main", "instantRefresh", false);
        if (instantRefresh) {
            refreshPolicy = WriteRequest.RefreshPolicy.IMMEDIATE;
        } else {
            refreshPolicy = WriteRequest.RefreshPolicy.NONE;
        }
    }

    public static ESConfiguration getInstance() {
        return instance;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getIndex() {
        return index;
    }

    public int getNumberOfShards() {
        return numberOfShards;
    }

    public int getNumberOfReplicas() {
        return numberOfReplicas;
    }

    public long getRestartInterval() {
        return restartInterval;
    }

    public WriteRequest.RefreshPolicy getRefreshPolicy() {
        return refreshPolicy;
    }
    
    


}
