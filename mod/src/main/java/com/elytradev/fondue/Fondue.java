package com.elytradev.fondue;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elytradev.concrete.NetworkContext;
import com.elytradev.fondue.module.Module;
import com.elytradev.fondue.module.ModuleClient;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ProgressManager.ProgressBar;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="fondue", name="Fondue", version="@VERSION@")
public class Fondue {

	private static final Logger log = LogManager.getLogger("Fondue");
	
	@Instance
	public static Fondue inst;
	
	public final List<Module> modules = Lists.newArrayList();
	public NetworkContext network;
	
	@EventHandler
	public void onConstructing(FMLConstructionEvent e) throws Exception {
		Set<ClassInfo> info = ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive("com.elytradev.fondue.module");
		ProgressBar bar = ProgressManager.push("Discovering modules", info.size());
		for (ClassInfo ci : info) {
			Class<?> clazz = ci.load();
			bar.step(clazz);
			if (Modifier.isAbstract(clazz.getModifiers())) continue;
			if (clazz.getSuperclass() == Module.class ||
					(e.getSide().isClient() && clazz.getSuperclass() == ModuleClient.class)) {
				modules.add((Module)clazz.newInstance());
				log.info("Discovered {}module {}", clazz.getSuperclass() == ModuleClient.class ? "client " : "", clazz.getSimpleName().replace("Module", ""));
			}
		}
		Collections.sort(modules, (a, b) -> Ints.compare(a.getWeight(), b.getWeight()));
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		network = NetworkContext.forChannel("fondue");
		ProgressBar bar = ProgressManager.push("Pre initializing modules", modules.size());
		for (Module m : modules) {
			bar.step(m.getClass().getSimpleName().replace("Module", ""));
			m.onPreInit(e);
		}
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		ProgressBar bar = ProgressManager.push("Initializing modules", modules.size());
		for (Module m : modules) {
			bar.step(m.getClass().getSimpleName().replace("Module", ""));
			m.onInit(e);
		}
		ProgressManager.pop(bar);
	}
	
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent e) {
		ProgressBar bar = ProgressManager.push("Initializing modules", modules.size());
		for (Module m : modules) {
			bar.step(m.getClass().getSimpleName().replace("Module", ""));
			m.onPostInit(e);
		}
		ProgressManager.pop(bar);
	}
	
}
