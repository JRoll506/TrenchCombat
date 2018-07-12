package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerJoin extends EngineListner implements Listener {
	
	public PlayerJoin(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME)) {
			for (PotionEffect effect : event.getPlayer().getActivePotionEffects())
				event.getPlayer().removePotionEffect(effect.getType());
			event.getPlayer().getInventory().setArmorContents(null);
			event.getPlayer().getInventory().clear();
			event.getPlayer().teleport(main.getSpawn());
			// main.messagePlayer(event.getPlayer(), "The game will begin shortly!");
		} else if (main.getGameState() == GameState.IN_GAME) {
			main.getGame().addPlayer(event.getPlayer());
		}
	}

}
