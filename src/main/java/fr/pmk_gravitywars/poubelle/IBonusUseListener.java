package fr.pmk_gravitywars.poubelle;

import org.spongepowered.api.event.block.ChangeBlockEvent;

public interface IBonusUseListener {

	public void callItem(ChangeBlockEvent.Place e);
	
}
