package org.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class Handler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		System.out.println("22222222222222222222222222");
		
		ByteBuf buffer = ctx.alloc().buffer();
		
		buffer.writeLong(System.nanoTime());
		
		ChannelFuture writeAndFlush = ctx.writeAndFlush(buffer);
		
		writeAndFlush.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture f) throws Exception {
				
				Void void1 = f.get();
				System.out.println("333333333333333:"+void1);
				ctx.close();
			}
		});
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
