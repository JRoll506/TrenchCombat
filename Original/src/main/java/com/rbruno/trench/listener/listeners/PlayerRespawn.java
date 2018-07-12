package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;


public class PlayerRespawn extends EngineListner implements Listener {
	
	public PlayerRespawn(Main main) {
		super(main);
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();
		if (main.getGameState() == GameState.IN_GAME) {
			if (main.game.getColorTeam(player) == ColorTeam.RED) {
				event.setRespawnLocation(main.getMap().getRedSpawn());
				main.game.giveItems(player);
			} else {
				event.setRespawnLocation(main.getMap().getBlueSpawn());
				main.game.giveItems(player);
			}
		} else {
			event.setRespawnLocation(main.getSpawn());
			player.getInventory().clear();
		}
	}
}
