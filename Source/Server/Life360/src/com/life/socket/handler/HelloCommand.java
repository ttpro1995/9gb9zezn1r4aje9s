package com.life.socket.handler;

import com.life.api.ApiMessage;
import com.life.socket.command.BaseCommand;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class HelloCommand extends BaseCommand {

    @Override
    public ApiMessage execute() {
        
        
        return new ApiMessage("hello");
    }

}
