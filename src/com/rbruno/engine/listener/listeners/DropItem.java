package com.rbruno.engine.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.rbruno.engine.listener.EngineListner;

public class DropItem extends EngineListner implements Listener {
	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if (!(event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

}
