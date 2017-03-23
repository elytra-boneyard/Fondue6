package com.elytradev.fondue.module.spiritgraves.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.elytradev.fondue.module.spiritgraves.EntityGrave;
import com.elytradev.fondue.module.spiritgraves.ModuleSpiritGraves;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleSmokeNormal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

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
		MinecraftForge.EVENT_BUS.register(this);
	}

	private static final ResourceLocation VIGNETTE = new ResourceLocation("fondue", "textures/misc/vignette.png");
	
	private float oldGamma;
	private int stareTicksInt = 0;
	private float stareTicks = 0;
	private int rawStareTicks = 0;
	private boolean reverseTicks = false;
	
	private Map<EntityGrave, GraveSound> sounds = Maps.newHashMap();
	
	@SubscribeEvent
	public void onRender(RenderTickEvent e) {
		if (e.phase == Phase.START) {
			oldGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
			stareTicks = stareTicksInt+(reverseTicks ? e.renderTickTime*-2 : e.renderTickTime);
			float gamma = oldGamma - (oldGamma+2)*Math.min(1, stareTicks/40f);
			Minecraft.getMinecraft().gameSettings.gammaSetting = gamma;
		} else {
			Minecraft.getMinecraft().gameSettings.gammaSetting = oldGamma;
		}
	}
	
	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent e) {
		if (rawStareTicks > 40) {
			e.setNewfov(1-(Math.min(0.6667f, ((rawStareTicks+Minecraft.getMinecraft().getRenderPartialTicks())-40)/26f)));
		}
	}
	
	@SubscribeEvent
	public void onPreRenderOverlay(RenderGameOverlayEvent.Pre e) {
		if (e.getType() == ElementType.ALL) {
			Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
			GlStateManager.enableBlend();
			GlStateManager.depthMask(false);
			GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
					SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
			
			GlStateManager.color(0, 0, 0, Math.min(1, stareTicks/40f));
			Minecraft.getMinecraft().getTextureManager().bindTexture(VIGNETTE);
			Tessellator tessellator = Tessellator.getInstance();
			VertexBuffer vertexbuffer = tessellator.getBuffer();
			vertexbuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			vertexbuffer.pos(0, e.getResolution().getScaledHeight(), -90).tex(0, 1).endVertex();
			vertexbuffer.pos(e.getResolution().getScaledWidth(), e.getResolution().getScaledHeight(), -90).tex(1, 1).endVertex();
			vertexbuffer.pos(e.getResolution().getScaledWidth(), 0, -90).tex(1, 0).endVertex();
			vertexbuffer.pos(0, 0, -90).tex(0, 0).endVertex();
			tessellator.draw();
			GlStateManager.depthMask(true);
			GlStateManager.enableDepth();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
					SourceFactor.ONE, DestFactor.ZERO);

		}
	}
	
	@SubscribeEvent
	public void onPostRenderOverlay(RenderGameOverlayEvent.Post e) {
		if (e.getType() == ElementType.ALL) {
			if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityGrave) {
				EntityGrave entity = (EntityGrave)Minecraft.getMinecraft().objectMouseOver.entityHit;
				int width = e.getResolution().getScaledWidth();
				int textY = (e.getResolution().getScaledHeight()/2)+12;
				String title = "\u00A7l"+I18n.format(entity.isOwner(Minecraft.getMinecraft().player) ? "fondue.yourGrave" : "fondue.othersGrave", entity.getOwner().getName());
				Minecraft.getMinecraft().fontRenderer.drawString(title, (width-Minecraft.getMinecraft().fontRenderer.getStringWidth(title))/2, textY, -1);
				textY += 12;
				if (entity.isOwner(Minecraft.getMinecraft().player)) {
					String line1 = I18n.format("fondue.attackToEquip", GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode()));
					String line2 = I18n.format("fondue.rightClickToEquip", GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode()));
					Minecraft.getMinecraft().fontRenderer.drawString(line1, (width-Minecraft.getMinecraft().fontRenderer.getStringWidth(line1))/2, textY, -1);
					textY += 12;
					Minecraft.getMinecraft().fontRenderer.drawString(line2, (width-Minecraft.getMinecraft().fontRenderer.getStringWidth(line2))/2, textY, -1);
				} else {
					String line = I18n.format("fondue.sneakRightClickToTakeOtherGrave", GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()), GameSettings.getKeyDisplayString(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode()));
					Minecraft.getMinecraft().fontRenderer.drawString(line, (width-Minecraft.getMinecraft().fontRenderer.getStringWidth(line))/2, textY, -1);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (e.phase == Phase.START) {
			if (Minecraft.getMinecraft().objectMouseOver != null &&
					Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityGrave) {
				stareTicksInt++;
				rawStareTicks++;
				reverseTicks = false;
				if (stareTicksInt > 40) {
					stareTicksInt = 40;
				}
			} else {
				stareTicksInt -= 2;
				rawStareTicks = 0;
				reverseTicks = true;
				if (stareTicksInt < 0) {
					stareTicksInt = 0;
				}
			}
			Iterator<Map.Entry<EntityGrave, GraveSound>> iter = sounds.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<EntityGrave, GraveSound> en = iter.next();
				if (en.getKey().isDead) {
					Minecraft.getMinecraft().getSoundHandler().stopSound(en.getValue());
					en.getValue().stop();
					iter.remove();
				}
			}
		}
	}
	
	public void onUpdate(EntityGrave entityGrave) {
		if (!sounds.containsKey(entityGrave)) {
			GraveSound sound = new GraveSound(ModuleSpiritGraves.SPIRIT, 0.25f, 1, SoundCategory.AMBIENT, entityGrave);
			Minecraft.getMinecraft().getSoundHandler().playSound(sound);
			sounds.put(entityGrave, sound);
		}
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
