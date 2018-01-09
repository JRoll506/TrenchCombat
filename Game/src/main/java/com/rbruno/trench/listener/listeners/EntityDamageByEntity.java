package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.rbruno.trench.Game;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;

public class EntityDamageByEntity extends EngineListner implements Listener {
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (Game.game.getColorTeam((Player) event.getDamager()) == ColorTeam.RED) {
				// attacker is red
				if (Game.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue

				if (Game.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
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
			if (Game.game.getColorTeam(damager) == ColorTeam.RED) {
				// attacker is red
				if (Game.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue
				if (Game.game.getColorTeam((Player) event.getEntity()) == ColorTeam.RED) {
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
