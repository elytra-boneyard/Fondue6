package com.elytradev.fondue.module.waypoints.client;

import com.elytradev.fondue.module.waypoints.WaypointShape;
import com.elytradev.fondue.module.waypoints.WaypointStyle;
import com.elytradev.fruitphone.client.render.Rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class WaypointCompassWidget extends BlockCompassWidget {

	private static final ResourceLocation WAYPOINT = new ResourceLocation("fondue", "textures/gui/waypoint.png");
	
	public int color;
	public WaypointShape shape;
	public WaypointStyle style;
	
	public WaypointCompassWidget(int color, BlockPos location, WaypointShape shape, WaypointStyle style) {
		super(location);
		this.color = color;
		this.shape = shape;
		this.style = style;
	}

	@Override
	public void render() {
		Rendering.color3(color);
		Minecraft.getMinecraft().getTextureManager().bindTexture(WAYPOINT);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, shape.ordinal()*8, style.ordinal()*8, 8, 8, 72, 16);
	}

	@Override
	public int getWidth() {
		return 8;
	}
	
	@Override
	public double getFalloffSize() {
		return 16;
	}
	
	@Override
	public double getFalloffStart() {
		return 256;
	}

}
