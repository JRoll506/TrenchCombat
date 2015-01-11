package com.rbruno.engine.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.rbruno.engine.listener.EngineListner;

public class BlockPlace extends EngineListner implements Listener {
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}
}
