package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.rbruno.trench.listener.EngineListner;

public class BlockPlace extends EngineListner implements Listener {
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}
}
