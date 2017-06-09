package com.life.socket.command;

import com.life.api.ApiMessage;
import com.life.socket.entities.Packet;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public abstract class BaseCommand {

    private int command;
    private Packet packet;

    public void init(int command, Packet packet) {
        this.command = command;
        this.packet = packet;

    }

    public int getCommand() {
        return command;
    }

    public Packet getPacket() {
        return packet;
    }

    public abstract ApiMessage execute();
}
