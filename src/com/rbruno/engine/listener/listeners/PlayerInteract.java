package com.rbruno.engine.listener.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.rbruno.TrenchWarfare.ParticleEffect;
import com.rbruno.engine.Main;
import com.rbruno.engine.game.ColorTeam;
import com.rbruno.engine.listener.EngineListner;
import com.rbruno.engine.timer.GameState;

public class PlayerInteract extends EngineListner implements Listener{

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			/*if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					for (int i = 0; i < Main.classes.length; i++) {
						if (sign.getLine(1).equals(Main.classes[i])) {
							if (Main.classMap.containsKey(player)) {
								Main.classMap.remove(player);
							}
							Main.classMap.put(player, sign.getLine(1));
							player.sendMessage("You have picked the " + sign.getLine(1) + " class");
							if (sign.getLine(1).equalsIgnoreCase("gunner")) {
								player.sendMessage("&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".replace("&", "§"));
								player.sendMessage("&f&lGunner Class".replace("&", "§"));
								player.sendMessage("&7Fully-automatic machine gun.".replace("&", "§"));
								player.sendMessage("");
								player.sendMessage("&f&lMachine Gun".replace("&", "§"));
								player.sendMessage("&eRight-Click &7to use gun.".replace("&", "§"));
								player.sendMessage("&7Equipped with &aIron Sword &7and &aLeather Tunic".replace("&", "§"));
								player.sendMessage("&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".replace("&", "§"));
							}
							if (sign.getLine(1).equalsIgnoreCase("shotgun")) {
								player.sendMessage("&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".replace("&", "§"));
								player.sendMessage("&f&lShotgun Class".replace("&", "§"));
								player.sendMessage("&7Pump action shotgun.".replace("&", "§"));
								player.sendMessage("&66 bullets per round.".replace("&", "§"));
								player.sendMessage("");
								player.sendMessage("&f&lShotgun".replace("&", "§"));
								player.sendMessage("&eRight-Click &7to use Shotgun.".replace("&", "§"));
								player.sendMessage("&7Equipped with &aIron Sword &7and &aLeather Tunic.".replace("&", "§"));

								player.sendMessage("&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".replace("&", "§"));
							}
							if (sign.getLine(1).equalsIgnoreCase("scout")) {
								player.sendMessage("&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".replace("&", "§"));
								player.sendMessage("&f&lScout Class".replace("&", "§"));
								player.sendMessage("&7Equipped with &aDiamond Sword &7and &aLeather Tunic.".replace("&", "§"));
								player.sendMessage("");
								player.sendMessage("&f&lSpeed".replace("&", "§"));
								player.sendMessage("&7Permanent Speed 2.".replace("&", "§"));
								player.sendMessage("&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=".replace("&", "§"));
							}

						}
					}
				} else if (sign.getLine(0).contains("[Parkour]")) {
					if (!(Main.parkour.contains(player))) {
						Main.broadcast(player.getName() + " knows how to use the spacebar!", true);
						Main.parkour.add(player);

					}
				} else if (sign.getLine(2).contains("[Right Click]")) {
					player.teleport(new Location(Main.getInstance().getServer().getWorld("Trenchwarfare"), 602.5, 69, 41.5, 180, 0));
				} else if (sign.getLine(0).contains("[Trampoline]")) {
					player.teleport(new Location(Main.getInstance().getServer().getWorld("Trenchwarfare"), 616.5, 70, 0.5, 180, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10));
				}
			}
		}*/
		if (Main.getGameState() == GameState.LOBBY) return;
		Location location = player.getLocation();
		Location d = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
		if (event.getMaterial().name() == "IRON_SWORD" || event.getMaterial().name() == "DIAMOND_SWORD") {
			if (d.getBlock().getType() == Material.SPONGE) {
				if (Main.game.cooldown.toArray().length == 0) {
					Main.game.cooldown.add(player);
					if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
						fireCannon(player, true);
					} else {
						fireCannon(player, false);
					}
					player.sendMessage("Reloading cannon...");
				} else {
					if (!(Main.game.cooldown.contains(player))) {
						Main.game.cooldown.add(player);
						if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
							fireCannon(player, true);
						} else {
							fireCannon(player, false);
						}
						player.sendMessage("Reloading cannon...");
					}
				}
			} else {
				if (Main.game.getColorTeam(player) == ColorTeam.RED && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationRed && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (Main.game.getColorTeam(player) == ColorTeam.BLUE && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationBlue && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}

			}
		} else if (event.getMaterial().name() == "ARROW") {
			event.setCancelled(true);
			if (Main.game.cooldownGunner.toArray().length == 0) {
				Main.game.cooldownGunner.add(player);
				player.launchProjectile(Arrow.class);
			} else {
				if (!(Main.game.cooldownGunner.contains(player))) {
					Main.game.cooldownGunner.add(player);
					player.launchProjectile(Arrow.class);
				}
			}
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				public void run() {
					Main.game.cooldownGunner.remove(player);
				}
			}, 1L);

		} else if (event.getMaterial().name() == "BONE") {
			if (Main.game.cooldownShotgun.toArray().length == 0) {
				fireArrow(player);
			} else {
				if (!(Main.game.cooldownShotgun.contains(player))) {
					fireArrow(player);
				}
			}
			event.setCancelled(true);

		} else if (event.getMaterial().name() == "SULPHUR") {
			player.getInventory().remove(Material.SULPHUR);
			final Item smoke = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SULPHUR, 1));
			smoke.setVelocity(player.getLocation().getDirection().multiply(1.2));
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.runTaskTimer(Main.getPlugin(), new Runnable() {
				int i=0;
				@Override
				public void run() {
					i++;
					if(i==100) smoke.remove();
					if(i<100){
						ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 10);
						ParticleEffect.EXPLOSION_HUGE.display(smoke.getVelocity(), 0, smoke.getLocation(), 10);
					}
				}
			}, 40L, 5L);
		}
			
		}else if (event.getMaterial().name() == "SLIME_BALL") {
			player.getInventory().remove(Material.SLIME_BALL);
			final Item grenade = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.SLIME_BALL, 1));
			grenade.setVelocity(player.getLocation().getDirection().multiply(1.2));
			final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					grenade.remove();
					grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);	
					if (grenade.getLocation().getX() <= Main.trenchConfig.fortRed || grenade.getLocation().getX() >= Main.trenchConfig.fortBlue) return;
					if (Main.game.getColorTeam(player) == ColorTeam.RED) {
						// red
						grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);
						List<Entity> players;
						players = grenade.getNearbyEntities(2, 2, 2);
						for (int i = 0; i < players.toArray().length; i++) {
							if (players.get(i) instanceof Player) {
								Player victum = (Player) players.get(i);
								if (Main.game.getColorTeam(victum) == ColorTeam.BLUE) {
									if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
										if (Main.game.getRedTeam().getFlagHolder() == victum) {
											Main.game.getRedTeam().setFlagHolder(null);
											Main.broadcast(ChatColor.BLUE + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
										}
									}
									player.sendMessage("You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.RED + " with your grenade!");
									victum.sendMessage(player.getName() + " has killed you with his cannon!");
									Main.game.kills.put(player, Main.game.kills.get(player) + 1);
									player.setExp(Main.game.kills.get(player));
									victum.damage(20F);
								}

							}

						}
					} else {
						// blue
						grenade.getWorld().createExplosion(grenade.getLocation().getX(), grenade.getLocation().getY(), grenade.getLocation().getZ(), 5F, false, false);
						List<Entity> players;
						players = grenade.getNearbyEntities(2, 2, 2);
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
									player.sendMessage("You have killed " + victum.getName() + " with your cannon!");
									victum.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " has killed you with his grenade!");
									Main.game.kills.put(player, Main.game.kills.get(player) + 1);
									player.setExp(Main.game.kills.get(player));
									victum.damage(20F);
								}
							}

						}
					}
					
				}
			}, 40L);
		}
			
		}

	public void fireArrow(final Player player) {
		Main.game.cooldownShotgun.add(player);
		Arrow arrow = (Arrow) player.launchProjectile(Arrow.class);
		arrow.setVelocity(arrow.getVelocity().multiply(2));
		Vector velocity = arrow.getVelocity();
		double speed = velocity.length();
		Vector direction = new Vector(velocity.getX() / speed, velocity.getY() / speed, velocity.getZ() / speed);
		// you can tune the following value for different spray. Higher number means less spray.
		double spray = 4.5D;

		int arrowCount = 5;
		for (int i = 0; i < arrowCount; i++) {
			Arrow arrow2 = player.launchProjectile(Arrow.class);
			arrow2.setVelocity(arrow.getVelocity().multiply(2));
			arrow2.setVelocity(new Vector(direction.getX() + (Math.random() - 0.5) / spray, direction.getY() + (Math.random() - 0.5) / spray, direction.getZ() + (Math.random() - 0.5) / spray).normalize().multiply(speed));
		}
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				Main.game.cooldownShotgun.remove(player);

			}
		}, 20L);
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
							if (Main.game.getColorTeam(victum) == ColorTeam.RED) {
								if (!(Main.game.getRedTeam().getFlagHolder() == null)) {
									if (Main.game.getRedTeam().getFlagHolder().equals(victum)) {
										Main.game.getRedTeam().setFlagHolder(null);
										Main.broadcast(ChatColor.BLUE + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag");
									}
								}
								player.sendMessage("You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.RED + " with your cannon!");
								victum.sendMessage(player.getName() + " has killed you with his cannon!");
								//Main.game.kills.put(player, Main.game.kills.get(player) + 1);
								//player.setExp(Main.game.kills.get(player));
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
								player.sendMessage("You have killed " + victum.getName() + " with your cannon!");
								victum.sendMessage(ChatColor.BLUE + player.getName() + ChatColor.RED + " has killed you with his cannon!");
								//Main.game.kills.put(player, Main.game.kills.get(player) + 1);
								//player.setExp(Main.game.kills.get(player));
								victum.damage(20F);
							}
						}

					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				Main.game.cooldown.remove(player);
			}
		}, 60L);
	}
}
