package com.rbruno.engine.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.rbruno.engine.listener.EngineListner;

public class InventoryClick  extends EngineListner implements Listener{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!(player.isOp())) {
			event.setCancelled(true);
		}
	}
}
