package com.rbruno.trench.classes.classes;

import com.rbruno.trench.classes.EngineClass;

public class Scout extends EngineClass {

	public Scout() {
		super("Scout");
		String[] description = { 
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=", 
				"&f&lScout Class",
				"&7Speedy one that hurts from close.",
				"",
				"&f&lSpeed",
				"&7Permanent Speed &a2&7.",
				"&7Equipped with &aDiamond Sword&7, &a3 grenades &7and &aa smoke&7.",
				"&2=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="
			};
		this.setDescription(description);
	}
}
