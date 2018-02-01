package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import server.ServerChat_my.Receiver;
import server.ServerChat_my.Sender;

public class ServerChat_my {
  
	ServerSocket serverSocket;
	Socket socket;
	Scanner scanner;

	public ServerChat_my() {

	}

	public ServerChat_my(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Ready Server ...");
			start();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void start() throws IOException {
		
		
		
		socket = serverSocket.accept();
		scanner = new Scanner(System.in);

		Receiver receiver = new Receiver();
		receiver.start();

		while (true) {
			Sender sender = new Sender();
			Thread t = new Thread(sender);
			System.out.println("Input Server Message ....");
			String msg = scanner.nextLine();
			if (msg.equals("q")) {
				scanner.close();
				sender.close();
				break;
			}
			// System.out.println(msg);
			sender.setSendMsg(msg);
			t.start();

		}
		System.out.println("Exit ServerChat ....");

	}

	// 쓰레드를 생성하는 방식이 다르다.
	// implements 할때와 extends 할때
	// Message Sender ...........................
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

	// Message Receiver ...........................
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
			while (true) {
				String msg = null;
				try {
					msg = din.readUTF();
					System.out.println(msg);
				} catch (IOException e) {
					System.out.println("Exit Client User ...");
					break;
				}
			}
		}
	}
}
