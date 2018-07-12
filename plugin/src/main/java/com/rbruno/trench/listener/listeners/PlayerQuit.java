package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
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
	
	public PlayerQuit(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME)) {
			if (Bukkit.getOnlinePlayers().size() - 1 < main.trenchConfig.getMinPlayer()) {
				main.setGameState(GameState.WAITING);
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Wating for more players.");
			}
			return;
		}
		Player player = (Player) event.getPlayer();
		if (main.game.getColorTeam(player) == ColorTeam.RED) {
			if (!(main.game.getBlueTeam().getFlagHolder() == null)) {
				if (main.game.getBlueTeam().getFlagHolder() == player) {
					main.game.getBlueTeam().setFlagHolder(null);
					Bukkit.getServer().broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
				}
			}
			main.game.getRedTeam().removePlayer(player);
		} else {
			if (!(main.game.getRedTeam().getFlagHolder() == null)) {
				if (main.game.getRedTeam().getFlagHolder() == player) {
					main.game.getRedTeam().setFlagHolder(null);
					Bukkit.getServer().broadcastMessage(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
				}
			}
			main.game.getBlueTeam().removePlayer(player);
		}
	}

}
