package fr.pmk_gravitywars.listener;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent.Death;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent.Dispense;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Disconnect;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;

import fr.pmk_gravitywars.MainGravityWars;

public class StopListener implements IPhaseGame{

	@Override
	@Listener
	public void onPlayerJoin(Join e,@First Player p) {
		// TODO Auto-generated method stub
		p.kick();
	}

	@Override
	public void onPlayerQuit(Disconnect e, Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerUseItem(InteractItemEvent e, Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDropItem(Dispense e, Player p) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPlayerClicInventory(ClickInventoryEvent e, Player p) {
		// TODO Auto-generated method stub
		
	}

	@Listener
	public void onPlayerRespawn(RespawnPlayerEvent e,@First Player p) {
		// TODO Auto-generated method stub
		System.out.println("Respawn");
		e.getTargetEntity().getInventory().clear();
		e.getTargetEntity().offer(Keys.GAME_MODE,GameModes.SPECTATOR);
		e.setToTransform(new Transform<>(MainGravityWars.getGravityManager().getMap().getSpec_spawn()));
		
	}

	@Override
	public void onPlayerDeath(Death e, Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerMove(MoveEntityEvent e, Player p) {
		// TODO Auto-generated method stub
		
	}

	
	
}
