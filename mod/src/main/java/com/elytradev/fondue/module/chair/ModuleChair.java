package com.elytradev.fondue.module.chair;

import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModuleChair extends Module {

	@Override
	public String getName() {
		return "Chair";
	}
	
	@Override
	public String getDescription() {
		return "Adds a Comfy Chair, which lets you speed up time if everyone online sits in one. Since you can still do things while sitting in it, it obsoletes mods like Morpheus.";
	}
	
	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.IMPROVE_VANILLA, Goal.ENCOURAGE_SOCIALIZATION, Goal.WORK_ON_SMALL_SERVERS);
	}
	
	public static final int TIMESKIP_RATE = 80;
	
	public static Set<EntityPlayer> lounging = Sets.newSetFromMap(new WeakHashMap<>());
	
	public static Block CHAIR;
	private boolean lastTimeskipping = false;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		GameRegistry.register(CHAIR = new BlockChair(Material.CLOTH)
				.setHardness(0.8f)
				.setRegistryName("chair")
				.setUnlocalizedName("fondue.chair")
				.setCreativeTab(CreativeTabs.DECORATIONS));
		GameRegistry.register(new ItemBlock(CHAIR).setRegistryName("chair"));
		
		EntityRegistry.registerModEntity(new ResourceLocation("fondue", "seat"), EntitySeat.class, "seat", Fondue.nextEntityId++, Fondue.inst, 64, 64, false);
		
		Fondue.inst.network.register(TimeSkipPacket.class);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(CHAIR,
				"PW ",
				"PWW",
				"/ /",
				'P', "plankWood",
				'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE),
				'/', "stickWood"));
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent e) {
		if (e.phase == Phase.START) {
			Iterator<EntityPlayer> iter = ModuleChair.lounging.iterator();
			while (iter.hasNext()) {
				EntityPlayer ep = iter.next();
				if (ep.isDead) {
					iter.remove();
				} else if (!ep.isRiding()) {
					iter.remove();
				} else {
					Entity riding = ep.getRidingEntity();
					if (!(riding instanceof EntitySeat)) {
						iter.remove();
					}
				}
			}
			World w = DimensionManager.getWorld(0);
			boolean timeskipping;
			if (lounging.size() >= w.playerEntities.size()) {
				w.setWorldTime((((w.getWorldTime()/TIMESKIP_RATE)*TIMESKIP_RATE)+TIMESKIP_RATE)%24000);
				timeskipping = true;
			} else {
				timeskipping = false;
			}
			if (timeskipping != lastTimeskipping) {
				lastTimeskipping = timeskipping;
				new TimeSkipPacket(timeskipping).sendToAllIn(w);
			}
		}
	}
	
}
