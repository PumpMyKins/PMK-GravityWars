package fr.pmk_gravitywars.listener;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextRepresentable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.MainGravityWars;
import fr.pmk_gravitywars.StartGameListener;
import fr.pmk_gravitywars.map.GravityMap;

public class TeamListener {

	private GravityManager gm;
	
	public TeamListener(GravityManager gravityManager) {
		// TODO Auto-generated constructor stub
		this.gm = gravityManager;
	}

	@Listener
	public void onPlayerJoinServer(ClientConnectionEvent.Join e, @First Player p) {
		
		System.out.println(" player join " + StartGameListener.class.getName());
		
		if(gm.getPartyState().equals("waiting")) {
			
			System.out.println(" waiting state " + StartGameListener.class.getName());
			
			List<Player> b = gm.getBlueTeamList();
			List<Player> r = gm.getRedTeamList();
			
			if(r.size() > b.size()) {
				System.out.println(" ajout team bleu " + StartGameListener.class.getName());
				e.setMessage(Text.of("§9Le joueur §6" + p.getName() + "§9 a rejoint l'équipe bleu !"));
				b.add(p);
				p.setLocationSafely(gm.getMap().getBlue_team_spawn());
				//Sponge.getServer().getWorld(p.getLocation().getExtent().getName()).get().getChunk(p.getLocation().getChunkPosition()).get().loadChunk(true);
				//Sponge.getServer().loadWorld(p.getLocation().getExtent().getName());
				//Sponge.getServer().getWorld(gm.getMap().getBlue_team_spawn().getExtent().getName()).get().loadChunkAsync(gm.getMap().getBlue_team_spawn().getChunkPosition(), true);
			}else {
				System.out.println(" ajout team rouge " + StartGameListener.class.getName());
				e.setMessage(Text.of("§cLe joueur §6" + p.getName() + "§c a rejoint l'équipe rouge !"));
				r.add(p);
				p.setLocationSafely(gm.getMap().getRed_team_spawn());
				//Sponge.getServer().loadWorld(p.getLocation().getExtent().getName());
				//Sponge.getServer().getWorld(gm.getMap().getSpectateLocation().getExtent().get).get().getChunk(p.getLocation().getChunkPosition()).get().loadChunk(true);
				//Sponge.getServer().getWorld(gm.getMap().getBlue_team_spawn().getExtent().getName()).get().set.loadChunkAsync(gm.getMap().getRed_team_spawn().getChunkPosition(), true);
			}
			
			System.out.println(" set gamemode " + StartGameListener.class.getName());
			p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
			
			
			if(r.size() == b.size()) {
				System.out.println(" assez de joueur appelle de start " + StartGameListener.class.getName());
				StartGameListener.start();
			}else {
				System.out.println(" joueur en plus " + StartGameListener.class.getName());
				if(StartGameListener.start) {
					System.out.println(" cancel de du starting " + StartGameListener.class.getName());
					StartGameListener.stop();
				}
			}
			
		}else if(gm.getPartyState().equals("ingame")){
			
			System.out.println(" déjà en game donc set spec " + StartGameListener.class.getName());
			p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
			p.setLocation(gm.getMap().getSpectateLocation());
			
			
		}else if(gm.getPartyState().equals("off")) {
			
			p.kick(Text.of("Serveur fermé !"));
			
		}else {
			p.kick(Text.of("Serveur fermé !"));
			
		}
		
	}
	
	@Listener
	public void onPlayerLeftServer(ClientConnectionEvent.Disconnect e, @First Player p) {
		
		System.out.println(" appelle de l'event de déconnection " + StartGameListener.class.getName());
		
		List<Player> b = gm.getBlueTeamList();
		List<Player> r = gm.getRedTeamList();
		
		if(gm.getPartyState().equals("waiting")) {
			
			System.out.println(" remove du player " + StartGameListener.class.getName());
			
			if(b.contains(p)) {
				b.remove(p);
				e.setMessage(Text.of("Le joueur " + p.getName() + " a quitté la partie !"));
			}
			
			if(r.contains(p)) {
				r.remove(p);
				e.setMessage(Text.of("Le joueur " + p.getName() + " a quitté la partie !"));
			}
				
			
			e.setMessage(Text.of("Le joueur " + p.getName() + " a quitté la partie !"));
			
		}else if(gm.getPartyState().equals("ingame")){
			
			System.out.println(" remove du player " + StartGameListener.class.getName());
			
			if(b.contains(p)) {
				b.remove(p);
				e.setMessage(Text.of("Le joueur " + p.getName() + " a quitté la partie !"));
			}
			
			if(r.contains(p)) {
				r.remove(p);
				
				e.setMessage(Text.of("Le joueur " + p.getName() + " a quitté la partie !"));
				
			}
			
			if(b.isEmpty()) {
				
				e.setMessage(Text.of("L'équipe rouge a remporté la partie"));
				
			}else if(r.isEmpty()){
				
				e.setMessage(Text.of("L'équipe bleu a remporté la partie"));
				
			}
			
			for (Player pl : Sponge.getServer().getOnlinePlayers()) {
				
				pl.setLocationSafely(gm.getMap().getRed_team_spawn());
				pl.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
				e.setMessage(Text.of("Retour au lobby dans quelques secondes !"));
				
				new StopGameScheduler().start();
			}
				
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
			
			System.out.println(" ingame donc remove du player " + StartGameListener.class.getName());
			
			if(b.contains(p)) {
				b.remove(p);
				e.setMessage(Text.of("Le joueur " + p.getName() + " nous a quitté suite à une explosion non controlé !"));
			}
			
			if(r.contains(p)) {
				r.remove(p);
				e.setMessage(Text.of("Le joueur " + p.getName() + " nous a quitté suite à une explosion non controlé !"));
			}
			
			System.out.println(" set du gamemode lors de la mort " + StartGameListener.class.getName());
			p.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
			p.setLocationSafely(gm.getMap().getSpectateLocation());
			
			
			
			System.out.println(" équipe gagnate ou non " + StartGameListener.class.getName());
			if(b.isEmpty()) {
				
				e.setMessage(Text.of("L'équipe rouge a remporté la partie"));
				
			}else if(r.isEmpty()){
				
				e.setMessage(Text.of("L'équipe bleu a remporté la partie"));
				
			}
			
			for (Player pl : Sponge.getServer().getOnlinePlayers()) {
				
				pl.setLocationSafely(gm.getMap().getRed_team_spawn());
				pl.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
				e.setMessage(Text.of("Retour au lobby dans quelques secondes !"));
				
				new StopGameScheduler().start();
			}
			
		}
		
	}
	
	@Listener
	public void onPlayerUseItem(InteractItemEvent event, @First Player p) {
		System.out.println(event.getItemStack().getType());
		
	}
	
}
