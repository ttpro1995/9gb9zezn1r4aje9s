package com.life.common;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Log4j2Config {

    static {
        String log4jConfigFile = "conf/log4j2.xml";
        File file = new File(log4jConfigFile);
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(file.toURI());
//        Configuration configuration = context.getConfiguration();
//
//        FileAppender fileAppender = configuration.getAppender("FILE");
//        File f = new File(fileAppender.getFileName());
//        if (!file.exists()) {
//            try {
//                f.createNewFile();
//            } catch (IOException ex) {
//                Logger.getLogger(Log4j2Config.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    public static void init() {

    }

}
