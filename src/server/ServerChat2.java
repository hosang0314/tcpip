package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerChat2 {
	ServerSocket serverSocket;
	Scanner scanner;
	ArrayList<DataOutputStream> list;
	String chatid;
	
	public ServerChat2() {
	}

	public ServerChat2(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Ready Server ...");
			list = new ArrayList<>();
			start();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	private void start() throws IOException {
		while (true) {
			System.out.println("Server Ready ....");
			Socket socket = serverSocket.accept();
			System.out.println(socket.getInetAddress()+" Connected..");
			Receiver receiver = new Receiver(socket);
			chatid = socket.getInetAddress().getHostAddress();
			receiver.start();
		}
	}

	private void sendAllMsg(String msg) throws IOException {
		Sender sender = new Sender();
		Thread t = new Thread(sender);
		sender.setSendMsg(msg);
		t.start();
	}
	
	
	
	// Message Sender .....................................
	class Sender implements Runnable {
		
		String msg;

		public Sender() {
		}

		public void setSendMsg(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
				if(list.size() != 0) {
					for(DataOutputStream dout:list) {
						dout.writeUTF(msg);
					}
				}
			} catch (IOException e) {
				System.out.println("Not Available");
			}
		}

	}

	// Message Receiver .....................................
	class Receiver extends Thread {
		InputStream in;
		DataInputStream din;
		OutputStream out;
		DataOutputStream dout;
		
		Socket socket;
		public Receiver(Socket socket) throws IOException {
			this.socket = socket;
			in = socket.getInputStream();
			din = new DataInputStream(in);
			out = socket.getOutputStream();
			dout = new DataOutputStream(out);
			list.add(dout);		
			System.out.println("접속자 수:"+list.size());
		}

		public void close() throws IOException {
			in.close();
			din.close();
		}

		@Override
		public void run() {
			String name = "";
			try {
				name = din.readUTF();
				sendAllMsg("#"+name+"님이 들어오셨습니다.");
				
				while(in!=null) {
					sendAllMsg(din.readUTF());
				}
			} catch(IOException e) {
				// ignore
			} finally {
				try {
					sendAllMsg("#"+name+"님이 나가셨습니다.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.remove(name);
				System.out.println("["+socket.getInetAddress() +":"+socket.getPort()+"]"+"에서 접속을 종료하였습니다.");
				System.out.println("현재 서버접속자 수는 "+ list.size()+"입니다.");	
				}

			}
		}
	}








