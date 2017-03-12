package com.elytradev.fondue.module.resizehelper;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ModuleResizeHelper extends ModuleClient {

	@Override
	public String getName() {
		return "Resize Helper";
	}

	@Override
	public String getDescription() {
		return "Shows the resolution of the window for a few seconds after resizing it.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	private int ticksSinceResolutionChange = 900;
	private int lastWidth = -1;
	private int lastHeight = -1;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) {
		if (e.phase == Phase.END) {
			ticksSinceResolutionChange++;
		}
	}
	
	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent.Post e) {
		if (e.getType() == ElementType.ALL) {
			renderResizeText(e.getResolution().getScaledWidth(), e.getResolution().getScaledHeight());
		}
	}
	
	@SubscribeEvent
	public void onRenderGui(DrawScreenEvent.Post e) {
		renderResizeText(e.getGui().width, e.getGui().height);
	}

	private void renderResizeText(int guiWidth, int guiHeight) {
		int width = Minecraft.getMinecraft().displayWidth;
		int height = Minecraft.getMinecraft().displayHeight;
		if (width != lastWidth || height != lastHeight) {
			if (lastWidth != -1) {
				ticksSinceResolutionChange = 0;
			}
			lastWidth = width;
			lastHeight = height;
		}
		if (ticksSinceResolutionChange < 40) { 
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(width+"x"+height+"px", 2, 2, 0xFFFFFF55);
			Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(guiWidth+"x"+guiHeight+"gp", 2, 14, 0xFFFFFF55);
		}
	}

}
