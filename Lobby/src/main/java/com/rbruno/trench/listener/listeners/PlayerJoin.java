package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;

import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.lobby.Lobby;
import com.rbruno.trench.timer.LobbyState;

public class PlayerJoin extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (Lobby.getPlugin().getLobbyState() == LobbyState.PLAYING
				|| Lobby.getPlugin().getLobbyState() == LobbyState.MOVING) {
			Lobby.sendToGame(event.getPlayer());
			// TODO kick player
			return;
		}
		for (PotionEffect effect : event.getPlayer().getActivePotionEffects())
			event.getPlayer().removePotionEffect(effect.getType());
		event.getPlayer().getInventory().setArmorContents(null);
		event.getPlayer().getInventory().clear();
		event.getPlayer().teleport(Lobby.getSpawn());
	}

}
