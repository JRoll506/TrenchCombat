package com.rbruno.trench.listener.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerMove extends EngineListner implements Listener {

	public PlayerMove(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (!(main.getGameState() == GameState.IN_GAME) && player.getLocation().getBlockY() < 0) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.teleport(main.trenchConfig.getSpawn());
			player.setFallDistance(0F);
		}
	}

	@EventHandler
	public void onPlayerMoveOutofBounds(PlayerMoveEvent event) {
		Player player = event.getPlayer();		

		Location location = player.getLocation();

		if (main.getGameState() == GameState.IN_GAME) {
			if (player.isOp()) return;
			
			/* Move out of other team's fort */
			if (main.game.getColorTeam(player).team.getName().equals("Blue")) {
				if (location.getBlockX() <= main.trenchConfig.fortRed)
					player.teleport(location.add(1, 0, 0));
			} else {
				if (location.getBlockX() >= main.trenchConfig.fortBlue)
					player.teleport(location.subtract(1, 0, 0));
			}
			
			/* Keep players in bounds */
			if (location.getBlockX() >= main.trenchConfig.maxX) {
				player.teleport(location.subtract(1, 0, 0));
			}
			if (location.getBlockX() <= main.trenchConfig.minX) {
				player.teleport(location.add(1, 0, 0));
			}
			if (location.getBlockZ() <= main.trenchConfig.minZ) {
				player.teleport(location.add(0, 0, 1));
			}
			if (location.getBlockZ() >= main.trenchConfig.maxZ) {
				player.teleport(location.subtract(0, 0, 1));
			}
		} else {
			if (location.getBlockY() < 0) {
				player.removePotionEffect(PotionEffectType.JUMP);
				player.teleport(main.trenchConfig.getSpawn());
				player.setFallDistance(0F);
			}
		}
	}
}
