package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerDeath extends EngineListner implements Listener {
	
	public PlayerDeath(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerDeath2(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
		if (main.getGameState() == GameState.IN_GAME) {
			Player killer = (Player) player.getKiller();

			String weapon = "hand";
			switch (player.getLastDamageCause().getCause()) {
			case BLOCK_EXPLOSION:
				killer = (Player) player.getLastDamageCause().getEntity();
				weapon = "cannon";
				break;
			case ENTITY_EXPLOSION:
				killer = (Player) player.getLastDamageCause().getEntity();
				weapon = "grenade";
				break;
			default:
				if (killer == null) break;
				switch (killer.getInventory().getItemInHand().getType()) {
				case ARROW:
					weapon = "gun";
					break;
				case BONE:
					weapon = "shotgun";
					break;
				case DIAMOND_SWORD:
				case IRON_SWORD:
					weapon = "sword";
					break;
				default:
					weapon = "hand";
				}
				break;
			}
			if (!player.equals(killer) && killer != null) {
				Bukkit.broadcastMessage(getChatColor(killer) + killer.getName() + ChatColor.WHITE + " has killed " + getChatColor(player) + player.getName() + ChatColor.WHITE + " with their " + weapon + "!");
				// TODO Stats
			} else {
				Bukkit.broadcastMessage(getChatColor(player) + player.getName() + ChatColor.WHITE + " has died!");
			}

			if (main.game.getColorTeam(player) == ColorTeam.RED) {
				if (!(main.game.getBlueTeam().getFlagHolder() == null)) {
					if (main.game.getBlueTeam().getFlagHolder() == player) {
						main.game.getBlueTeam().setFlagHolder(null);
						Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
					}
				}
			} else {
				if (!(main.game.getRedTeam().getFlagHolder() == null)) {
					if (main.game.getRedTeam().getFlagHolder() == player) {
						main.game.getRedTeam().setFlagHolder(null);
						Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
					}
				}
			}
		}
	}

	private ChatColor getChatColor(Player player) {
		ChatColor color;
		switch (main.getGame().getColorTeam(player)) {
		case BLUE:
			color = ChatColor.BLUE;
			break;
		case RED:
			color = ChatColor.RED;
			break;
		default:
			color = ChatColor.YELLOW;
			break;

		}
		return color;
	}
}
