package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;

public class PlayerRespawn extends EngineListner implements Listener {
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();
		if (Game.game.getColorTeam(player) == ColorTeam.RED) {
			event.setRespawnLocation(Game.getMap().getRedSpawn());
			Game.game.giveItems(player);
		} else {
			event.setRespawnLocation(Game.getMap().getBlueSpawn());
			Game.game.giveItems(player);
		}

	}
}
