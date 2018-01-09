package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;

public class PlayerDeath extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerDeath2(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
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
			player.sendMessage(
					killerColor + killer.getName() + ChatColor.WHITE + " has killed you with their grenade!");
			killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your grenade!");
			break;
		default:
			String weapon = "";
			if (killer == null)
				break;
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
			player.sendMessage(
					killerColor + killer.getName() + ChatColor.WHITE + " has killed you with their " + weapon + "!");
			killer.sendMessage(
					"You have killed " + color + player.getName() + ChatColor.WHITE + " with your " + weapon + "!");
			break;
		}
		if (Game.game.getColorTeam(player) == ColorTeam.RED) {
			if (!(Game.game.getBlueTeam().getFlagHolder() == null)) {
				if (Game.game.getBlueTeam().getFlagHolder() == player) {
					Game.game.getBlueTeam().setFlagHolder(null);
					Game.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the "
							+ ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
				}
			}
		} else {
			if (!(Game.game.getRedTeam().getFlagHolder() == null)) {
				if (Game.game.getRedTeam().getFlagHolder() == player) {
					Game.game.getRedTeam().setFlagHolder(null);
					Game.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the "
							+ ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
				}
			}
		}
		if (!(killer == null))
			Game.getGame().kills.put(killer, (Game.getGame().kills.get(killer) != null ? Game.getGame().kills.get(killer)+ 1 : 1));

	}

	private ChatColor getChatColor(Player player) {
		ChatColor color;
		switch (Game.getGame().getColorTeam(player)) {
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
