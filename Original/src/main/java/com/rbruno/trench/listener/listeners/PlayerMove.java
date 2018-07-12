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
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerMove extends EngineListner implements Listener {
	
	public PlayerMove(Main main) {
		super(main);
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isDead())
			return;
		if (player.getLocation().getBlockY() < 0) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.teleport(main.trenchConfig.getSpawn());
			player.setFallDistance(0F);
		}
		Location location = player.getLocation();
		if (!(main.getGameState() == GameState.IN_GAME)) return;
		
		if (main.game.getColorTeam(player) == ColorTeam.BLUE) {
			if (location.getBlockX() <= main.trenchConfig.fortRed)
				player.teleport(new Location(player.getWorld(), main.trenchConfig.fortRed + 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		} else {
			if (location.getBlockX() >= main.trenchConfig.fortBlue)
				player.teleport(new Location(player.getWorld(), main.trenchConfig.fortBlue - 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		
		if (main.game.getColorTeam(player) == ColorTeam.BLUE && location.distance(main.trenchConfig.redFlag) <= 1) {
			if (!(main.game.getRedTeam().getFlagHolder() == null))
				return;
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 14) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			main.game.getRedTeam().setFlagHolder(player);
		}
		if (main.game.getColorTeam(player) == ColorTeam.RED && location.distance(main.trenchConfig.blueFlag) <= 1) {
			if (!(main.game.getBlueTeam().getFlagHolder() == null))
				return;
			Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			main.game.getBlueTeam().setFlagHolder(player);
		}

		if (main.game.getRedTeam().getFlagHolder() == player && location.distance(main.trenchConfig.blueFlag) <= 1) {
			if (!(main.game.getBlueTeam().getFlagHolder() == null))
				return;
			main.game.getRedTeam().setFlagHolder(null);
			main.game.getBlueTeam().setScore(main.game.getBlueTeam().getScore() + 1);
			main.game.score[0].setScore(main.game.getBlueTeam().getScore());
			if (main.game.getBlueTeam().getScore() >= 3) {
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.BLUE + "Blue");
				main.clock.endGame();
			} else {
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.BLUE + "Blue");
				main.game.giveItems(player);

			}
		}

		if (main.game.getBlueTeam().getFlagHolder() == player && location.distance(main.trenchConfig.redFlag) <= 1) {
			if (!(main.game.getRedTeam().getFlagHolder() == null))
				return;
			main.game.getBlueTeam().setFlagHolder(null);
			main.game.getRedTeam().setScore(main.game.getRedTeam().getScore() + 1);
			main.game.score[1].setScore(main.game.getRedTeam().getScore());		
			if (main.game.getRedTeam().getScore() >= 3) {
				Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.RED + "Red");
				main.clock.endGame();
			} else {
				Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.RED + "Red");
				main.game.giveItems(player);

			}
		}
		if (player.isOp())
			return;

		if (location.getBlockX() >= main.trenchConfig.maxX) {
			player.teleport(new Location(location.getWorld(), main.trenchConfig.maxX - 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockX() <= main.trenchConfig.minX) {
			player.teleport(new Location(location.getWorld(), main.trenchConfig.minX + 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() <= main.trenchConfig.minZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), main.trenchConfig.minZ + 1, location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() >= main.trenchConfig.maxZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), main.trenchConfig.maxZ - 1, location.getYaw(), location.getPitch()));
		}
	}
}
