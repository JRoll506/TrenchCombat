package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.classes.EngineClass;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;


public class PlayerRespawn extends EngineListner implements Listener {
	
	public PlayerRespawn(Main main) {
		super(main);
	}
	
	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		final Player player = (Player) event.getPlayer();
		if (main.getGameState() == GameState.IN_GAME) {
			// Send to player's team spawn
			event.setRespawnLocation(main.game.getColorTeam(player).spawn);
			main.game.giveItems(player);
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				
				@Override
				public void run() {
					EngineClass playerClass = main.classManager.classMap.get(player);
					if (playerClass.getEffect() != null) {
						player.addPotionEffect(playerClass.getEffect());
					}
				}
			});
		} else {
			// Send to lobby spawn
			event.setRespawnLocation(main.trenchConfig.getSpawn());
			player.getInventory().clear();
		}
	}
}
