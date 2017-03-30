package com.elytradev.fondue.module.waypoints;

import com.elytradev.concrete.Marshaller;

import io.netty.buffer.ByteBuf;

/**
 * Glue due to a Concrete bug where it doesn't recognize lists of Marshallables.
 */
public class WaypointDataMarshaller implements Marshaller<WaypointData> {

	@Override
	public WaypointData unmarshal(ByteBuf in) {
		WaypointData wd = new WaypointData();
		wd.readFromNetwork(in);
		return wd;
	}

	@Override
	public void marshal(ByteBuf out, WaypointData t) {
		t.writeToNetwork(out);
	}

}
