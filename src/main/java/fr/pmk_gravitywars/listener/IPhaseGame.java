package fr.pmk_gravitywars.listener;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;

public interface IPhaseGame {
	
	public void onPlayerJoin(ClientConnectionEvent.Join e, @First Player p);
	
	public void onPlayerQuit(ClientConnectionEvent.Disconnect e, @First Player p);
	
	public void onPlayerUseItem(InteractItemEvent e, @First Player p);
	
	public void onPlayerDropItem(DropItemEvent.Dispense e, @First Player p);
	
	public void onPlayerClicInventory(ClickInventoryEvent e, @First Player p);
	
	public void onPlayerRespawn(RespawnPlayerEvent e, @First Player p);
	
	public void onPlayerDeath(DestructEntityEvent.Death e, @First Player p);
	
	public void onPlayerMove(MoveEntityEvent e, @First Player p);
	
	@SuppressWarnings("deprecation")
	public default void giveWaintingStuff(Player p) {
		
		Inventory i = p.getInventory();
		Hotbar hotbar = i.query(Hotbar.class);
		
		ItemStack redItem = ItemStack.builder().itemType(ItemTypes.REDSTONE).quantity(1).build();
		redItem.offer(Keys.DISPLAY_NAME, Text.of("§c§lRejoindre l'équipe rouge"));
		
		hotbar.set(new SlotIndex(2), redItem);
		
		ItemStack leaveItem = ItemStack.builder().itemType(ItemTypes.BED).quantity(1).build();
		leaveItem.offer(Keys.DISPLAY_NAME, Text.of("§2§lRetour au lobby"));
		
		hotbar.set(new SlotIndex(4), leaveItem);
		
		ItemStack blueItem = ItemStack.builder().itemType(ItemTypes.LAPIS_ORE).quantity(1).build();
		blueItem.offer(Keys.DISPLAY_NAME, Text.of("§9§lRejoindre l'équipe bleu"));
		
		hotbar.set(new SlotIndex(6), blueItem);
		
	}
	
	public default void givePlayingStuff(Player p) {
		
		
		
	}
	
}
