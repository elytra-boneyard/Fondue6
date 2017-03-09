package com.elytradev.fondue;

import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.elytradev.fondue.module.Module;
import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="fondue", name="Fondue", version="@VERSION@")
public class Fondue {

	private static final Logger log = LogManager.getLogger("Fondue");
	
	private final List<Module> modules = Lists.newArrayList();
	
	public Fondue() throws Exception {
		for (ClassInfo ci : ClassPath.from(getClass().getClassLoader()).getTopLevelClassesRecursive("com.elytradev.fondue.module")) {
			Class<?> clazz = ci.load();
			if (Modifier.isAbstract(clazz.getModifiers())) continue;
			if (clazz.getSuperclass() == Module.class) {
				modules.add((Module)clazz.newInstance());
				log.info("Discovered module {}", clazz.getSimpleName().replace("Module", ""));
			}
		}
		
	}
	
	@EventHandler
	public void onPreInit(FMLPreInitializationEvent e) {
		for (Module m : modules) {
			m.onPreInit(e);
		}
	}
	
	@EventHandler
	public void onInit(FMLInitializationEvent e) {
		for (Module m : modules) {
			m.onInit(e);
		}
	}
	
	@EventHandler
	public void onPostInit(FMLPostInitializationEvent e) {
		for (Module m : modules) {
			m.onPostInit(e);
		}
	}
	
}
