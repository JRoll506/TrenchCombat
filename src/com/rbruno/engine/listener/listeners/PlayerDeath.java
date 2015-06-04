package com.rbruno.engine.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rbruno.engine.Main;
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
			if (!(damageCause.getEntity() instanceof Player)) return;
			Player killer = player.getKiller();
			ChatColor color = getChatColor(player);
			ChatColor killerColor  = getChatColor(killer);

			switch (damageCause.getCause()) {
			case BLOCK_EXPLOSION:
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your cannon!");
				player.sendMessage(killerColor + killer.getName() + ChatColor.WHITE + " has killed you with his cannon!");
				break;
			case ENTITY_ATTACK:
				player.sendMessage(killerColor + player.getKiller().getName() + ChatColor.WHITE + " has killed you with their sword!");
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your sword!");
				break;
			case ENTITY_EXPLOSION:
				player.sendMessage(killerColor + player.getKiller().getName() + ChatColor.WHITE + " has killed you with their grenade!");
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your grenade!");
				break;
			case PROJECTILE:
				String weapon = "";
				switch (killer.getInventory().getItemInHand().getType()) {
				case ARROW:
					weapon = " gun";
					break;
				case BONE:
					weapon = " shotgun";
					break;
				default:
					break;
				}	
				player.sendMessage(killerColor + player.getKiller().getName() + ChatColor.WHITE + " has killed you with their" + weapon + "!");
				System.out.println(color + player.getName() + ":" + weapon);
				killer.sendMessage("You have killed " + color + player.getName() + ChatColor.WHITE + " with your" + weapon + "!");
				break;
			default:
				break;
			}
			Main.getGame().kills.put(player.getKiller(), Main.getGame().kills.get(player.getKiller()) + 1);
			if (!(Main.getGame().getTeam(player).getFlagHolder() == null)) {
				if (Main.getGame().getTeam(player).getFlagHolder() == player) {
					Main.getGame().getTeam(player).setFlagHolder(null);
					Main.broadcast(color + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + killerColor + (killerColor == ChatColor.RED? " red": " blue") + ChatColor.WHITE + "flag");
				}
			}
		}
	}
	
	private ChatColor getChatColor(Player player){
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
