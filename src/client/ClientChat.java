package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import client.ClientChat.Receiver;
import client.ClientChat.Sender;

public class ClientChat {
	String ip;
	int port;
	Socket socket;
	Scanner scanner;

	public ClientChat() {

	}

	public ClientChat(String ip, int port) {
		this.ip = ip;
		this.port = port;
		try {
			socket = new Socket(ip, port);
			System.out.println("Connected Server ...");
			start();
		} catch (IOException e) {
			System.out.println("Connection Refused ...");
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void start() throws IOException {
		scanner = new Scanner(System.in);
		
		Receiver receiver = new Receiver();
		receiver.start();
		
		while (true) {
			//Runnable에서 상속받으면 아래와 같이 사용해야 한다.
			Sender sender = new Sender();
			Thread t = new Thread(sender);
			System.out.println("Input Client Message ..");
			String msg = scanner.nextLine();
			if (msg.equals("q")) { // 종료
				scanner.close(); // 스캐너도 종료
				sender.close();
				break;
			}
			//System.out.println(msg);
			sender.setSendMsg(msg);
			t.start();
			
		}
		System.out.println("Exit Chatting ... ");
	}

	// 쓰레드를 생성하는 방식이 다르다.
	// implements 할때와 extends 할때
	// Message Sender  ...........................
	class Sender implements Runnable {
		OutputStream out;
		DataOutputStream dout;
		String msg;

		public Sender() throws IOException {
			out = socket.getOutputStream();
			dout = new DataOutputStream(out);

		}

		public void setSendMsg(String msg) {
			this.msg = msg;
		}
		public void close() throws IOException {
			dout.close();
			out.close();
		}
		@Override
		public void run() {
			try {
				if (dout != null) {
					dout.writeUTF(msg);
				}
			} catch (IOException e) {
				System.out.println("Not Available");
			} 

		}

	}

	
	//Message Receiver  ...........................
	class Receiver extends Thread {
		InputStream in;
		DataInputStream din;
		
		public Receiver() throws IOException {
			in = socket.getInputStream();
			din = new DataInputStream(in);
		}
		
		public void close() throws IOException {
			in.close();
			din.close();
		}
		
		@Override
		public void run() {
			while(true) {
				String msg = null;
				try {
					msg = din.readUTF();
					System.out.println(msg);
				} catch (IOException e) {
					System.out.println("Exit Server User ...");
					break;
				}
			}
		}
	}

}
