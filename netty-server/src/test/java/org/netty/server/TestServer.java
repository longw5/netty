package org.netty.server;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.netty.server.Request.OpenConnection;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TestServer {

	public static void main(String[] args) throws Exception {
		
		NioEventLoopGroup group = new NioEventLoopGroup();
		
		StringEncoder encoder = new StringEncoder(Charset.forName("utf-8"));
		
		StringDecoder decoder = new StringDecoder(Charset.forName("utf-8"));
		
		ServerBootstrap b = new ServerBootstrap();
		
		b.group(group);
		b.channel(NioServerSocketChannel.class);
		b.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("encoder", encoder);
				ch.pipeline().addLast("decoder", decoder);
				ch.pipeline().addLast(new ServerHandler());
			}
		});
		ChannelFuture sync = b.bind(9999).sync();

		b.bind(new InetSocketAddress("127.0.0.1", 1234)).sync();
		
		System.out.println("server is start.....................");
		sync.channel().closeFuture().sync();
	}
	
	static class ServerHandler extends SimpleChannelInboundHandler<Object>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
			
			System.out.println("开始处理业务..................");
			
			Channel channel = ctx.channel();
			String mmsg = (String)msg;
			
			System.out.println(channel + mmsg);
			
			byte[] byteArray = mmsg.getBytes();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			buffer.put(byteArray);
			
			Buffer flip = buffer.flip();
			
			org.netty.server.Request.OpenConnection.Builder newBuilder = Request.OpenConnection.newBuilder();
			newBuilder.setConnectionid(0);
			newBuilder.setPath("");
			newBuilder.setTimestamp("");
			OpenConnection build2 = newBuilder.build();
			OpenConnection parseFrom = build2.parseFrom(buffer);
			System.out.println("parseFrom:"+parseFrom);
			
			channel.writeAndFlush(mmsg);
		}
		
		@Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	        ctx.flush();
	    }
	}
}
