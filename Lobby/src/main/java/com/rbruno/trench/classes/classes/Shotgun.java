package com.rbruno.trench.classes.classes;

import com.rbruno.trench.classes.EngineClass;

public class Shotgun extends EngineClass {

	public Shotgun() {
		super("shotgun");
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
	}

}
