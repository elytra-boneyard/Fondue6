package com.elytradev.fondue.module.nodebug;

import java.util.List;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModuleNoDebug extends ModuleClient {

	private List<String> left = Lists.newArrayList();
	private List<String> center = Lists.newArrayList();
	private List<String> right = Lists.newArrayList();
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onRenderText(RenderGameOverlayEvent.Text e) {
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			if (Minecraft.getMinecraft().player.isCreative()) {
				Minecraft.getMinecraft().gameSettings.reducedDebugInfo = false;
				e.getLeft().add(2, "Fondue "+Loader.instance().getIndexedModList().get("fondue").getDisplayVersion().replace("@VERSION@", "<dev>")+" ("+Fondue.inst.modules.size()+" modules)");
				return;
			}
			Minecraft.getMinecraft().gameSettings.reducedDebugInfo = true;
			e.getLeft().clear();
			e.getRight().clear();
			
			left.clear();
			center.clear();
			right.clear();
			
			long total = Runtime.getRuntime().totalMemory();
			long free = Runtime.getRuntime().freeMemory();
			long max = Runtime.getRuntime().maxMemory();
			long used = total-free;
			
			String divider = "§m";
			
			left.add("Minecraft §?"+Minecraft.getMinecraft().getVersion());
			left.add("Fondue §?"+Loader.instance().getIndexedModList().get("fondue").getDisplayVersion().replace("@VERSION@", "<dev>"));
			left.add(divider);
			left.add("Altitude: §?"+((int)Minecraft.getMinecraft().player.posY));
			
			String mem = Loader.isModLoaded("foamfix") ? "memory" : "memes";
			
			center.add("§?"+(used/1024/1024)+"MiB§r/§?"+(total/1024/1024)+"MiB §r"+mem);
			center.add("§?"+(max/1024/1024)+"MiB §rmax "+mem);
			center.add(divider);
			center.add("§?"+Minecraft.getDebugFPS()+" §rfps");
			
			right.add("Forge §?"+ForgeVersion.getVersion());
			right.add(Loader.instance().getMCPVersionString().replace(" ", " §?"));
			right.add(divider);
			right.add("§?"+Loader.instance().getModList().size()+" §rmods");
			
			drawSide(left, e.getResolution().getScaledWidth(), 0);
			drawSide(center, e.getResolution().getScaledWidth(), 2);
			drawSide(right, e.getResolution().getScaledWidth(), 1);
		}
	}
	
	private void drawSide(List<String> content, int screenWidth, int div) {
		int y = 5;
		int boxWidth = 0;
		int boxHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT*content.size()+4;
		for (String s : content) {
			int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(s)+10;
			if (width > boxWidth) {
				boxWidth = width;
			}
		}
		int boxX = div == 0 ? 1 : (screenWidth-boxWidth-1)/div;
		Gui.drawRect(boxX, y-4, boxX+boxWidth, y+boxHeight, 0x22FFFFFF);
		int idx = 0;
		for (String s : content) {
			int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(s)+2;
			int height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
			
			int x = boxX+(boxWidth-width-4)/2;
			
			if (s.equals("§m")) {
				Gui.drawRect(boxX+4, y+4, boxX+boxWidth-4, y+5, 0xFFFFFFFF);
			} else {
				Minecraft.getMinecraft().fontRenderer.drawString(s.replace("§?", "§6"), x+4, y+1, 0xFFFFFFFF);
			}
			
			y+=height;
			idx++;
		}
	}
	
}
