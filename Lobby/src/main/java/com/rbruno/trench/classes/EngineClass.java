package com.rbruno.trench.classes;

import org.bukkit.event.Listener;

public class EngineClass implements Listener {
	private String name;
	private String[] description = {};

	public EngineClass(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}
	
	public String[] getDescription() {
		return description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}
}
