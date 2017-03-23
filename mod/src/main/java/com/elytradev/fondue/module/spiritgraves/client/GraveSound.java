package com.elytradev.fondue.module.spiritgraves.client;

import com.elytradev.fondue.module.spiritgraves.EntityGrave;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class GraveSound extends MovingSound {

	private EntityGrave entity;
	
	public GraveSound(SoundEvent event, float volume, float pitch, SoundCategory category, EntityGrave entity) {
		super(event, category);
		this.volume = volume;
		this.pitch = pitch;
		this.repeat = true;
		this.entity = entity;
	}
	
	@Override
	public void update() {
		xPosF = (float)entity.posX;
		yPosF = (float)entity.posY;
		zPosF = (float)entity.posZ;
	}

	public void stop() {
		donePlaying = true;
	}

	public void setPitch(float f) {
		pitch = f;
	}

}
