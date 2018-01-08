package com.rbruno.trench.listener.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.lobby.Lobby;

public class SignInteractEvent extends EngineListner {

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					if (Lobby.getClassManager().getClassMap().containsKey(player)) {
						Lobby.getClassManager().getClassMap().remove(player);
					}
					Lobby.getClassManager().getClassMap().put(player, sign.getLine(1));
					if (Lobby.getClassManager().getEngineClass(sign.getLine(1)) == null) {
						player.sendMessage(ChatColor.RED + "Class not Found!");
						return;
					}
					player.sendMessage("You have picked the " + sign.getLine(1) + " class");
					for (String line : Lobby.getClassManager().getEngineClass(sign.getLine(1)).getDescription()) {
						player.sendMessage(line.replace("&", "§"));
					}
				} else if (sign.getLine(0).contains("[Team]")) {
					if (Lobby.getPlugin().getTeamQueue().containsKey(player)) Lobby.getPlugin().getTeamQueue().remove(player);
					player.sendMessage("You have queued for the " + sign.getLine(1) + ChatColor.WHITE + " team");
					if (sign.getLine(1).contains("Red")) Lobby.getPlugin().getTeamQueue().put(player, ColorTeam.RED);
					if (sign.getLine(1).contains("Blue")) Lobby.getPlugin().getTeamQueue().put(player, ColorTeam.BLUE);
				} else if (sign.getLine(0).contains("[Parkour]")) {
					if (!(Lobby.parkour.contains(player))) {
						Lobby.broadcast(player.getName() + " knows how to use the spacebar!");
						Lobby.parkour.add(player);

					}
				} else if (sign.getLine(2).contains("[Right Click]")) {
					player.teleport(new Location(Lobby.getPlugin().getServer().getWorld("Trenchwarfare"), 602.5, 69, 41.5, 180, 0));
				} else if (sign.getLine(0).contains("[Trampoline]")) {
					player.teleport(new Location(Lobby.getPlugin().getServer().getWorld("Trenchwarfare"), 616.5, 70, 0.5, 180, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10));
				}
			}
		}
	}
}
