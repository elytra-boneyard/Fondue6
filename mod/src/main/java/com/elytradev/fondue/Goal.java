package com.elytradev.fondue;

public enum Goal {
	WORK_ON_SMALL_SERVERS("Makes the modpack work better on servers with small populations (5-24 players)"),
	WORK_IN_SINGLEPLAYER("Makes the modpack work better in singleplayer"),
	ENCOURAGE_MAP_LONGEVITY("Encourages maps lasting for a month or more"),
	BE_UNIQUE("Makes the modpack more unique"),
	IMPROVE_VANILLA("Improves upon vanilla mechanics"),
	ENCOURAGE_EXPLORATION("Encourages players to explore"),
	ENCOURAGE_SOCIALIZATION("Encourages players to socialize"),
	ENCOURAGE_INFRASTRUCTURE("Encourages players to build infrastructure"),
	REDUCE_FRICTION("Makes it easier to get started");
	public final String description;
	private Goal(String description) {
		this.description = description;
	}
}
