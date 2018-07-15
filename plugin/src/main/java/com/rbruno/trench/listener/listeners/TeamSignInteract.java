package com.rbruno.trench.listener.listeners;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;

public class TeamSignInteract extends EngineListner {

	public TeamSignInteract(Main main) {
		super(main);
	}

	/*@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Team]")) {
					// Team Sign
					/*if (main.getTeamQueue().containsKey(player))
						main.getTeamQueue().remove(player);
					player.sendMessage("You have queued for the " + sign.getLine(1) + ChatColor.WHITE + " team");
					if (sign.getLine(1).contains("Red"))
						main.getTeamQueue().put(player, ColorTeam.RED);
					if (sign.getLine(1).contains("Blue"))
						main.getTeamQueue().put(player, ColorTeam.BLUE);
				}
			}
		}
	}*/

}
