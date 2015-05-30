package com.rbruno.engine.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

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
