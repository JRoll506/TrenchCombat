package com.rbruno.trench.classes.classes;

import com.rbruno.trench.classes.EngineClass;
import com.rbruno.trench.lobby.Lobby;

public class Gunner extends EngineClass {

	public Gunner() {
		super("Gunner");
		String[] description = { 
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 
				"&f&lGunner Class",
				"&7Fully-automatic machine gun.",
				"",
				"&f&lMachine Gun",
				"&eRight-Click &7to use gun.",
				"&7Equipped with &aIron Sword &7and &a3 grenades",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
			};
		this.setDescription(description);
		Lobby.getPlugin().getServer().getPluginManager().registerEvents(this, Lobby.getPlugin());
	}
}
