package com.elytradev.fondue.module.spiritgraves.client;

import java.util.Set;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.elytradev.fondue.module.spiritgraves.EntityGrave;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModuleSpiritGravesClient extends ModuleClient {

	@Override
	public String getName() {
		return "Spirit Graves (Client)";
	}

	@Override
	public String getDescription() {
		return "Registers renderers.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		RenderingRegistry.registerEntityRenderingHandler(EntityGrave.class, RenderGrave::new);
	}

	public void onUpdate(EntityGrave entityGrave) {
		ParticleManager pm = Minecraft.getMinecraft().effectRenderer;
		for (int i = 0; i < 16; i++) {
			double x = entityGrave.posX + entityGrave.world.rand.nextGaussian()/8;
			double y = (entityGrave.posY+0.25) + entityGrave.world.rand.nextGaussian()/8;
			double z = entityGrave.posZ + entityGrave.world.rand.nextGaussian()/8;
			ParticleSmokeNormal p = new ParticleSmokeNormal(entityGrave.world, x, y, z, 0, 0, 0, 1f) {};
			p.setRBGColorF(0, 0, 0);
			pm.addEffect(p);
		}
	}

}
