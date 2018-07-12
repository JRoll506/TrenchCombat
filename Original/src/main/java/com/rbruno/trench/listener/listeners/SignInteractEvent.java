package com.rbruno.trench.listener.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.rbruno.trench.Main;
import com.rbruno.trench.game.ColorTeam;
import com.rbruno.trench.listener.EngineListner;
import com.rbruno.trench.timer.GameState;

public class SignInteractEvent extends EngineListner {
	
	public SignInteractEvent(Main main) {
		super(main);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		final Player player = (Player) event.getPlayer();
		if (!(event.getClickedBlock() == null)) {
			if (event.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).contains("[Class]")) {
					if (main.getClassManager().getClassMap().containsKey(player)) {
						main.getClassManager().getClassMap().remove(player);
					}
					main.getClassManager().getClassMap().put(player, sign.getLine(1));
					if (main.getClassManager().getEngineClass(sign.getLine(1)) == null) {
						player.sendMessage(ChatColor.RED + "Class not Found!");
						return;
					}
					player.sendMessage("You have picked the " + sign.getLine(1) + " class");
					for (String line : main.getClassManager().getEngineClass(sign.getLine(1)).getDescription()) {
						player.sendMessage(line.replace("&", "§"));
					}
					if (main.getGameState() == GameState.IN_GAME)
						main.getGame().giveItems(player);
				} else if (sign.getLine(0).contains("[Team]")) {
					if (main.getTeamQueue().containsKey(player)) main.getTeamQueue().remove(player);
					player.sendMessage("You have queued for the " + sign.getLine(1) + ChatColor.WHITE + " team");
					if (sign.getLine(1).contains("Red")) main.getTeamQueue().put(player, ColorTeam.RED);
					if (sign.getLine(1).contains("Blue")) main.getTeamQueue().put(player, ColorTeam.BLUE);
				} else if (sign.getLine(0).contains("[Parkour]")) {
					if (!(main.parkour.contains(player))) {
						Bukkit.getServer().broadcastMessage(player.getName() + " knows how to use the spacebar!");
						main.parkour.add(player);

					}
				} else if (sign.getLine(2).contains("[Right Click]")) {
					player.teleport(new Location(Bukkit.getWorld("Trenchwarfare"), 602.5, 69, 41.5, 180, 0));
				} else if (sign.getLine(0).contains("[Trampoline]")) {
					player.teleport(new Location(Bukkit.getWorld("Trenchwarfare"), 616.5, 70, 0.5, 180, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 10));
				}
			}
		}
	}
}
