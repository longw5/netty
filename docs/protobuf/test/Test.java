package com.beagle.protobuf.test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.beagle.protobuf.domain.Request;
import com.beagle.protobuf.domain.Request.OpenConnection;
import com.beagle.protobuf.domain.Request.OpenConnection.Builder;

public class Test {

	public static void main(String[] args) throws Exception {

		Builder openConnection = Request.OpenConnection.newBuilder();

		openConnection.setConnectionid(1);
		openConnection.setPath("test");
		openConnection.setTimestamp(System.nanoTime() + "");

		OpenConnection build = openConnection.build();

		for (byte b : build.toByteArray()) {
			System.out.println(b);
		}

		System.out.println();
		System.out.println(new String(build.toByteArray()));
		System.out.println("================================");

		byte[] byteArray = build.toByteArray();

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.put(byteArray);
		
		Buffer flip = buffer.flip();
		
		Builder newBuilder = Request.OpenConnection.newBuilder();
		newBuilder.setConnectionid(0);
		newBuilder.setPath("");
		newBuilder.setTimestamp("");
		OpenConnection build2 = newBuilder.build();
		
		OpenConnection parseFrom = build2.parseFrom(buffer);
		System.out.println(parseFrom);
		
	}
}
