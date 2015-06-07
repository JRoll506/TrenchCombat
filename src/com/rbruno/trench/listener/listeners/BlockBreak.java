package com.rbruno.trench.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.rbruno.trench.listener.EngineListner;

public class BlockBreak extends EngineListner implements Listener {
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}
}
