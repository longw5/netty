package org.netty.server;

import org.netty.server.Request.OpenConnection;
import org.netty.server.Request.OpenConnection.Builder;

public class TestProtobuf {

	public static void main(String[] args) {
		
		Builder openConnection = Request.OpenConnection.newBuilder();

		openConnection.setConnectionid(1);
		openConnection.setPath("test");
		openConnection.setTimestamp(System.nanoTime() + "");

		OpenConnection build = openConnection.build();
		
		System.out.println(build);
		
		byte[] byteArray = build.toByteArray();
		
		
	}
	
}
