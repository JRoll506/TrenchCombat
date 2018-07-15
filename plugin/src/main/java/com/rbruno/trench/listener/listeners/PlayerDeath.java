package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.EngineTeam;
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
				break;
			}
			
			EngineTeam team = main.game.getColorTeam(player);
			
			if (killer != null && !player.equals(killer)) {
				Bukkit.broadcastMessage(main.game.getColorTeam(killer).color + killer.getName() + ChatColor.WHITE + " has killed " + team.color + player.getName() + ChatColor.WHITE + " with their " + weapon + "!");
				// TODO Stats
			} else {
				Bukkit.broadcastMessage(team.color + player.getName() + ChatColor.WHITE + " has died!");
			}

			for (EngineTeam targetTeam : main.game.teams) {
				if (team.flagHolder == player) {
					Bukkit.getServer().broadcastMessage(team.color + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + targetTeam.color + targetTeam.team.getName() + ChatColor.WHITE + " flag");
				}
			}
		}
	}
}
