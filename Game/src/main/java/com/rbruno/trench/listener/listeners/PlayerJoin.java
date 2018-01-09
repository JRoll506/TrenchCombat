package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerJoin extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (Game.getPlugin().getGameState() == GameState.LOADING) {
			event.getPlayer().teleport(Game.getSpawn());
		} else {
			Game.getGame().addPlayer(event.getPlayer());
		}
	}

}
