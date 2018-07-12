package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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
			EntityDamageEvent damageCause = player.getLastDamageCause();
			Player killer = (Player) player.getKiller();
			ChatColor color = getChatColor(player);
			ChatColor killerColor = getChatColor(killer);

			switch (damageCause.getCause()) {
			case BLOCK_EXPLOSION:
				killer = (Player) damageCause.getEntity();
				killerColor = getChatColor(killer);
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your cannon!");
				player.sendMessage(killerColor + killer.getName() + ChatColor.WHITE + " has killed you with their cannon!");
				break;
			case ENTITY_EXPLOSION:
				killer = (Player) damageCause.getEntity();
				killerColor = getChatColor(killer);
				player.sendMessage(killerColor + killer.getName() + ChatColor.WHITE + " has killed you with their grenade!");
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your grenade!");
				break;
			default:
				String weapon = "";
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
				player.sendMessage(killerColor + killer.getName() + ChatColor.WHITE + " has killed you with their " + weapon + "!");
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your " + weapon + "!");
				break;
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
			if (!(killer == null)) main.getGame().kills.put(killer, main.getGame().kills.get(killer) + 1);

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
