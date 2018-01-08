package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.lobby.Lobby;

public class PlayerRespawn extends EngineListner implements Listener {
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();

		event.setRespawnLocation(Lobby.getSpawn());
		player.getInventory().clear();

	}
}
