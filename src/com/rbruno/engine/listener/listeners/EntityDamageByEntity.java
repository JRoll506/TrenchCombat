package com.rbruno.engine.listener.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class EntityDamageByEntity extends EngineListner implements Listener{
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (Main.getGameState() == GameState.LOBBY) return;
		if (event.getDamager() instanceof Player) {
			event.getDamager();
			if (Main.getGameState() == GameState.LOBBY) {
				event.setCancelled(true);
			} else if (Main.game.getColorTeam((Player) event.getDamager()) == ColorTeam.RED) {
				// attacker is red
				if (Main.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue

				if (Main.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
					// victim is red
					return;
				} else {
					// Victim is blue
					event.setCancelled(true);
				}
			}
		} else if (event.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) event.getDamager();
			Player damager = (Player) arrow.getShooter();
			if (Main.getGameState() == GameState.LOBBY) {
				event.setCancelled(true);
			} else if (Main.game.getColorTeam(damager) == ColorTeam.RED) {
				// attacker is red
				if (Main.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue
				if (Main.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
					// victim is red
					return;

				} else {
					// Victim is blue
					event.setCancelled(true);
				}
			}
		}
	}
}
