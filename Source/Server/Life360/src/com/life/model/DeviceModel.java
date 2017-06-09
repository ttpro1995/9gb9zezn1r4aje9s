package com.life.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class DeviceModel {

    private static final Logger LOGGER = LogManager.getLogger(DeviceModel.class);

    public static final DeviceModel instance = new DeviceModel();

    private DeviceModel() {
    }
}
