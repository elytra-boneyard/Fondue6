package com.elytradev.fondue.module.waypoints;

import com.elytradev.concrete.Marshallable;
import com.google.common.base.Enums;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class WaypointData implements INBTSerializable<NBTTagCompound>, Marshallable {

	public BlockPos pos;
	public int color;
	public WaypointShape shape;
	public WaypointStyle style;
	
	public WaypointData() {
	}
	
	public WaypointData(BlockPos pos, int color, WaypointShape shape, WaypointStyle style) {
		this.pos = pos;
		this.color = color;
		this.shape = shape;
		this.style = style;
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("Position", pos.toLong());
		tag.setInteger("Color", color);
		tag.setString("Shape", shape.name());
		tag.setString("Style", style.name());
		return tag;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.pos = BlockPos.fromLong(nbt.getLong("Position"));
		this.color = nbt.getInteger("Color");
		this.shape = Enums.getIfPresent(WaypointShape.class, nbt.getString("Shape")).or(WaypointShape.DIAMOND);
		this.style = Enums.getIfPresent(WaypointStyle.class, nbt.getString("Style")).or(WaypointStyle.SOLID);
	}

	@Override
	public void writeToNetwork(ByteBuf buf) {
		buf.writeLong(pos.toLong());
		buf.writeMedium(color);
		buf.writeByte(shape.ordinal());
		buf.writeByte(style.ordinal());
	}

	@Override
	public void readFromNetwork(ByteBuf buf) {
		pos = BlockPos.fromLong(buf.readLong());
		color = buf.readUnsignedMedium();
		shape = WaypointShape.values()[buf.readUnsignedByte()];
		style = WaypointStyle.values()[buf.readUnsignedByte()];
	}

}
