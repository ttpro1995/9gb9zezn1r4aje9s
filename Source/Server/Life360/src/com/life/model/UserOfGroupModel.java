package com.life.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class UserOfGroupModel {

    private static final Logger LOGGER = LogManager.getLogger(UserOfGroupModel.class);

    public static final UserOfGroupModel instance = new UserOfGroupModel();

    private UserOfGroupModel() {
    }



}
