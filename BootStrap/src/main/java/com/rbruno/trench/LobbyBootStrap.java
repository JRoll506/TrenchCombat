package com.rbruno.trench;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.bukkit.craftbukkit.Main;

public class LobbyBootStrap {

	public static void main(String[] args) throws InterruptedException {
		while(pingGame("game2")) {
			Thread.sleep(500L);
		}

		Main.main(args);
	}

	public static boolean pingGame(String game) {
		try {
			Socket socket = new Socket(game, 5000);
			OutputStream out  = socket.getOutputStream();
			byte[] buffer = "PING".getBytes();
			out.write(buffer);
			out.flush();
			
			InputStream in = socket.getInputStream();
			in.read(buffer);
			socket.close();
			
			return buffer.toString().equals("PONG");
		} catch (Exception e) {
			return false;
		} 

	}

}