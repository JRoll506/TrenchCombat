package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;

public class PlayerQuit extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		if (Game.game.getColorTeam(player) == ColorTeam.RED) {
			if (!(Game.game.getBlueTeam().getFlagHolder() == null)) {
				if (Game.game.getBlueTeam().getFlagHolder() == player) {
					Game.game.getBlueTeam().setFlagHolder(null);
					Game.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the "
							+ ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
				}
			}
			Game.game.getRedTeam().removePlayer(player);
		} else {
			if (!(Game.game.getRedTeam().getFlagHolder() == null)) {
				if (Game.game.getRedTeam().getFlagHolder() == player) {
					Game.game.getRedTeam().setFlagHolder(null);
					Game.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the "
							+ ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
				}
			}
			Game.game.getBlueTeam().removePlayer(player);
		}
	}

}
