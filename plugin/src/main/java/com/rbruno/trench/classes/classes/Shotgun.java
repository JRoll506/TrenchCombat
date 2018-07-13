package com.rbruno.trench.classes.classes;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import com.rbruno.trench.Main;
import com.rbruno.trench.classes.EngineClass;

public class Shotgun extends EngineClass {

	private ArrayList<Player> cooldown = new ArrayList<Player>();

	public Shotgun(Main main) {
		super(new ItemStack[] { new ItemStack(Material.IRON_SWORD), new ItemStack(Material.BONE), main.getGrenade() },
				"shotgun", main);
		String[] description = { "&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", "&f&lShotgun Class",
				"&7Pump action shotgun.", "&66 bullets per round.", "", "&f&lShotgun",
				"&eRight-Click &7to use Shotgun.", "&7Equipped with &aIron Sword &7and &a3 grenades&7.",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=" };
		this.setDescription(description);
		main.getServer().getPluginManager().registerEvents(this, main);
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (event.getMaterial().name() == "BONE") {
			if (!(cooldown.contains(player))) {
				fireArrow(player);
			}
			event.setCancelled(true);
		}
	}

	public void fireArrow(final Player player) {
		cooldown.add(player);
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
			arrow2.setVelocity(new Vector(direction.getX() + (Math.random() - 0.5) / spray,
					direction.getY() + (Math.random() - 0.5) / spray, direction.getZ() + (Math.random() - 0.5) / spray)
							.normalize().multiply(speed));
		}
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				cooldown.remove(player);
			}
		}, 20L);
	}

}
