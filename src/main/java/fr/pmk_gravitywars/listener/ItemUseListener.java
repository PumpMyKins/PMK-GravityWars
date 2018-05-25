package fr.pmk_gravitywars.listener;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.world.World;

import fr.pmk_gravitywars.GravityManager;

public class ItemUseListener {

	private GravityManager gm;
	
	public ItemUseListener(GravityManager gravityManager) {
		// TODO Auto-generated constructor stub
		this.gm = gravityManager;
	}
	
	
	@Listener
	public void OnPlayerPlaceBlock(ChangeBlockEvent.Place e, @First Player p) {
		
		BlockSnapshot b = e.getTransactions().get(0).getFinal();
		System.out.println("tnt");	
		if(e.getTransactions().get(0).getFinal().getState().getType().equals(BlockTypes.TNT)){
			System.out.println("tnt entity");
			World w = b.getLocation().get().getExtent();
			
			Entity tnt = w.createEntity(EntityTypes.PRIMED_TNT, b.getLocation().get().getTileEntity().get().getLocation().getBlockPosition());
			
			w.spawnEntity(tnt);
			
		}
		e.setCancelled(true);
		
	}	

}
