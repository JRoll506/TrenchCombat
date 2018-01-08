package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.rbruno.trench.listener.EngineListner;

public class InventoryClick  extends EngineListner implements Listener{
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(((Player) event.getWhoClicked()).isOp())) {
			event.setCancelled(true);
		}
	}
}
