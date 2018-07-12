package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class ClassSignInteract extends EngineListner {

	public ClassSignInteract(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					// Class Sign
					if (main.getClassManager().getClassMap().containsKey(player)) {
						main.getClassManager().getClassMap().remove(player);
					}
					main.getClassManager().getClassMap().put(player, sign.getLine(1));
					if (main.getClassManager().getEngineClass(sign.getLine(1)) == null) {
						player.sendMessage(ChatColor.RED + "Class not Found!");
						return;
					}
					player.sendMessage("You have picked the " + sign.getLine(1) + " class");
					for (String line : main.getClassManager().getEngineClass(sign.getLine(1)).getDescription()) {
						player.sendMessage(line.replace("&", "§"));
					}
					if (main.getGameState() == GameState.IN_GAME)
						main.getGame().giveItems(player);
				}
			}
		}
	}

}
