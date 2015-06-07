package com.rbruno.engine.classes.classes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.rbruno.engine.Main;
import com.rbruno.engine.classes.EngineClass;

public class Shotgun extends EngineClass {

	public Shotgun() {
		super(new ItemStack[] { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BONE), Main.getPlugin().getGrenade() }, "shotgun");
		String[] description = { 
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 
				"&f&lShotgun Class",
				"&7Pump action shotgun.",
				"&66 bullets per round.",
				"",
				"&f&lShotgun",
				"&eRight-Click &7to use Shotgun.",
				"&7Equipped with &aIron Sword &7and &a3 grenades&7.",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
			};
		this.setDescription(description);
		Main.getPlugin().getServer().getPluginManager().registerEvents(this, Main.getPlugin());
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (event.getMaterial().name() == "BONE") {
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
		// you can tune the following value for different spray. Higher number
		// means less spray.
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

}
