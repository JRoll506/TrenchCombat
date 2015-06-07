package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class PlayerQuit extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if (Main.getGameState() == GameState.LOBBY) return;
		Player player = (Player) event.getPlayer();
		if (Main.game.getColorTeam(player) == ColorTeam.RED) {
			if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
				if (Main.game.getBlueTeam().getFlagHolder() == player) {
					Main.game.getBlueTeam().setFlagHolder(null);
					Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
				}
			}
			Main.game.getRedTeam().removePlayer(player);
		} else {
			if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
				if (Main.game.getRedTeam().getFlagHolder() == player) {
					Main.game.getRedTeam().setFlagHolder(null);
					Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
				}
			}
			Main.game.getBlueTeam().removePlayer(player);
		}
	}

}
