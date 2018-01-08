package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.lobby.Lobby;

public class PlayerMove extends EngineListner implements Listener {
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isDead())
			return;
		if (player.getLocation().getBlockY() < 0) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.teleport(Lobby.getSpawn());
			player.setFallDistance(0F);
		}
	}
}
