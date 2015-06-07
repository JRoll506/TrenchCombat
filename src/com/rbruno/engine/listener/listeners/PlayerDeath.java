package com.rbruno.engine.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class PlayerDeath extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerDeath2(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
		if (Main.getGameState() == GameState.IN_GAME) {
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
			if (Main.game.getColorTeam(player) == ColorTeam.RED) {
				if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
					if (Main.game.getBlueTeam().getFlagHolder() == player) {
						Main.game.getBlueTeam().setFlagHolder(null);
						Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
					}
				}
			} else {
				if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
					if (Main.game.getRedTeam().getFlagHolder() == player) {
						Main.game.getRedTeam().setFlagHolder(null);
						Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
					}
				}
			}
			if (!(killer == null)) Main.getGame().kills.put(killer, Main.getGame().kills.get(killer) + 1);

		}
	}

	private ChatColor getChatColor(Player player) {
		ChatColor color;
		switch (Main.getGame().getColorTeam(player)) {
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
