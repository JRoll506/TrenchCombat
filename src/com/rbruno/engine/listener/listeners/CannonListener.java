package com.rbruno.engine.listener.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitScheduler;

import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class CannonListener extends EngineListner implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (Main.getGameState() == GameState.LOBBY) return;
		final Player player = (Player) event.getPlayer();
		Location location = player.getLocation();
		Location blockStandingOn = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		if (event.getMaterial().name() == "IRON_SWORD" || event.getMaterial().name() == "DIAMOND_SWORD") {
			if (blockStandingOn.getBlock().getType() == Material.SPONGE) {
				// Player Fired Cannon
				if (!(Main.game.cooldownCannon.contains(player))) {
					Main.game.cooldownCannon.add(player);
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
				if (Main.game.getColorTeam(player) == ColorTeam.RED && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationRed && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (Main.game.getColorTeam(player) == ColorTeam.BLUE && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationBlue && location.getPitch() == -90 && player.isOnGround()) {
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
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (tnt.getLocation().getX() <= Main.trenchConfig.fortRed || tnt.getLocation().getX() >= Main.trenchConfig.fortBlue) return;
				if (Main.game.getColorTeam(player) == ColorTeam.RED) {
					// red
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(4, 4, 4);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (Main.game.getColorTeam(victum) == ColorTeam.BLUE) {
								if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
									if (Main.game.getRedTeam().getFlagHolder().equals(victum)) {
										Main.game.getRedTeam().setFlagHolder(null);
										Main.broadcast(ChatColor.BLUE + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
									}
								}
								player.sendMessage("You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.WHITE + " with your cannon!");
								victum.sendMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " has killed you with their cannon!");
								Main.game.kills.put(player, Main.game.kills.get(player) + 1);
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
							if (Main.game.getColorTeam(victum) == ColorTeam.RED) {
								if (!(Main.game.getBlueTeam().getFlagHolder() == null)) {
									if (Main.game.getBlueTeam().getFlagHolder() == victum) {
										Main.game.getBlueTeam().setFlagHolder(null);
										Main.broadcast(ChatColor.RED + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag");
									}
								}
								player.sendMessage("You have killed " + ChatColor.RED + victum.getName() + ChatColor.WHITE + " with your cannon!");
								victum.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has killed you with his cannon!");
								Main.game.kills.put(player, Main.game.kills.get(player) + 1);
								victum.damage(20F);
							}
						}

					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				Main.game.cooldownCannon.remove(player);
			}
		}, 60L);
	}
}
