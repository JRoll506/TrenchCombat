package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.lobby.Lobby;

public class PlayerJoin extends EngineListner implements Listener{
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
			for (PotionEffect effect : event.getPlayer().getActivePotionEffects())
				event.getPlayer().removePotionEffect(effect.getType());
			event.getPlayer().getInventory().setArmorContents(null);
			event.getPlayer().getInventory().clear();
			event.getPlayer().teleport(Lobby.getSpawn());
			//Main.messagePlayer(event.getPlayer(), "The game will begin shortly!");
		
	}

}
