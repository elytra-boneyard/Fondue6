/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 Aesen 'unascribed' Vismea
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.elytradev.fondue.module.pale;

import com.elytradev.concrete.reflect.accessor.Accessor;
import com.elytradev.concrete.reflect.accessor.Accessors;
import com.elytradev.concrete.reflect.invoker.Invoker;
import com.elytradev.concrete.reflect.invoker.Invokers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class PotionPale extends Potion {

	private static final ResourceLocation PALE_TEX = new ResourceLocation("fondue", "textures/effect/pale.png");
	public static final DamageSource SIZZLE = new DamageSource("fondue.sizzle").setFireDamage();
	
	private final Invoker damageEntity = Invokers.findMethod(EntityLivingBase.class, null, new String[]{"func_70665_d", "damageEntity", "d"}, DamageSource.class, float.class);
	private final Accessor<DamageSource> lastDamageSource = Accessors.findField(EntityLivingBase.class, "field_189750_bF", "lastDamageSource", "bF");
	private final Accessor<Long> lastDamageStamp = Accessors.findField(EntityLivingBase.class, "field_189751_bG", "lastDamageStamp", "bG");
	private final Accessor<Float> lastDamage = Accessors.findField(EntityLivingBase.class, "field_110153_bc", "lastDamage", "bc");
	private final Invoker checkTotemDeathProtection = Invokers.findMethod(EntityLivingBase.class, null, new String[]{"func_190628_d", "checkTotemDeathProtection", "d"}, DamageSource.class);
	
	public PotionPale() {
		super(true, 0xD4D5AC);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return (duration % ((4-amplifier)*40)) == 0;
	}
	
	@Override
	public void performEffect(EntityLivingBase elb, int amp) {
		if (elb.isImmuneToFire() || elb.isWet()) return;
		boolean day = (elb.world.getWorldTime()%24000) < 13000;
		if (elb.getHealth() > 0 && day && elb.world.canSeeSky(elb.getPosition())) {
			if (!elb.world.isRemote) {
				float dmg = 1;
				if (!elb.world.getBiome(elb.getPosition()).canRain()) {
					dmg = 2;
				}
				if (elb instanceof EntityPlayer) {
					// !?
					lastDamage.set(elb, dmg);
					damageEntity.invoke(elb, SIZZLE, dmg);
					lastDamageSource.set(elb, SIZZLE);
					lastDamageStamp.set(elb, elb.world.getTotalWorldTime());
					if (elb.getHealth() <= 0) {
						if (!(Boolean)checkTotemDeathProtection.invoke(elb, SIZZLE)) {
							elb.onDeath(SIZZLE);
						}
					}
				} else {
					elb.attackEntityFrom(SIZZLE, dmg);
				}
				elb.world.playSound(null, elb.posX, elb.posY, elb.posZ, ModulePale.SIZZLE, SoundCategory.NEUTRAL, 0.15f, 0.85f+(elb.world.rand.nextFloat()/5f));
			}
		}
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase elb, int amplifier, double health) {
	}
	
	@Override
	public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) {
		mc.renderEngine.bindTexture(PALE_TEX);
		GlStateManager.color(1, 1, 1, alpha);
		Gui.drawModalRectWithCustomSizedTexture(x+3, y+3, 0, 0, 18, 18, 18, 18);
	}
	
	@Override
	public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		mc.renderEngine.bindTexture(PALE_TEX);
		Gui.drawModalRectWithCustomSizedTexture(x+6, y+7, 0, 0, 18, 18, 18, 18);
	}
	
}
