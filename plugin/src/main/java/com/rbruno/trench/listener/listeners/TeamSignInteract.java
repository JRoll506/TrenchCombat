package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;

public class TeamSignInteract extends EngineListner {

	public TeamSignInteract(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Team]")) {
					// Team Sign
					main.teamQueue.put(player, sign.getLine(0));
					player.sendMessage("You have queued for the " + sign.getLine(1) + ChatColor.WHITE + " team");
				}
			}
		}
	}
}
