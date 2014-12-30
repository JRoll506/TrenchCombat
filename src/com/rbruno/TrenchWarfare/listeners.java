package com.rbruno.TrenchWarfare;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

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
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (Main.gameState == 0) event.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void signCreation(SignChangeEvent event) {
		for (int i = 0; i < 4; i++) {
			event.setLine(i, event.getLine(i).replace("&", "§"));
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.isDead()) return;
		if (player.getLocation().getBlockY() < 0) {
			player.teleport(spawn);
			player.setFallDistance(0F);
		}
		Location location = player.getLocation();
		if (Main.gameState == 0) {
			return;
		}
		if (Main.game.blueTeam.contains(player)) {
			if (location.getBlockX() <= Main.trenchConfig.fortRed) player.teleport(new Location(player.getWorld(), Main.trenchConfig.fortRed + 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		} else {
			if (location.getBlockX() >= Main.trenchConfig.fortBlue) player.teleport(new Location(player.getWorld(), Main.trenchConfig.fortBlue - 1, location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (Main.game.blueTeam.contains(player) && location.getBlockX() == Main.trenchConfig.redFlagX && location.getBlockY() == Main.trenchConfig.redFlagY && location.getBlockZ() == Main.trenchConfig.redFlagZ) {
			if (!(Main.game.redFlagHolder == null)) return;
			if (!(Main.game.blueFlagHolder == null)) return;
			Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag", true);
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 14) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Main.game.redFlagHolder = player;
		}
		if (Main.game.redTeam.contains(player) && location.getBlockX() == Main.trenchConfig.blueFlagX && location.getBlockY() == Main.trenchConfig.blueFlagY && location.getBlockZ() == Main.trenchConfig.blueFlagZ) {
			if (!(Main.game.blueFlagHolder == null)) return;
			if (!(Main.game.redFlagHolder == null)) return;
			Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has taken the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag", true);
			player.getInventory().clear();
			ItemStack[] kit = { new ItemStack(Material.WOOL, 1, (byte) 11) };
			kit[0].setAmount(64);
			for (int i = 0; i < 9; i++) {
				player.getInventory().addItem(kit);
			}
			Main.game.blueFlagHolder = player;
		}

		if (Main.game.redFlagHolder == player && location.getBlockX() == Main.trenchConfig.blueFlagX && location.getBlockY() == Main.trenchConfig.blueFlagY && location.getBlockZ() == Main.trenchConfig.blueFlagZ) {
			Main.game.redFlagHolder = null;
			Main.game.blueScore = Main.game.blueScore + 1;
			Main.game.score[0].setScore(Main.game.blueScore);
			if (Main.game.blueScore == 3) {
				Main.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.BLUE + "Blue", true);
				Main.endGame(true);
			} else {
				Main.broadcast(ChatColor.BLUE + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.BLUE + "Blue", true);
				Main.game.giveItems(player);

			}
		}

		if (Main.game.blueFlagHolder == player && location.getBlockX() == Main.trenchConfig.redFlagX && location.getBlockY() == Main.trenchConfig.redFlagY && location.getBlockZ() == Main.trenchConfig.redFlagZ) {
			Main.game.blueFlagHolder = null;
			Main.game.redScore = Main.game.redScore + 1;
			Main.game.score[1].setScore(Main.game.redScore);
			if (Main.game.redScore == 3) {
				Main.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag and won the game for " + ChatColor.RED + "Red", true);
				Main.endGame(true);
			} else {
				Main.broadcast(ChatColor.RED + player.getName() + ChatColor.WHITE + " has captured the flag for " + ChatColor.RED + "Red", true);
				Main.game.giveItems(player);

			}
		}
		if (Main.gameState == 0 || player.isOp()) return;

		if (location.getBlockX() >= Main.trenchConfig.maxX) {
			player.teleport(new Location(location.getWorld(), Main.trenchConfig.maxX - 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockX() <= Main.trenchConfig.minX) {
			player.teleport(new Location(location.getWorld(), Main.trenchConfig.minX + 1, location.getBlockY(), location.getZ(), location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() <= Main.trenchConfig.minZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Main.trenchConfig.minZ + 1, location.getYaw(), location.getPitch()));
		}
		if (location.getBlockZ() >= Main.trenchConfig.maxZ) {
			player.teleport(new Location(location.getWorld(), location.getX(), location.getBlockY(), Main.trenchConfig.maxZ - 1, location.getYaw(), location.getPitch()));
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {
		if (Main.gameState == 0) {
			event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
			event.getPlayer().getInventory().setArmorContents(null);
			event.getPlayer().getInventory().clear();
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
		} else {
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
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		if (!event.getPlayer().isOp()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage("");
		Player player = (Player) event.getEntity();
		event.getDrops().clear();
		if (Main.gameState == 1) {
			if (Main.game.redTeam.contains(player)) {
				if (player.getKiller() instanceof Player) {
					if (player.getKiller().getItemInHand().getType() == Material.ARROW) {
						Main.messagePlayer(player, ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you with his gun!");
						Main.messagePlayer(player.getKiller(), "You have killed " + player.getName() + " with your gun!");
					} else if (player.getKiller().getItemInHand().getType() == Material.IRON_SWORD || player.getKiller().getItemInHand().getType() == Material.DIAMOND_SWORD) {
						Main.messagePlayer(player, ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you with his sword!");
						Main.messagePlayer(player.getKiller(), "You have killed " + player.getName() + " with your sword!");
					} else if (player.getKiller().getItemInHand().getType() == Material.BONE) {
						Main.messagePlayer(player, ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you with his shotgun!");
						Main.messagePlayer(player.getKiller(), "You have killed " + player.getName() + " with your shotgun!");
					} else {
						Main.messagePlayer(player, ChatColor.BLUE + player.getKiller().getName() + ChatColor.RED + " has killed you!");
						Main.messagePlayer(player.getKiller(), "You have killed " + player.getName());
					}
				}
				if (!(Main.game.blueFlagHolder == null)) {
					if (Main.game.blueFlagHolder.equals(player)) {
						Main.game.blueFlagHolder = null;
						Main.broadcast(ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag", true);
					}
				}
			} else {
				if (player.getKiller() instanceof Player) {

					if (player.getKiller().getItemInHand().getType() == Material.ARROW) {
						Main.messagePlayer(player, ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you with his gun!");
						Main.messagePlayer(player.getKiller(), "You have killed " + ChatColor.BLUE + player.getName() + ChatColor.RED + " with your gun!");
					} else if (player.getKiller().getItemInHand().getType() == Material.IRON_SWORD || player.getKiller().getItemInHand().getType() == Material.DIAMOND_SWORD) {
						Main.messagePlayer(player, ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you with his sword!");
						Main.messagePlayer(player.getKiller(), "You have killed " + ChatColor.BLUE + player.getName() + ChatColor.RED + " with your sword!");
					} else if (player.getKiller().getItemInHand().getType() == Material.BONE) {
						Main.messagePlayer(player, ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you with his shotgun!");
						Main.messagePlayer(player.getKiller(), "You have killed " + ChatColor.BLUE + player.getName() + ChatColor.RED + " with your shotgun!");
					} else {
						Main.messagePlayer(player, ChatColor.RED + player.getKiller().getName() + ChatColor.RED + " has killed you!");
						Main.messagePlayer(player.getKiller(), "You have killed " + ChatColor.BLUE + player.getName());
					}

				}
				if (!(Main.game.redFlagHolder == null)) {
					if (Main.game.redFlagHolder.equals(player)) {
						Main.game.redFlagHolder = null;
						Main.broadcast(ChatColor.BLUE + player.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag", true);
					}
				}
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
			player.getInventory().clear();
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (Main.gameState == 0) return;
		if (event.getDamager() instanceof Player) {
			event.getDamager();
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(event.getDamager())) {
				// attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue

				if (Main.game.redTeam.contains(event.getEntity())) {
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
			if (Main.gameState == 0) {
				event.setCancelled(true);
			} else if (Main.game.redTeam.contains(damager)) {
				// attacker is red
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					event.setCancelled(true);
				} else {
					// Victim is blue
					return;
				}
			} else {
				// attacker is blue
				if (Main.game.redTeam.contains(event.getEntity())) {
					// victim is red
					return;

				} else {
					// Victim is blue
					event.setCancelled(true);
				}
			}
		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					for (int i = 0; i < Main.classes.length; i++) {
						if (sign.getLine(1).equals(Main.classes[i])) {
							if (Main.classMap.containsKey(player)) {
								Main.classMap.remove(player);
							}
							Main.classMap.put(player, sign.getLine(1));
							Main.messagePlayer(player, "You have picked the " + sign.getLine(1) + " class");
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
				}
			}
		}
		if (Main.gameState == 0) return;
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
					Main.messagePlayer(player, "Reloading cannon...");
				} else {
					if (!(Main.game.cooldown.contains(player))) {
						Main.game.cooldown.add(player);
						if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
							fireCannon(player, true);
						} else {
							fireCannon(player, false);
						}
						Main.messagePlayer(player, "Reloading cannon...");
					}
				}
			} else {
				if (Main.game.redTeam.contains(player) && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationRed && location.getPitch() == -90 && player.isOnGround()) {
					player.setVelocity(player.getLocation().getDirection().multiply(1.2));
				}
				if (Main.game.blueTeam.contains(player) && player.getLocation().getBlockX() == Main.trenchConfig.trenchLocationBlue && location.getPitch() == -90 && player.isOnGround()) {
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
			scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
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
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
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
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			@Override
			public void run() {
				if (tnt.getLocation().getX() <= Main.trenchConfig.fortRed || tnt.getLocation().getX() >= Main.trenchConfig.fortBlue) return;
				if (Main.game.redTeam.contains(player)) {
					// red
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(5, 5, 5);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (Main.game.blueTeam.contains(victum)) {
								if (!(Main.game.redFlagHolder == null)) {
									if (Main.game.redFlagHolder.equals(victum)) {
										Main.game.redFlagHolder = null;
										Main.broadcast(ChatColor.BLUE + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.RED + "Red " + ChatColor.WHITE + "flag", true);
									}
								}
								Main.messagePlayer(player, "You have killed " + ChatColor.BLUE + victum.getName() + ChatColor.RED + " with your cannon!");
								Main.messagePlayer(victum, player.getName() + " has killed you with his cannon!");

								victum.damage(20F);
							}

						}

					}
				} else {
					// blue
					tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
					List<Entity> players;
					players = tnt.getNearbyEntities(5, 5, 5);
					for (int i = 0; i < players.toArray().length; i++) {
						if (players.get(i) instanceof Player) {
							Player victum = (Player) players.get(i);
							if (Main.game.redTeam.contains(victum)) {
								if (!(Main.game.blueFlagHolder == null)) {
									if (Main.game.blueFlagHolder.equals(victum)) {
										Main.game.blueFlagHolder = null;
										Main.broadcast(ChatColor.RED + victum.getDisplayName() + ChatColor.WHITE + " has droped the " + ChatColor.BLUE + "Blue " + ChatColor.WHITE + "flag", true);
									}
								}
								Main.messagePlayer(player, "You have killed " + victum.getName() + " with your cannon!");
								Main.messagePlayer(victum, ChatColor.BLUE + player.getName() + ChatColor.RED + " has killed you with his cannon!");
								victum.damage(20F);
							}
						}

					}
				}

			}
		}, tnt.getFuseTicks());
		scheduler.scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
			public void run() {
				Main.game.cooldown.remove(player);
			}
		}, 60L);
	}
}
