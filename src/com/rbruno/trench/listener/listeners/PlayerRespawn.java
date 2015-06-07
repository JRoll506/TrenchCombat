package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;


public class PlayerRespawn extends EngineListner implements Listener{
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();
		if (Main.getGameState() == GameState.IN_GAME) {
			if (Main.game.getColorTeam(player) == ColorTeam.RED) {
				event.setRespawnLocation(Main.getMap().getRedSpawn());
				Main.game.giveItems(player);
			} else {
				event.setRespawnLocation(Main.getMap().getBlueSpawn());
				Main.game.giveItems(player);
			}
		} else {
			event.setRespawnLocation(Main.getSpawn());
			player.getInventory().clear();
		}
	}
}
