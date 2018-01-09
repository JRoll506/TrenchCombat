package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.listener.EngineListner;

public class PlayerJoin extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		Game.getGame().addPlayer(event.getPlayer());

	}

}
