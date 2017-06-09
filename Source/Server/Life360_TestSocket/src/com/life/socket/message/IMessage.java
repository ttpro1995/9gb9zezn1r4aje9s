package com.life.socket.message;

import java.util.Map;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public interface IMessage {

    int size();

    boolean serializeData();

    boolean deserializeData(Map<String, Object> data);
}
