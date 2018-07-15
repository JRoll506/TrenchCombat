package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.EngineTeam;
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
	public void onPlayerCaptureFlag(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (main.getGameState() != GameState.IN_GAME) return;
		
		EngineTeam team = main.game.getColorTeam(player);
		
		if (player.getLocation().distance(team.flag) <= 1) {
			for (EngineTeam targetTeam : main.game.teams) {
				if (targetTeam.flagHolder == player) {
					targetTeam.flagHolder = null;
					team.score.setScore(team.score.getScore() + 1);
					Bukkit.getServer().broadcastMessage(team.color + player.getName() + ChatColor.WHITE + " has captured " + targetTeam.color + targetTeam.team.getName() + ChatColor.WHITE +  "'s flag!");
					main.game.giveItems(player);
				}
			}
		}
	}
	
	
	
	@EventHandler
	public void onPlayerPickUpFlag(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (main.getGameState() != GameState.IN_GAME) return;
		
		EngineTeam team = main.game.getColorTeam(player);
		
		for (EngineTeam targetTeam : main.game.teams) {
			if (team != targetTeam && player.getLocation().distance(targetTeam.flag) <= 1 && targetTeam.flagHolder == null) {
				targetTeam.flagHolder = player;
				Bukkit.getServer().broadcastMessage(team.color + player.getName() + ChatColor.WHITE + " has taken the " + targetTeam.team.getPrefix() + targetTeam.team.getName() + " " + ChatColor.WHITE + "flag");
				
				player.getInventory().clear();
				// TODO Fix
				ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11)};
				kit[0].setAmount(64);
				for (int i = 0; i < 9; i++) {
					player.getInventory().addItem(kit);
				}
			}
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
					player.teleport(new Location(player.getWorld(), main.trenchConfig.fortRed + 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
			} else {
				if (location.getBlockX() >= main.trenchConfig.fortBlue)
					player.teleport(new Location(player.getWorld(), main.trenchConfig.fortBlue - 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
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
