package com.rbruno.trench.listener.listeners;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMap;
import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class CannonListener extends EngineListner implements Listener {
	
	public ArrayList<Player> cooldown = new ArrayList<Player>();

	public CannonListener(Main main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME)) return;
		final Player player = (Player) event.getPlayer();
		Location location = player.getLocation();
		Location blockStandingOn = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		if (event.getMaterial().name() == "IRON_SWORD" || event.getMaterial().name() == "DIAMOND_SWORD") {
			if (blockStandingOn.getBlock().getType() == Material.SPONGE) {
				// Player Fired Cannon
				if (!(cooldown.contains(player))) {
					cooldown.add(player);
					if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
						// Lobed tnt
						fireCannon(player, true);
					} else {
						// Threw tnt
						fireCannon(player, false);
					}
					player.sendMessage("Reloading cannon...");
				}
			} else {
				// Trench Hoping
				if (main.game.getColorTeam(player) == ColorTeam.RED && player.getLocation().getBlockX() == main.trenchConfig.trenchLocationRed && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (main.game.getColorTeam(player) == ColorTeam.BLUE && player.getLocation().getBlockX() == main.trenchConfig.trenchLocationBlue && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}

			}
		}
	}

	public void fireCannon(final Player player, final boolean rightClick) {
		final TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
		if (rightClick) {
			tnt.setVelocity(player.getLocation().getDirection().multiply(1.5));
		} else {
			tnt.setVelocity(player.getLocation().getDirection().multiply(3));
		}
		tnt.setYield(0);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(main, new Runnable() {
			@Override
			public void run() {
				if (tnt.getLocation().getX() <= main.trenchConfig.fortRed || tnt.getLocation().getX() >= main.trenchConfig.fortBlue) return;
				if (main.game.getColorTeam(player) == ColorTeam.RED) {
					// red
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(4, 4, 4);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (main.game.getColorTeam(victum) == ColorTeam.BLUE) {
								double inital = victum.getHealth();
								victum.setLastDamageCause(new EntityDamageEvent(player, DamageCause.BLOCK_EXPLOSION, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, inital)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(-0.0)))));
								victum.damage(20F);
							}

						}

					}
				} else {
					// blue
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(4, 4, 4);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (main.game.getColorTeam(victum) == ColorTeam.RED) {
								double inital = victum.getHealth();
								victum.setLastDamageCause(new EntityDamageEvent(player, DamageCause.BLOCK_EXPLOSION, new EnumMap<DamageModifier, Double>(ImmutableMap.of(DamageModifier.BASE, inital)), new EnumMap<DamageModifier, Function<? super Double, Double>>(ImmutableMap.of(DamageModifier.BASE, Functions.constant(-0.0)))));
								victum.damage(20F);
							}
						}

					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				cooldown.remove(player);
			}
		}, 60L);
	}
}
