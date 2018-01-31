package tcpip1;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		
		String ip = "70.12.111.130";
		int port = 7777;
		Socket socket = null;
		OutputStream outs = null;
		OutputStreamWriter outw = null;
		
		
		
		
		
		try {
			System.out.println("Start Client ....");
			socket = new Socket(ip,port);
			System.out.println("Connected OK ...");
			
			//Send Data ...
			outs = socket.getOutputStream();
			outw = new OutputStreamWriter(outs);
			outw.write("hsyoo");
			System.out.println("Send Completed..");
			
			
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			
			e.printStackTrace();
		} finally {
			try {
				outw.close();
				outs.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}
