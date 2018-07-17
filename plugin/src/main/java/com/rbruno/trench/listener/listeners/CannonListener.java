package com.rbruno.trench.listener.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.rbruno.trench.Main;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class CannonListener extends EngineListner implements Listener {

	public ArrayList<Player> cooldown = new ArrayList<Player>();

	public CannonListener(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if (!(main.getGameState() == GameState.IN_GAME))
			return;
		Player player = event.getPlayer();
		if (event.getMaterial().name() == "IRON_SWORD" || event.getMaterial().name() == "DIAMOND_SWORD") {
			if (player.getLocation().subtract(0, 1, 0).getBlock().getType() == Material.SPONGE) {
				// Player Fired Cannon
				if (cooldown.contains(player))
					return;
				cooldown.add(player);
				
				if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
					// Lobed TNT
					fireCannon(player, true);
				} else {
					// Throw TNT
					fireCannon(player, false);
				}
				player.sendMessage("Reloading cannon...");
			}

		}
	}

	public void fireCannon(final Player player, final boolean rightClick) {
		final TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);

		tnt.setVelocity(player.getLocation().getDirection().multiply(rightClick ? 1.5 : 3));
		tnt.setYield(0);

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			@Override
			public void run() {
				if (tnt.getLocation().getX() <= main.trenchConfig.fortRed || tnt.getLocation().getX() >= main.trenchConfig.fortBlue)
					return;
				tnt.getWorld().createExplosion(tnt.getLocation().getX(), tnt.getLocation().getY(), tnt.getLocation().getZ(), 5F, false, false);
			}
		}, tnt.getFuseTicks());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				cooldown.remove(player);
			}
		}, 60L);
	}
}
