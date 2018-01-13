package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.lobby.Lobby;
import com.rbruno.trench.timer.LobbyState;

public class PlayerQuit extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		if (Lobby.getPlugin().getLobbyState() == LobbyState.MOVING) {
			event.setQuitMessage(null);
		}
		if (Lobby.getPlugin().getLobbyState() != LobbyState.COUNTING) return;
		if (Bukkit.getOnlinePlayers().size() - 1 < Lobby.trenchConfig.getMinPlayer()) {
			Lobby.getPlugin().setGameState(LobbyState.WAITING);
			Lobby.broadcast(ChatColor.YELLOW + "Wating for more players.");
		}
	}

}
