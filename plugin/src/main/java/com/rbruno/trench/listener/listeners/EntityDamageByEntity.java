package com.rbruno.trench.listener.listeners;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class EntityDamageByEntity extends EngineListner implements Listener {

	public EntityDamageByEntity(Main main) {
		super(main);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME))
			return;
		if (event.getDamager() instanceof Player) {
			if (main.game.getColorTeam((Player) event.getDamager()).getName().equals("Red")) {
				// attacker is red
				if (main.game.getColorTeam((Player) event.getEntity()).getName().equals("Red")) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue

				if (main.game.getColorTeam((Player) event.getEntity()).getName().equals("Red")) {
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
			if (main.game.getColorTeam(damager).getName().equals("Red")) {
				// attacker is red
				if (main.game.getColorTeam((Player) event.getEntity()).getName().equals("Red")) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue
				if (main.game.getColorTeam((Player) event.getEntity()).getName().equals("Red")) {
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
