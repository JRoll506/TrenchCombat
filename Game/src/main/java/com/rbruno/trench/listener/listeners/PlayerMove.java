package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.Game;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerMove extends EngineListner implements Listener {
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isDead())
			return;
		
		if (player.getLocation().getBlockY() < 0) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.teleport(Game.getSpawn());
			player.setFallDistance(0F);
		}
		Location location = player.getLocation();
		if (Game.getPlugin().getGameState() != GameState.PLAYING) {
			return;
		}

		if (Game.game.getColorTeam(player) == ColorTeam.BLUE) {
			if (location.getBlockX() <= Game.trenchConfig.fortRed)
				player.teleport(new Location(player.getWorld(), Game.trenchConfig.fortRed + 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		} else {
			if (location.getBlockX() >= Game.trenchConfig.fortBlue)
				player.teleport(new Location(player.getWorld(), Game.trenchConfig.fortBlue - 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (Game.game.getColorTeam(player) == ColorTeam.BLUE && location.getBlockX() == Game.trenchConfig.redFlagX && location.getBlockY() == Game.trenchConfig.redFlagY && location.getBlockZ() == Game.trenchConfig.redFlagZ) {
			if (!(Game.game.getRedTeam().getFlagHolder() == null))
				return;
			Game.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 14) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Game.game.getRedTeam().setFlagHolder(player);
		}
		if (Game.game.getColorTeam(player) == ColorTeam.RED && location.getBlockX() == Game.trenchConfig.blueFlagX && location.getBlockY() == Game.trenchConfig.blueFlagY && location.getBlockZ() == Game.trenchConfig.blueFlagZ) {
			if (!(Game.game.getBlueTeam().getFlagHolder() == null))
				return;
			Game.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Game.game.getBlueTeam().setFlagHolder(player);
		}

		if (Game.game.getRedTeam().getFlagHolder() == player && location.getBlockX() == Game.trenchConfig.blueFlagX && location.getBlockY() == Game.trenchConfig.blueFlagY && location.getBlockZ() == Game.trenchConfig.blueFlagZ) {
			if (!(Game.game.getBlueTeam().getFlagHolder() == null))
				return;
			Game.game.getRedTeam().setFlagHolder(null);
			Game.game.getBlueTeam().setScore(Game.game.getBlueTeam().getScore() + 1);
			Game.game.score[0].setScore(Game.game.getBlueTeam().getScore());
			if (Game.game.getBlueTeam().getScore() >= 3) {
				Game.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.BLUE + "Blue");
				Game.clock.endGame();
			} else {
				Game.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.BLUE + "Blue");
				Game.game.giveItems(player);

			}
		}

		if (Game.game.getBlueTeam().getFlagHolder() == player && location.getBlockX() == Game.trenchConfig.redFlagX && location.getBlockY() == Game.trenchConfig.redFlagY && location.getBlockZ() == Game.trenchConfig.redFlagZ) {
			if (!(Game.game.getRedTeam().getFlagHolder() == null))
				return;
			Game.game.getBlueTeam().setFlagHolder(null);
			Game.game.getRedTeam().setScore(Game.game.getRedTeam().getScore() + 1);
			Game.game.score[1].setScore(Game.game.getRedTeam().getScore());		
			if (Game.game.getRedTeam().getScore() >= 3) {
				Game.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.RED + "Red");
				Game.clock.endGame();
			} else {
				Game.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.RED + "Red");
				Game.game.giveItems(player);

			}
		}

		if (location.getBlockX() >= Game.trenchConfig.maxX) {
			player.teleport(new Location(location.getWorld(), Game.trenchConfig.maxX - 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockX() <= Game.trenchConfig.minX) {
			player.teleport(new Location(location.getWorld(), Game.trenchConfig.minX + 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() <= Game.trenchConfig.minZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Game.trenchConfig.minZ + 1, location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() >= Game.trenchConfig.maxZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Game.trenchConfig.maxZ - 1, location.getYaw(), location.getPitch()));
		}
	}
}
