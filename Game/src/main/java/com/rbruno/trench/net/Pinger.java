package com.rbruno.trench.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Pinger implements Runnable {

	private Socket socket;

	public Pinger() {
		this.socket = null;
	}

	public Pinger(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		if (socket == null) {
			ServerSocket serverSocket = null;
			try {
				while (true) {
					serverSocket = new ServerSocket(5000);
					Socket socket = serverSocket.accept();
					Thread thread = new Thread(new Pinger(socket));
					thread.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
				try {
					if (serverSocket != null) serverSocket.close();
				} catch (IOException e1) {
				}
			}

		} else {
			try {
				OutputStream out = socket.getOutputStream();
				
				byte[] buffer = new byte[4];
				InputStream in = socket.getInputStream();
				in.read(buffer);
				
				buffer = "PONG".getBytes();
				out.write(buffer);
				out.flush();
				
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

}
