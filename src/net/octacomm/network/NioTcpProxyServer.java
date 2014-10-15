package net.octacomm.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

import net.octacomm.sample.netty.proxy.handler.ProxyInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Taeyoung,Kim
 */
public class NioTcpProxyServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

    private int localPort;
	private String remoteHost;
	private int remotePort;
	
    private	ChannelFuture channelFuture;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NioTcpProxyServer(int localPort, String remoteHost, int remotePort) {
    	this.localPort = localPort;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
    }

    public void init() {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
        		.channel(NioServerSocketChannel.class)
        		.childHandler(new ProxyInitializer(remoteHost, remotePort))
        		.childOption(ChannelOption.AUTO_READ, false)
        		.handler(new LoggingHandler());
        
        logger.debug("---------- Netty Option ----------");
        logger.debug(" tcpNoDelay is true");
        logger.debug(" keepAlive is true");

        // Bind and start to accept incoming connections.
       	channelFuture = bootstrap.bind(new InetSocketAddress(localPort));
        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
    
    public void close() {
    	if (channelFuture != null) {
    		try {
				channelFuture.channel().close().sync();
			} catch (InterruptedException e) {
				logger.info("", e.toString());
			}
    		logger.info("releaseExternalResources");
    		bossGroup.shutdownGracefully();
    		workerGroup.shutdownGracefully();
    	}
    }
}
