package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class SignInteractEvent extends EngineListner {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (Game.getPlugin().getGameState() != GameState.PLAYING) return;
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					if (Game.getClassManager().getClassMap().containsKey(player)) {
						Game.getClassManager().getClassMap().remove(player);
					}
					Game.getClassManager().getClassMap().put(player, sign.getLine(1));
					if (Game.getClassManager().getEngineClass(sign.getLine(1)) == null) {
						player.sendMessage(ChatColor.RED + "Class not Found!");
						return;
					}
					player.sendMessage("You have picked the " + sign.getLine(1) + " class");
					for (String line : Game.getClassManager().getEngineClass(sign.getLine(1)).getDescription()) {
						player.sendMessage(line.replace("&", "§"));
					}
					Game.getGame().giveItems(player);
				}
			}
		}
	}
}
