package com.rbruno.engine.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;


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
