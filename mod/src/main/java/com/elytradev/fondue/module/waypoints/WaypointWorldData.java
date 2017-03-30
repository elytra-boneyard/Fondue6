package com.elytradev.fondue.module.waypoints;

import java.util.Collections;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

public class WaypointWorldData extends WorldSavedData {

	public WaypointWorldData(String name) {
		super(name);
	}

	private Map<BlockPos, WaypointData> waypoints = Maps.newHashMap();
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		waypoints.clear();
		NBTTagList li = nbt.getTagList("Waypoints", NBT.TAG_COMPOUND);
		for (int i = 0; i < li.tagCount(); i++) {
			NBTTagCompound tag = li.getCompoundTagAt(i);
			WaypointData wd = new WaypointData();
			wd.deserializeNBT(tag);
			add(wd);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagList li = new NBTTagList();
		for (WaypointData wd : waypoints.values()) {
			li.appendTag(wd.serializeNBT());
		}
		tag.setTag("Waypoints", li);
		return tag;
	}

	public WaypointData get(BlockPos pos) {
		return waypoints.get(pos);
	}
	
	public void add(WaypointData wd) {
		waypoints.put(wd.pos, wd);
	}
	
	public void remove(BlockPos pos) {
		waypoints.remove(pos);
	}
	
	public Iterable<WaypointData> all() {
		return Collections.unmodifiableCollection(waypoints.values());
	}

}
