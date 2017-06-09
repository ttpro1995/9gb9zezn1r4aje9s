package com.life.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.nio.charset.Charset;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class initClient extends ChannelInitializer<SocketChannel> {

    private final static Charset charset = Charset.forName("UTF-8");

    public initClient() {

    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder(charset));
        pipeline.addLast(new StringEncoder(charset));

        pipeline.addLast(new handlerClient());
    }

}
