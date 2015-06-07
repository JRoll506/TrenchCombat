package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import com.rbruno.trench.listener.EngineListner;

public class PlayerPickUpItem extends EngineListner {

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		if (!(event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

}
