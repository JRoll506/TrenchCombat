package com.rbruno.engine.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class PlayerMove extends EngineListner implements Listener {
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isDead()) return;
		if (player.getLocation().getBlockY() < 0) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.teleport(Main.getSpawn());
			player.setFallDistance(0F);
		}
		Location location = player.getLocation();
		if (Main.getGameState() == GameState.LOBBY) {
			return;
		}
		if (Main.game.getColorTeam(player) == ColorTeam.BLUE) {
			if (location.getBlockX() <= Main.trenchConfig.fortRed) player.teleport(new Location(player.getWorld(), Main.trenchConfig.fortRed + 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		} else {
			if (location.getBlockX() >= Main.trenchConfig.fortBlue) player.teleport(new Location(player.getWorld(), Main.trenchConfig.fortBlue - 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (Main.game.getColorTeam(player) == ColorTeam.BLUE && location.getBlockX() == Main.trenchConfig.redFlagX && location.getBlockY() == Main.trenchConfig.redFlagY && location.getBlockZ() == Main.trenchConfig.redFlagZ) {
			if (!(Main.game.getRedTeam().getFlagHolder() == null)) return;
			Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 14) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Main.game.getRedTeam().setFlagHolder(player);
		}
		if (Main.game.getColorTeam(player) == ColorTeam.RED && location.getBlockX() == Main.trenchConfig.blueFlagX && location.getBlockY() == Main.trenchConfig.blueFlagY && location.getBlockZ() == Main.trenchConfig.blueFlagZ) {
			if (!(Main.game.getBlueTeam().getFlagHolder() == null)) return;
			Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Main.game.getBlueTeam().setFlagHolder(player);
		}

		if (Main.game.getRedTeam().getFlagHolder() == player && location.getBlockX() == Main.trenchConfig.blueFlagX && location.getBlockY() == Main.trenchConfig.blueFlagY && location.getBlockZ() == Main.trenchConfig.blueFlagZ) {
			if (!(Main.game.getBlueTeam().getFlagHolder() == null)) return;
			Main.game.getRedTeam().setFlagHolder(null);
			Main.game.getBlueTeam().score++;
			Main.game.score[0].setScore(Main.game.getRedTeam().score);
			if (Main.game.getBlueTeam().score == 3) {
				Main.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.BLUE + "Blue");
				Main.clock.endGame();
			} else {
				Main.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.BLUE + "Blue");
				Main.game.giveItems(player);

			}
		}

		if (Main.game.getBlueTeam().getFlagHolder() == player && location.getBlockX() == Main.trenchConfig.redFlagX && location.getBlockY() == Main.trenchConfig.redFlagY && location.getBlockZ() == Main.trenchConfig.redFlagZ) {
			if (!(Main.game.getRedTeam().getFlagHolder() == null)) return;
			Main.game.getBlueTeam().setFlagHolder(null);
			Main.game.getRedTeam().score++;
			Main.game.score[1].setScore(Main.game.getRedTeam().score);
			if (Main.game.getBlueTeam().score == 3) {
				Main.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.RED + "Red");
				Main.clock.endGame();
			} else {
				Main.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.RED + "Red");
				Main.game.giveItems(player);

			}
		}
		if (Main.getGameState() == GameState.LOBBY || player.isOp()) return;

		if (location.getBlockX() >= Main.trenchConfig.maxX) {
			player.teleport(new Location(location.getWorld(), Main.trenchConfig.maxX - 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockX() <= Main.trenchConfig.minX) {
			player.teleport(new Location(location.getWorld(), Main.trenchConfig.minX + 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() <= Main.trenchConfig.minZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Main.trenchConfig.minZ + 1, location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() >= Main.trenchConfig.maxZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Main.trenchConfig.maxZ - 1, location.getYaw(), location.getPitch()));
		}
	}
}
