package com.life.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class GroupOfUserModel {

    private static final Logger LOGGER = LogManager.getLogger(GroupOfUserModel.class);


    public static final GroupOfUserModel instance = new GroupOfUserModel();

    private GroupOfUserModel() {

    }

}
