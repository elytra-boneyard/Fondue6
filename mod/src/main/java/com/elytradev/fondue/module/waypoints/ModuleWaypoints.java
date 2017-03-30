package com.elytradev.fondue.module.waypoints;

import java.util.Set;

import com.elytradev.fondue.Fondue;
import com.elytradev.fondue.Goal;
import com.elytradev.fondue.module.Module;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraft.item.EnumDyeColor.*;

public class ModuleWaypoints extends Module {

	@Override
	public String getName() {
		return "Waypoints";
	}

	@Override
	public String getDescription() {
		return "Adds waypoint blocks which show on an unintrusive compass at the top of the screen when wearing Fruit Glass or Contacts.";
	}

	@Override
	public Set<Goal> getGoals() {
		return ImmutableSet.of(Goal.ENCOURAGE_SOCIALIZATION, Goal.WORK_ON_SMALL_SERVERS);
	}
	
	// Like EntitySheep.getDyeRgb, but brighter. 
	// Lightness +20, Saturation +50
	public static final ImmutableMap<EnumDyeColor, Integer> BRIGHT_COLORS = ImmutableMap.<EnumDyeColor, Integer>builder()
			.put(     BLACK, 0x313131)
			.put(       RED, 0xCE1E1E)
			.put(     GREEN, 0x81AE27)
			.put(     BROWN, 0x8E5E30)
			.put(      BLUE, 0x1942E9)
			.put(    PURPLE, 0x9031DC)
			.put(      CYAN, 0x4295C0)
			.put(    SILVER, 0xA4A4A4)
			.put(      GRAY, 0x5E5E5E)
			.put(      PINK, 0xFF81AA)
			.put(      LIME, 0x93FF03)
			.put(    YELLOW, 0xFFFF31)
			.put(LIGHT_BLUE, 0x5DA1F5)
			.put(   MAGENTA, 0xC840FB)
			.put(    ORANGE, 0xFF8925)
			.put(     WHITE, 0xFFFFFF)
			.build();
	
	public static int mix(int a, int b) {
		float ra = byteToFloat(a >> 16);
		float ga = byteToFloat(a >> 8);
		float ba = byteToFloat(a);
		
		float rb = byteToFloat(b >> 16);
		float gb = byteToFloat(b >> 8);
		float bb = byteToFloat(b);
		
		float rm = (ra+rb)/2f;
		float gm = (ga+gb)/2f;
		float bm = (ba+bb)/2f;
		
		int m = (floatToByte(rm) << 16) | (floatToByte(gm) << 8) | floatToByte(bm);
		
		return m;
	}

	private static float byteToFloat(int i) {
		return (i & 0xFF) / 255f;
	}
	
	private static int floatToByte(float f) {
		return (int)(Math.max(Math.min(f, 1), 0) * 255f);
	}
	
	public static BlockWaypoint WAYPOINT;
	
	@Override
	public void onPreInit(FMLPreInitializationEvent e) {
		GameRegistry.register(WAYPOINT = (BlockWaypoint) new BlockWaypoint()
				.setHardness(2f)
				.setResistance(2000)
				.setLightLevel(0.25f)
				.setRegistryName("waypoint")
				.setUnlocalizedName("fondue.waypoint")
				.setCreativeTab(CreativeTabs.DECORATIONS));
		GameRegistry.register(new ItemBlock(WAYPOINT).setRegistryName("waypoint"));
		
		GameRegistry.registerTileEntity(TileEntityWaypoint.class, "fondue:waypoint");
		
		Fondue.inst.network.register(MessageSetWaypoints.class);
		Fondue.inst.network.register(MessageUpdateWaypoint.class);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		new MessageSetWaypoints(getDataFor(e.player.world).all()).sendTo(e.player);;
	}
	
	@SubscribeEvent
	public void onChangeDimension(PlayerChangedDimensionEvent e) {
		World w = DimensionManager.getWorld(e.toDim);
		new MessageSetWaypoints(getDataFor(w).all()).sendTo(e.player);
	}
	
	public static WaypointWorldData getDataFor(World w) {
		WaypointWorldData data = (WaypointWorldData)w.getPerWorldStorage().getOrLoadData(WaypointWorldData.class, "fondue_waypoints");
		if (data == null) {
			data = new WaypointWorldData("fondue_waypoints");
			w.getPerWorldStorage().setData("fondue_waypoints", data);
		}
		return data;
	}

}
