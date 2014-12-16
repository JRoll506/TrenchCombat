package com.rbruno.TrenchWarfare;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

public class listeners implements Listener {

	public static ArrayList<Player> cooldown = new ArrayList<Player>();
	
	Location spawn = Main.trenchConfig.getSpawn();
	Location redSpawn = Main.trenchConfig.getRed();
	Location blueSpawn = Main.trenchConfig.getBlue();

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!(player.isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
		if (!(event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
		if (!(event.getPlayer().isOp())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (Main.gameState == 0) {
			ItemStack[] kit = { new ItemStack(Material.COOKED_BEEF) };
			kit[0].setAmount(64);
			event.getPlayer().getInventory().clear();
			event.getPlayer().getInventory().addItem(kit);
			event.getPlayer().teleport(spawn);
			Main.messagePlayer(event.getPlayer(), "The game will begin shortly!");
		} else if (Main.gameState == 1) {
			Main.addPlayer(event.getPlayer());
		}
	}
	public void onPlayerQuitEvent(PlayerQuitEvent event) {
		Player player = (Player) event.getPlayer();
		if (Main.game.redTeam.contains(player)) {
			Main.game.redTeam.remove(player);
		} else{
			Main.game.blueTeam.remove(player);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		if (Main.gameState == 1) {
			if (Main.game.redTeam.contains(player)) {
				Main.game.blueScore = Main.game.blueScore + 10;
				Main.game.redScore = Main.game.redScore - 5;
			} else {
				Main.game.redScore = Main.game.redScore + 10;
				Main.game.blueScore = Main.game.blueScore - 5;
			}
		}
	}

	@EventHandler
	public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
		Player player = (Player) event.getPlayer();
		if (Main.gameState == 1) {
			if (Main.game.redTeam.contains(player)) {
				event.setRespawnLocation(redSpawn);
				Main.game.giveItems(player);
			} else {
				event.setRespawnLocation(blueSpawn);
				Main.game.giveItems(player);
			}
		} else {
			event.setRespawnLocation(spawn);
			ItemStack[] kit = { new ItemStack(Material.COOKED_BEEF) };
			kit[0].setAmount(64);
			player.getInventory().clear();
			player.getInventory().addItem(kit);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(event.getDamager())) {
				//attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					event.setCancelled(true);
				} else {
					//Victim is blue
					return;
				}
			} else {
				//attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					return;
				} else {
					//Victim is blue
					event.setCancelled(true);
				}
			}
		} else if (event.getDamager() instanceof Arrow) {
			Arrow a = (Arrow) event.getDamager();
			Player damager = (Player) a.getShooter();
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(damager)) {
				//attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					event.setCancelled(true);
				} else {
					//Victim is blue
					return;
				}
			} else {
				//attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					//victim is red
					return;
				} else {
					//Victim is blue
					event.setCancelled(true);
				}
			}
		}

	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		Location location = player.getLocation();
		Location d = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		if (event.getMaterial().name() == "IRON_SWORD") {
			if (d.getBlock().getType() == Material.SPONGE) {
				if (Main.game.cooldown.toArray().length == 0) {
					Main.game.cooldown.add(player);
					fireCannon(player);
				} else {
					if (!(Main.game.cooldown.contains(player))) {
						Main.game.cooldown.add(player);
						fireCannon(player);
					} else {

						Main.messagePlayer(player, "cannon is reloading!");
					}
				}
			}
		} else if (event.getMaterial().name() == "ARROW") {
			event.setCancelled(true);
			player.launchProjectile(Arrow.class);

		}
	}

	public void fireCannon(final Player player) {
		final TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
		tnt.setVelocity(player.getLocation().getDirection().multiply(3));
		tnt.setYield(0);
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {

				if (Main.game.redTeam.contains(player)) {
					//red
					if (tnt.getLocation().getBlockX() < 593) {
						Main.messagePlayer(player, "To avoid your own team being put in danger you must shoot beyond your trench");
					} else {
						tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5, false, false);
					}
				} else {
					//blue
					if (tnt.getLocation().getBlockX() > 610) {
						Main.messagePlayer(player, "To avoid your own team being put in danger you must shoot beyond your trench");
					} else {
						tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5, false, false);
					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			public void run() {
				Main.game.cooldown.remove(player);
			}
		}, 20L);
	}
}
