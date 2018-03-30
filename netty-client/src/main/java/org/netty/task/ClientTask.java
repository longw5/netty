package org.netty.task;

import org.netty.client.ProtocolClient;

/**
 * 说明：
 *
 * @author <a href="http://www.waylau.com">waylau.com</a> 2015年11月5日 
 */
public class ClientTask implements Runnable {

	/**
	 * 
	 */
	public ClientTask() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			ProtocolClient client = new ProtocolClient("localhost", 8082);
			client.run();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
