package com.rbruno.engine.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;


public class PlayerDeath extends EngineListner implements Listener{
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
		if (Main.getGameState() == GameState.IN_GAME) {
			if (Main.getGame().getColorTeam(player) == ColorTeam.RED) {
				if (player.getKiller() instanceof Player) {
					if (player.getKiller().getItemInHand().getType() == Material.ARROW) {
						player.sendMessage(ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you with his gun!");
						player.getKiller().sendMessage("You have killed " + player.getName() + " with your gun!");
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					} else if (player.getKiller().getItemInHand().getType() == Material.IRON_SWORD || player.getKiller().getItemInHand().getType() == Material.DIAMOND_SWORD) {
						player.sendMessage(ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you with his sword!");
						player.getKiller().sendMessage("You have killed " + player.getName() + " with your sword!");
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					} else if (player.getKiller().getItemInHand().getType() == Material.BONE) {
						player.sendMessage(ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you with his shotgun!");
						player.getKiller().sendMessage( "You have killed " + player.getName() + " with your shotgun!");
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					} else {
						player.sendMessage(ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you!");
						player.getKiller().sendMessage( "You have killed " + player.getName());
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					}
				}
				if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
					if (Main.game.getBlueTeam().getFlagHolder()==player) {
						Main.game.getBlueTeam().setFlagHolder(null);
						Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
					}
				}
			} else {
				if (player.getKiller() instanceof Player) {

					if (player.getKiller().getItemInHand().getType() == Material.ARROW) {
						player.sendMessage(ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you with his gun!");
						player.getKiller().sendMessage( "You have killed " + ChatColor.BLUE + player.getName() + ChatColor.RED + " with your gun!");
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					} else if (player.getKiller().getItemInHand().getType() == Material.IRON_SWORD || player.getKiller().getItemInHand().getType() == Material.DIAMOND_SWORD) {
						player.sendMessage(ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you with his sword!");
						player.getKiller().sendMessage( "You have killed " + ChatColor.BLUE + player.getName() + ChatColor.RED + " with your sword!");
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					} else if (player.getKiller().getItemInHand().getType() == Material.BONE) {
						player.sendMessage(ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you with his shotgun!");
						player.getKiller().sendMessage( "You have killed " + ChatColor.BLUE + player.getName() + ChatColor.RED + " with your shotgun!");
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					} else {
						player.sendMessage(ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you!");
						player.getKiller().sendMessage( "You have killed " + ChatColor.BLUE + player.getName());
						//Main.game.kills.put(player.getKiller(), Main.game.kills.get(player.getKiller()) + 1);
						//player.getKiller().setExp(Main.game.kills.get(player.getKiller()));
					}

				}
				if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
					if (Main.game.getRedTeam().getFlagHolder() ==player) {
						Main.game.getRedTeam().setFlagHolder(null);
						Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
					}
				}
			}
		}
	}

}
