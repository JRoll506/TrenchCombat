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
		//if (player.isDead()) return;
		
		if (player.getLocation().getBlockY() < 0) {
			player.removePotionEffect(PotionEffectType.JUMP);
			player.teleport(main.trenchConfig.getSpawn());
			player.setFallDistance(0F);
		}
		Location location = player.getLocation();
		if (!(main.getGameState() == GameState.IN_GAME))
			return;





		if (main.game.redTeam.flagHolder == player && location.distance(main.trenchConfig.blueFlag) <= 1) {
			if (!(main.game.blueTeam.flagHolder == null))
				return;
			main.game.redTeam.flagHolder = null;
			main.game.blueTeam.score.setScore(main.game.blueTeam.score.getScore() + 1);
			if (main.game.blueTeam.score.getScore() >= 3) {
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.BLUE + "Blue");
				main.clock.endGame();
			} else {
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.BLUE + "Blue");
				main.game.giveItems(player);
			}
		}

		if (main.game.blueTeam.flagHolder == player && location.distance(main.trenchConfig.redFlag) <= 1) {
			if (!(main.game.redTeam.flagHolder == null))
				return;
			main.game.blueTeam.flagHolder = null;
			main.game.redTeam.score.setScore(main.game.redTeam.score.getScore() + 1);
			if (main.game.redTeam.score.getScore() >= 3) {
				Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.RED + "Red");
				main.clock.endGame();
			} else {
				Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.RED + "Red");
				main.game.giveItems(player);

			}
		}
	}
	
	@EventHandler
	public void onPlayerPickUpFlag(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if (main.getGameState() != GameState.IN_GAME) return;
		
		
		
		if (main.game.getColorTeam(player).getName().equals("Blue") && location.distance(main.trenchConfig.redFlag) <= 1) {
			if (!(main.game.redTeam.flagHolder == null)) return;
			Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 14) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			main.game.redTeam.flagHolder = player;
		}
		if (main.game.getColorTeam(player).getName().equals("Red") && location.distance(main.trenchConfig.blueFlag) <= 1) {
			if (!(main.game.blueTeam.flagHolder == null))
				return;
			Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			main.game.blueTeam.flagHolder = player;
		}
		
		
		EngineTeam team = main.game.getColorTeam(player);
		
		if (player.getLocation().distance(team.team.getName().equals("Red") ? main.trenchConfig.blueFlag : main.trenchConfig.redFlag) <= 1) {
			// TODO
			
		}
	}

	@EventHandler
	public void onPlayerMoveOutofBounds(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		//if (player.isDead()) return;
		

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
