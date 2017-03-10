package com.elytradev.fondue.module.obelisk.client;

import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ObeliskSound extends PositionedSound {
	
	protected ObeliskSound(SoundEvent soundIn, SoundCategory categoryIn, float volume, float pitch) {
		super(soundIn, categoryIn);
		repeat = true;
		this.volume = volume;
		this.pitch = pitch;
	}

	public void setPosition(BlockPos pos) {
		xPosF = pos.getX()+0.5f;
		yPosF = pos.getY()+0.5f;
		zPosF = pos.getZ()+0.5f;
	}
	

}
