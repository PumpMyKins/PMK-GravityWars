package fr.pmk_gravitywars;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;

public class SpectatorListener {

	private GravityManager gm;
	
	public SpectatorListener(GravityManager gravityManager) {
		// TODO Auto-generated constructor stub
		this.gm = gravityManager;
	}
	
	@Listener
	public void OnPlayerRespawn(RespawnPlayerEvent e, @First Player p) {
		
		p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
		p.setLocation(gm.getMap().getSpec_spawn());
		
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@Listener
	public void OnSpectatorMove(MoveEntityEvent e, @First Player p) {
		
		if(gm.getPartyState().equals("ingame")) {
			
			if(p.getGameModeData().get(Keys.GAME_MODE).equals(GameModes.SPECTATOR)) {
				
				if(e.getToTransform().getLocation().getY() < gm.getMap().getMinY())
					e.setCancelled(true);
				
			}
			
		}
		
	}

}
