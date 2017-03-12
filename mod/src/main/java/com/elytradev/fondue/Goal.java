package com.elytradev.fondue;

public enum Goal {
	WORK_ON_SMALL_SERVERS("Make the modpack work better on servers with small populations (5-24 players)"),
	WORK_IN_SINGLEPLAYER("Make the modpack work better in singleplayer"),
	ENCOURAGE_MAP_LONGEVITY("Encourage maps lasting for a month or more"),
	BE_UNIQUE("Make the modpack more unique"),
	IMPROVE_VANILLA("Improve upon vanilla mechanics"),
	ENCOURAGE_EXPLORATION("Encourage players to explore"),
	ENCOURAGE_SOCIALIZATION("Encourage players to socialize"),
	ENCOURAGE_INFRASTRUCTURE("Encourage players to build infrastructure"),
	REDUCE_FRICTION("Make it easier to get started");
	public final String description;
	private Goal(String description) {
		this.description = description;
	}
}
