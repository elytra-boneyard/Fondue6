package com.elytradev.fondue.module.obelisk.client;

import java.util.Random;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.ModuleClient;
import com.elytradev.fondue.module.obelisk.ModuleObelisk;
import com.elytradev.fondue.module.obelisk.TileEntityObelisk;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ModuleObeliskClient extends ModuleClient {

	@Override
	public String getName() {
		return "Obelisk (Client)";
	}
	
	@Override
	public String getDescription() {
		return "Client-side tweaks for the Obelisk module.";
	}
	
	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of();
	}
	
	private static final ResourceLocation VIGNETTE = new ResourceLocation("fondue", "textures/misc/vignette.png");
	
	private ObeliskSound sound;
	private Random rand = new Random();
	
	public static TileEntityObelisk attuning;
	public static int attuneTicks = 0;
	
	public void spark(TileEntityObelisk teo) {
		Minecraft mc = Minecraft.getMinecraft();
		RayTraceResult rtr = mc.objectMouseOver;
		double x = rtr == null ? teo.getPos().getX()+0.5 : rtr.hitVec.xCoord;
		double y = rtr == null ? teo.getPos().getY()+0.5 : rtr.hitVec.yCoord;
		double z = rtr == null ? teo.getPos().getZ()+0.5 : rtr.hitVec.zCoord;
		
		float t = RenderObelisk.getTime(teo.getWorld(), teo.getPos(), 0);
		
		float sin = (MathHelper.sin(t)+2)/3;
		float cos = (MathHelper.cos(t)+2)/3;
		
		rand.setSeed(RenderObelisk.getSeed(teo.getPos()));
		float r = RenderObelisk.selectColor(rand, sin, cos);
		float g = RenderObelisk.selectColor(rand, sin, cos);
		float b = RenderObelisk.selectColor(rand, sin, cos);
		
		for (int i = 0; i < 2; i++) {
			Particle effect = new ParticleRedstone(mc.world, x+((mc.world.rand.nextFloat()-0.5f)/2f), y+((mc.world.rand.nextFloat()-0.5f)/2f), z+((mc.world.rand.nextFloat()-0.5f)/2f), 0, 0, 0) {};
			effect.setRBGColorF(r, g, b);
			Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		}
	}
	
	public void playAttuneEffect(TileEntityObelisk teo) {
		attuning = teo;
		attuneTicks = 0;
	}
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityObelisk.class, new RenderObelisk());
		sound = new ObeliskSound(ModuleObelisk.PULSATING, SoundCategory.AMBIENT, 0.35f, 1f);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		if (e.phase == Phase.START) {
			attuneTicks++;
		}
	}
	
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre e) {
		e.getMap().registerSprite(new ResourceLocation("fondue", "blocks/obelisk_floor_glowmap"));
		e.getMap().registerSprite(new ResourceLocation("fondue", "blocks/totem_glowmap"));
		e.getMap().registerSprite(new ResourceLocation("fondue", "blocks/totem_top_glowmap"));
	}
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e) {
		if (e.getItemStack().getItem() == Items.BED) {
			e.getToolTip().add(I18n.format("fondue.bedsAreDisabled"));
			e.getToolTip().add(I18n.format("fondue.findObelisks"));
		}
	}
	
	@SubscribeEvent
	public void onPreRenderOverlay(RenderGameOverlayEvent.Pre e) {
		if (e.getType() == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			
			float dist = -1;
			TileEntityObelisk teo = null;
			
			int chunkX = mc.player.chunkCoordX;
			int chunkZ = mc.player.chunkCoordZ;
			
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					Chunk c = mc.world.getChunkFromChunkCoords(chunkX+x, chunkZ+z);
					
					for (TileEntity te : c.getTileEntityMap().values()) {
						if (te instanceof TileEntityObelisk) {
							if (Math.abs(te.getPos().getY()-mc.player.posY) > 5) continue;
							teo = (TileEntityObelisk)te;
							Vec3d a = mc.player.getPositionVector();
							Vec3d b = new Vec3d(te.getPos().getX()+0.5, a.yCoord, te.getPos().getZ()+0.5);
							dist = (float)a.distanceTo(b);
							sound.setPosition(te.getPos());
							if (!mc.getSoundHandler().isSoundPlaying(sound)) {
								try {
									mc.getSoundHandler().playSound(sound);
								} catch (Throwable t) {}
							}
						}
					}
				}
			}
			
			if (teo == null && mc.getSoundHandler().isSoundPlaying(sound)) {
				try {
					mc.getSoundHandler().stopSound(sound);
				} catch (Throwable t) {}
			}
			
			if (teo != null && dist >= 0 && dist < 5) {
				dist -= 1;
				if (dist < 0) dist = 0;
				
				float t = RenderObelisk.getTime(teo.getWorld(), teo.getPos(), e.getPartialTicks());
				
				float sin = (MathHelper.sin(t)+2)/3;
				float cos = (MathHelper.cos(t)+2)/3;
				
				rand.setSeed(RenderObelisk.getSeed(teo.getPos()));
				float r = RenderObelisk.selectColor(rand, sin, cos);
				float g = RenderObelisk.selectColor(rand, sin, cos);
				float b = RenderObelisk.selectColor(rand, sin, cos);
				
				mc.entityRenderer.setupOverlayRendering();
				GlStateManager.enableBlend();
				GlStateManager.depthMask(false);
				GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
						SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
				
				float a = 1-((dist-1)/4f);
				
				a /= 2;
				
				GlStateManager.color(r, g, b, a);
				mc.getTextureManager().bindTexture(VIGNETTE);
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
	}

}
