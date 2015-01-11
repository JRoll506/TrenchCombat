package com.rbruno.engine.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.rbruno.engine.listener.EngineListner;

public class SignCreation extends EngineListner implements Listener{
	@EventHandler
	public void signCreation(SignChangeEvent event) {
		for (int i = 0; i < 4; i++) {
			event.setLine(i, event.getLine(i).replace("&", "§"));
		}
	}


}
