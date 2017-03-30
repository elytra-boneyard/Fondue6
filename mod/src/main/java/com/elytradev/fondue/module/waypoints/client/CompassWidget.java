package com.elytradev.fondue.module.waypoints.client;

public abstract class CompassWidget {

	public static final CardinalCompassWidget NORTH = new CardinalCompassWidget(180, "N");
	public static final CardinalCompassWidget EAST = new CardinalCompassWidget(-90, "E");
	public static final CardinalCompassWidget SOUTH = new CardinalCompassWidget(0, "S");
	public static final CardinalCompassWidget WEST = new CardinalCompassWidget(90, "W");
	
	public abstract float getYaw();
	public abstract double getDistanceSq();
	
	public abstract void render();
	public abstract int getWidth();
	
	public double getDistance() {
		return Math.sqrt(getDistanceSq());
	}
	
	public abstract double getFalloffStart();
	public abstract double getFalloffSize();
	
}
