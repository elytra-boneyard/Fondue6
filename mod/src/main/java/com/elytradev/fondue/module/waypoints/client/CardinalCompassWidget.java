package com.elytradev.fondue.module.waypoints.client;

import net.minecraft.client.Minecraft;

public class CardinalCompassWidget extends CompassWidget {

	private final float yaw;
	private final String text;
	
	public CardinalCompassWidget(float yaw, String text) {
		this.yaw = yaw;
		this.text = text;
	}
	
	@Override
	public float getYaw() {
		return yaw;
	}

	@Override
	public double getDistanceSq() {
		return Double.POSITIVE_INFINITY;
	}
	
	@Override
	public double getDistance() {
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public void render() {
		Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, -1);
	}

	@Override
	public int getWidth() {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
	}
	
	@Override
	public double getFalloffSize() {
		return -1;
	}
	
	@Override
	public double getFalloffStart() {
		return -1;
	}

}
