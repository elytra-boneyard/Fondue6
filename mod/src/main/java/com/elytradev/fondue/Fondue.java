package com.elytradev.fondue;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elytradev.concrete.NetworkContext;
import com.elytradev.fondue.module.Module;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.base.Predicates;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Fondue.MODID, name=Fondue.NAME, version=Fondue.VERSION)
public class Fondue {
	
	public static final String MODID = "fondue";
	public static final String NAME = "Fondue";
	public static final String VERSION = "@VERSION@";

	private static final Logger log = LogManager.getLogger("Fondue");
	
	@Instance
	public static Fondue inst;
	public static int nextEntityId = 0;
	
	public final List<Module> modules = Lists.newArrayList();
	public NetworkContext network;
	
	public static boolean isModuleLoaded(Class<? extends Module> clazz) {
		for (Module m : inst.modules) {
			if (m.getClass().isAssignableFrom(clazz)) return true;
		}
		return false;
	}
	
	public static <T extends Module> T getModule(Class<T> clazz) {
		for (Module m : inst.modules) {
			if (m.getClass().isAssignableFrom(clazz)) {
				return (T)m;
			}
		}
		return null;
	}
	
	@EventHandler
	public void onConstructing(FMLConstructionEvent e) throws Exception {
		Set<ClassInfo> info = ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive("com.elytradev.fondue.module");
		ProgressBar bar = ProgressManager.push("Discovering modules", info.size());
		for (ClassInfo ci : info) {
			if (!e.getSide().isClient() && ci.getName().endsWith("Client")) {
				bar.step(ci.getName());
				continue;
			}
			if (!ci.getName().contains("Module")) {
				bar.step(ci.getName());
				continue;
			}
			Class<?> clazz = ci.load();
			bar.step(clazz);
			if (Modifier.isAbstract(clazz.getModifiers())) continue;
			if (clazz.getSuperclass() == Module.class ||
					(e.getSide().isClient() && clazz.getSuperclass() == ModuleClient.class)) {
				Module m = (Module)clazz.newInstance();
				modules.add(m);
			}
		}
		Collections.sort(modules, (a, b) -> ComparisonChain.start()
				.compare(a.getWeight(), b.getWeight())
				.compare(a.getName(), b.getName())
				.result());
		for (Module m : modules) {
			log.info("Discovered {}module {}", m instanceof ModuleClient ? "client " : "", m.getName());
		}
		log.info("Disabling modules is not officially supported. If you really want to disable one, open the Fondue mod jar and delete the module class.");
		ProgressManager.pop(bar);
		MetadataCollection mc = new MetadataCollection() {
			@Override
			public ModMetadata getMetadataForId(String modId, Map<String, Object> extraData) {
				if (modId.equals(MODID)) {
					ModMetadata mm = new ModMetadata();
					mm.name = NAME;
					mm.modId = MODID;
					mm.version = VERSION;
					mm.authorList = Lists.newArrayList("unascribed", "Falkreon");
					StringBuilder sb = new StringBuilder("Fondue tweaks mod. Loaded modules:\n");
					for (Module m : modules) {
						sb.append("\u00A7m--\u00A7e ");
						sb.append(m.getName());
						sb.append("\u00A7r \u00A7m--\n");
						sb.append(m.getDescription());
						if (!m.getGoals().isEmpty()) {
							sb.append("\n");
							for (Goal g : m.getGoals()) {
								sb.append(" - ");
								sb.append(g.description);
								sb.append("\n");
							}
						}
						sb.append("\n\n");
					}
					mm.description = sb.toString();
					return mm;
				}
				return super.getMetadataForId(modId, extraData);
			}
		};
		Loader.instance().activeModContainer().bindMetadata(mc);
	}
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		network = NetworkContext.forChannel("fondue");
		ProgressBar bar = ProgressManager.push("Pre initializing modules", modules.size());
		for (Module m : modules) {
			bar.step(m.getName());
			m.onPreInit(e);
		}
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		ProgressBar bar = ProgressManager.push("Initializing modules", modules.size());
		for (Module m : modules) {
			bar.step(m.getName());
			m.onInit(e);
		}
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent e) {
		ProgressBar bar = ProgressManager.push("Initializing modules", modules.size());
		for (Module m : modules) {
			bar.step(m.getName());
			m.onPostInit(e);
		}
		ProgressManager.pop(bar);
	}
	
	public static void sendUpdatePacket(TileEntity te) {
		sendUpdatePacket(te, te.getUpdateTag());
	}
	
	public static void sendUpdatePacket(TileEntity te, NBTTagCompound nbt) {
		WorldServer ws = (WorldServer)te.getWorld();
		Chunk c = te.getWorld().getChunkFromBlockCoords(te.getPos());
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(te.getPos(), te.getBlockMetadata(), nbt);
		for (EntityPlayerMP player : te.getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
			if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.xPosition, c.zPosition)) {
				player.connection.sendPacket(packet);
			}
		}
	}
	
}
