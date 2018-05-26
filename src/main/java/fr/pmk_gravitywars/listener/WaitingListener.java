package fr.pmk_gravitywars.listener;

import java.util.List;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.entity.DestructEntityEvent.Death;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent.Dispense;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Disconnect;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.GravityManager;

public class WaitingListener implements IPhaseGame{
	
	private GravityManager gm;
	
	public WaitingListener(GravityManager gravityManager) {
		this.gm = gravityManager;
	}

	@Override
	public void onPlayerJoin(Join e, Player p) {
		// TODO Auto-generated method stub
		e.setMessage(Text.of("[GravityWars]Le joueur " + p.getName() + " a quitté la partie !"));
		//teleportation du joueur
		p.setLocation(gm.getMap().getRed_team_spawn());
		// mise à jour du gamemode
		p.offer(Keys.GAME_MODE,GameModes.ADVENTURE);
		
		// mise à jour de l'inventaire
		giveWaintingStuff(p);
		
	}

	@Override
	public void onPlayerQuit(Disconnect e, Player p) {
		// TODO Auto-generated method stub
		e.setMessage(Text.of("[GravityWars]Le joueur " + p.getName() + " a quitté la partie !"));
		
	}

	@Override
	public void onPlayerUseItem(InteractItemEvent e, Player p) {
		// TODO Auto-generated method stub
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		

		ItemStackSnapshot i = e.getItemStack();
		
		String n = i.get(Keys.DISPLAY_NAME).get().toPlain();
		
		if(n.equals("§c§lRejoindre l'équipe rouge")) {
			// utilisation team rouge
			if(!r.contains(p)) {
				// ajout dans l'équipe
				r.add(p);
				
			}else {
				//déjà dans l'équipe
				
			}			
			
		}else if(n.equals("§9§lRejoindre l'équipe bleu")) {
			// utilisation team bleu
			if(!b.contains(p)) {
				// ajout dans l'équipe
				b.add(p);
				
			}else {
				//déjà dans l'équipe
				
			}
			
		}else if(n.equals("§2§lRetour au lobby")) {
			// utilisation retour lobby
			p.sendMessage(Text.of("§2§lVous allez etre renvoyé au lobby"));
			
		}else {
			
			// item non valide
			
		}
		
		p.getInventory().clear();
		giveWaintingStuff(p);
		
		e.setCancelled(true);	
		
	}

	@Override
	public void onPlayerRespawn(RespawnPlayerEvent e, Player p) {
		// TODO Auto-generated method stub
		
		//teleportation du joueur
		p.setLocation(gm.getMap().getRed_team_spawn());
		// mise à jour du gamemode
		p.offer(Keys.GAME_MODE,GameModes.ADVENTURE);
				
		// mise à jour de l'inventaire
		giveWaintingStuff(p);
		
	}

	@Override
	public void onPlayerDeath(Death e, Player p) {
		// TODO Auto-generated method stub
		e.setMessageCancelled(true);
	}

	@Override
	public void onPlayerDropItem(Dispense e, Player p) {
		// TODO Auto-generated method stub
		e.setCancelled(true);
		//reset de l'inventaire
		p.getInventory().clear();
		giveWaintingStuff(p);
	}

	@Override
	public void onPlayerClicInventory(ClickInventoryEvent e, Player p) {
		// TODO Auto-generated method stub
		e.setCancelled(true);
		//reset de l'inventaire
		p.getInventory().clear();
		giveWaintingStuff(p);
	}
	
}
