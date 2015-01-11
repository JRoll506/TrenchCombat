package com.rbruno.engine.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class PlayerJoin extends EngineListner implements Listener{
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (Main.getGameState() == GameState.LOBBY) {
			event.getPlayer().getInventory().setArmorContents(null);
			event.getPlayer().getInventory().clear();
			event.getPlayer().teleport(Main.getSpawn());
			//Main.messagePlayer(event.getPlayer(), "The game will begin shortly!");
		} else if (Main.getGameState() == GameState.IN_GAME) {
			Main.getGame().addPlayer(event.getPlayer());
		}
	}

}
