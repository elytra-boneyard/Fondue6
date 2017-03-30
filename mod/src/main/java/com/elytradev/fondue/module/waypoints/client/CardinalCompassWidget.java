package com.elytradev.fondue.module.waypoints.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class CardinalCompassWidget extends CompassWidget {

	private final float yaw;
	private final String text;
	private final boolean small;
	
	public CardinalCompassWidget(float yaw, String text, boolean small) {
		this.yaw = yaw;
		this.text = text;
		this.small = small;
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
		if (small) {
			GlStateManager.translate(0, 2, 0);
			GlStateManager.scale(0.5f, 0.5f, 1);
		}
		Minecraft.getMinecraft().fontRenderer.drawString(text, 0, 0, -1);
	}

	@Override
	public int getWidth() {
		return Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / (small ? 2 : 1);
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
