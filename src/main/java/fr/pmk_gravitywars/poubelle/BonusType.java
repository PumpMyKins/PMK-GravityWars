package fr.pmk_gravitywars.poubelle;

import org.spongepowered.api.item.inventory.ItemStack;

public class BonusType {

	private ItemStack item;
	private String name;
	private IBonusUseListener listener;
	
	public BonusType(String n, ItemStack i, IBonusUseListener l) {
		
		this.name = n;
		this.item = i;
		this.listener = l;
		
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IBonusUseListener getListener() {
		return listener;
	}

	public void setListener(IBonusUseListener listener) {
		this.listener = listener;
	}
	
}
