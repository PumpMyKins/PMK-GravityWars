package fr.pmk_gravitywars.poubelle;

import org.spongepowered.api.entity.Entity;

public class BonusData {

	private Entity entity;
	private String type;

	public BonusData(Entity e, String b) {
		
		this.entity = e;
		this.type = b;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
