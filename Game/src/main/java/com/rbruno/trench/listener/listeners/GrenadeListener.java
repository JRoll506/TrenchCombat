package com.rbruno.trench.listener.listeners;

import java.util.EnumMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.rbruno.trench.Game;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;

public class GrenadeListener extends EngineListner implements Listener {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();

		if (event.getMaterial().name() == "SLIME_BALL") {
			// Removes 1 grenade
			ItemStack item = player.getItemInHand();
			player.getInventory().remove(item);
			item.setAmount(item.getAmount() - 1);
			player.getInventory().addItem(item);

			final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SLIME_BALL, 1));
			// Throws grenade
			grenade.setVelocity(player.getLocation().getDirection().multiply(1.2));
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Game.getPlugin(), new Runnable() {
				@Override
				public void run() {
					grenade.remove();
					grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);

					if (grenade.getLocation().getX() <= Game.trenchConfig.fortRed || grenade.getLocation().getX() >= Game.trenchConfig.fortBlue) return;

					List<Entity> players;
					players = grenade.getNearbyEntities(2, 2, 2);
					for (Entity victum : players) {
						if (victum instanceof Player) {
							Player victumPlayer = (Player) victum;
							if (Game.game.getColorTeam(player) == ColorTeam.RED) {
								if (Game.game.getColorTeam(victumPlayer) == ColorTeam.BLUE) {
									double inital = victumPlayer.getHealth();
									victum.setLastDamageCause(new EntityDamageEvent(player, DamageCause.ENTITY_EXPLOSION, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, inital)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(-0.0)))));
									victumPlayer.setHealth(0);
								}

							} else {
								if (Game.game.getColorTeam(victumPlayer) == ColorTeam.RED) {
									double inital = victumPlayer.getHealth();
									victum.setLastDamageCause(new EntityDamageEvent(player, DamageCause.ENTITY_EXPLOSION, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, inital)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(-0.0)))));
									victumPlayer.setHealth(0);
								}
							}
						}
					}

				}
			}, 40L);
		}
	}

}
