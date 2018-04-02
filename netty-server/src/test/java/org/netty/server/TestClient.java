package org.netty.server;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

import org.netty.server.Request.OpenConnection;
import org.netty.server.Request.OpenConnection.Builder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TestClient {

	public static void main(String[] args) throws Exception {
		
		StringEncoder encoder = new StringEncoder(Charset.forName("UTF8"));
		
		StringDecoder decoder = new StringDecoder(Charset.forName("UTF8"));
		
		NioEventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap bootstrap = new Bootstrap();
		
		bootstrap.group(group);
		
		bootstrap.channel(NioSocketChannel.class);
		
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
			
				ch.pipeline().addLast("encoder", encoder);
				ch.pipeline().addLast("decoder", decoder);
				ch.pipeline().addLast(new ClientHandler());
			}
		});
		
		ChannelFuture sync = bootstrap.connect("127.0.0.1", 1234).sync();
		
		Channel channel = sync.channel();
		
		Builder openConnection = Request.OpenConnection.newBuilder();
		openConnection.setConnectionid(1);
		openConnection.setPath("/test");
		openConnection.setTimestamp(System.nanoTime() + "");
		OpenConnection build = openConnection.build();
		String mmmsg = new String(build.toByteArray());
		channel.writeAndFlush(mmmsg);
		System.out.println("is send ................"+mmmsg);
		
		/*int xx = new Random().nextInt(100)%2;
//		System.out.println(xx);
		if( xx == 0){
			String msg = "1";
			System.out.println(msg);
			channel.writeAndFlush(msg);
		}else {
			String msg = "2";
			System.out.println(msg);
			channel.writeAndFlush(msg);
		}*/
		
		System.out.println("client is send msg.................");
	}
	
	static class ClientHandler extends SimpleChannelInboundHandler<Object>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
			
			Channel channel = ctx.channel();
			
			String mmsg = (String) msg;
			
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
			
			channel.writeAndFlush("2222222222222222222");
		}
		
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.close();
		}
	}
	
}
