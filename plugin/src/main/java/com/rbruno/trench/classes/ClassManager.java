package com.rbruno.trench.classes;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.rbruno.trench.Main;
import com.rbruno.trench.classes.classes.Gunner;
import com.rbruno.trench.classes.classes.Scout;
import com.rbruno.trench.classes.classes.Shotgun;

public class ClassManager {

	
	public EngineClass[] classes;

	public HashMap<Player, EngineClass> classMap = new HashMap<Player, EngineClass>();

	public ClassManager(Main main) {
		this.classes = new EngineClass[3];
		
		this.classes[0] = new Gunner(main);
		this.classes[1] = new Shotgun(main);
		this.classes[2] = new Scout(main);
	}

	public EngineClass getClass(String name) {
		for (EngineClass engineClass : classes) {
			if (engineClass.getName().equalsIgnoreCase(name)) return engineClass;
		}
		return null;
	}
}
