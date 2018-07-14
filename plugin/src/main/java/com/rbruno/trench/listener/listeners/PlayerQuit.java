package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
			if (Bukkit.getOnlinePlayers().size() - 1 < main.getConfig().getInt("minPlayer")) {
				main.setGameState(GameState.WAITING);
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Wating for more players.");
			}
			return;
		}
		if (main.game.getColorTeam(event.getPlayer()).getName().equals("Red")) {
			if (main.game.blueTeam.flagHolder == event.getPlayer()) {
				main.game.blueTeam.flagHolder = null;
				Bukkit.getServer().broadcastMessage(ChatColor.RED + event.getPlayer().getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
			}
		} else {
			if (main.game.redTeam.flagHolder == event.getPlayer()) {
				main.game.redTeam.flagHolder = null;
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + event.getPlayer().getDisplayName() + ChatColor.WHITE + " has dropped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
			}
		}
		main.game.teamMap.remove(event.getPlayer());
	}

}
