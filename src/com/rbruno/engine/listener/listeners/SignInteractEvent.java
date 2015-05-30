package com.rbruno.engine.listener.listeners;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.engine.Main;
import com.rbruno.engine.listener.EngineListner;

public class SignInteractEvent extends EngineListner {

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
						Main.broadcast(player.getName() + " knows how to use the spacebar!");
						Main.parkour.add(player);

					}
				} else if (sign.getLine(2).contains("[Right Click]")) {
					player.teleport(new Location(Main.getPlugin().getServer().getWorld("Trenchwarfare"), 602.5, 69, 41.5, 180, 0));
				} else if (sign.getLine(0).contains("[Trampoline]")) {
					player.teleport(new Location(Main.getPlugin().getServer().getWorld("Trenchwarfare"), 616.5, 70, 0.5, 180, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10));
				}
			}
		}
	}
}
