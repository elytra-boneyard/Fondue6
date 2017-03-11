package com.elytradev.fondue.module.rezsickness;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class PotionResurrectionSickness extends Potion {

	private static final ResourceLocation TEX = new ResourceLocation("fondue", "textures/effect/resurrection_sickness.png");
	
	protected PotionResurrectionSickness() {
		super(true, 0x220011);
	}
	
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void performEffect(EntityLivingBase elb, int amp) {
		if (elb instanceof EntityPlayer) {
			// half the speed of normal Hunger
			((EntityPlayer)elb).addExhaustion((amp + 1) / 400f);
		}
	}
	
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		mc.renderEngine.bindTexture(TEX);
		GlStateManager.color(1, 1, 1, alpha);
		Gui.drawModalRectWithCustomSizedTexture(x+3, y+3, 0, 0, 18, 18, 18, 18);
	}
	
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		mc.renderEngine.bindTexture(TEX);
		Gui.drawModalRectWithCustomSizedTexture(x+6, y+7, 0, 0, 18, 18, 18, 18);
	}
	

}
