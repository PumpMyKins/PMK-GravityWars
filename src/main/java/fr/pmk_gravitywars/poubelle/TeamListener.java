package fr.pmk_gravitywars.poubelle;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.MainGravityWars;
import fr.pmk_gravitywars.map.GravityMap;

public class TeamListener {

	private GravityManager gm;
	
	public TeamListener(GravityManager gravityManager) {
		// TODO Auto-generated constructor stub
		this.gm = gravityManager;
	}

	@Listener
	public void onPlayerJoinServer(ClientConnectionEvent.Join e, @First Player p) {
		
		if(gm.getPartyState().equals("waiting")) {
			
			Inventory i = p.getInventory();
			
			
			List<Player> b = gm.getBlueTeamList();
			List<Player> r = gm.getRedTeamList();
			
			if((r.size() + b.size() >= MainGravityWars.getMaximumPlayer())) {
				
				p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
				p.sendMessage(Text.of("§c Vous etes spectateur car il n'y a plus de place dans les équipes"));
				
			}else {
				
				if(r.size() > b.size()) {
					
					
					
					
					e.setMessage(Text.of("§9Le joueur §6" + p.getName() + "§9 a rejoint l'équipe bleu !"));
					b.add(p);
					p.setLocationSafely(gm.getMap().getBlue_team_spawn());
					
				}else {
					
					e.setMessage(Text.of("§cLe joueur §6" + p.getName() + "§c a rejoint l'équipe rouge !"));
					r.add(p);
					p.setLocationSafely(gm.getMap().getRed_team_spawn());
					
				}
				
			}
				
				
				
			
			
			p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
			
			
			if((r.size() + b.size() >= MainGravityWars.getMinimumPlayer()))
				return;
			
			if(r.size() == b.size()) {
				System.out.println(" assez de joueur appelle de start " + StartGameListener.class.getName());
				gm.startParty();
			}else {
				
				
				
				if(StartGameListener.start) {
					System.out.println(" cancel de du starting " + StartGameListener.class.getName());
					StartGameListener.stop();
				}
				
				
			}
			
		}else if(gm.getPartyState().equals("ingame")){
			
			p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
			p.setLocation(gm.getMap().getSpec_spawn());
			e.setMessage(Text.of("§aLe joueur §6" + p.getName() + "§a a rejoint les spectateurs !"));
			
		}else if(gm.getPartyState().equals("off")) {
			
			p.kick(Text.of("Serveur fermé !"));
			
		}else {
			p.kick(Text.of("Serveur fermé !"));
			
		}
		
	}
	
	@Listener
	public void onPlayerLeftServer(ClientConnectionEvent.Disconnect e, @First Player p) {
		
		List<Player> b = gm.getBlueTeamList();
		List<Player> r = gm.getRedTeamList();
		
		if(gm.getPartyState().equals("waiting")) {
			
			if(b.contains(p)) {
				b.remove(p);
				e.setMessage(Text.of("§9Le joueur §6" + p.getName() + "§9 a quitté la fil d'attente !"));
			}
			
			if(r.contains(p)) {
				r.remove(p);
				e.setMessage(Text.of("§cLe joueur §6" + p.getName() + "§c a quitté la fil d'attente !"));
			}
				
			
		}else if(gm.getPartyState().equals("ingame")){
			
			System.out.println(" remove du player " + StartGameListener.class.getName());
			
			if(b.contains(p)) {
				b.remove(p);
				e.setMessage(Text.of("§9Le joueur §6" + p.getName() + "§9 a quitté la partie !"));
			}
			
			if(r.contains(p)) {
				r.remove(p);
				
				e.setMessage(Text.of("§cLe joueur §6" + p.getName() + "§c a quitté la partie !"));
				
			}
			
			if(b.isEmpty()) {
				
				Sponge.getGame().getServer().getBroadcastChannel().send(Text.of("L'équipe rouge a remporté la partie"));
				GravityManager.finishParty();
				
			}else if(r.isEmpty()){
				
				e.setMessage(Text.of("L'équipe bleu a remporté la partie"));
				GravityManager.finishParty();				
			}
			
			/*for (Player pl : Sponge.getServer().getOnlinePlayers()) {
				
				pl.setLocationSafely(gm.getMap().getRed_team_spawn());
				pl.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
				e.setMessage(Text.of("Retour au lobby dans quelques secondes !"));
				
				new StopGameScheduler().start();
			}*/
				
		}		
		
	}
	
	@Listener
	public void OnPlayerDeath(DestructEntityEvent.Death e, @First Player p) {
		
		System.out.println(" appelle mort d'un joueur " + StartGameListener.class.getName());
		
		List<Player> b = gm.getBlueTeamList();
		List<Player> r = gm.getRedTeamList();
		
		if(gm.getPartyState().equals("waiting")) {
			
			// rien
			
		}else if(gm.getPartyState().equals("ingame")){
			
			if(b.contains(p)) {
				b.remove(p);
				e.setMessage(Text.of("§8Le joueur §6" + p.getName() + "§8 nous a quitté suite à une explosion non controlé !"));
			}
			
			if(r.contains(p)) {
				r.remove(p);
				e.setMessage(Text.of("§cLe joueur §6" + p.getName() + "§c nous a quitté suite à une explosion non controlé !"));
			}			
			
			if(b.isEmpty()) {
				
				Sponge.getServer().getBroadcastChannel().send(Text.of("§cL'équipe rouge a remporté la partie !"));
				GravityManager.finishParty();
				
			}else if(r.isEmpty()){
				
				Sponge.getServer().getBroadcastChannel().send(Text.of("§9L'équipe bleu a remporté la partie !"));
				GravityManager.finishParty();
				
			}
			
		}
		
	}
	
}
