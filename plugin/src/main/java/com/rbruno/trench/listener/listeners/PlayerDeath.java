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
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
		if (main.getGameState() == GameState.IN_GAME) {
			Player killer = (Player) player.getKiller();
			
			EngineTeam team = main.game.getColorTeam(player);
			
			if (killer != null && !player.equals(killer)) {
				Bukkit.broadcastMessage(main.game.getColorTeam(killer).color + killer.getName() + ChatColor.WHITE + " has killed " + team.color + player.getName());
				// TODO Stats
			} else {
				Bukkit.broadcastMessage(team.color + player.getName() + ChatColor.WHITE + " has died!");
			}

			for (EngineTeam targetTeam : main.game.teams) {
				if (targetTeam.flagHolder == player) {
					targetTeam.flagHolder = null;
					Bukkit.getServer().broadcastMessage(team.color + player.getName() + ChatColor.WHITE + " has dropped the " + targetTeam.color + targetTeam.team.getName() + ChatColor.WHITE + " flag");
				}
			}
		}
	}
}
