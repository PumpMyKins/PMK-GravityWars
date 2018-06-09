package fr.pmk_gravitywars.listener;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
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
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.MainGravityWars;

public class WaitingListener implements IPhaseGame{
	
	private GravityManager gm;
	
	public WaitingListener(GravityManager gravityManager) {
		this.gm = gravityManager;
	}

	@Override
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join e,@First Player p) {
		// TODO Auto-generated method stub
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§d a rejoint la partie !"));
		//teleportation du joueur
		p.setLocation(gm.getMap().getRed_team_spawn());
		// mise § jour du gamemode
		p.offer(Keys.GAME_MODE,GameModes.ADVENTURE);
		
		// mise § jour de l'inventaire
		giveWaintingStuff(p);
		
	}

	@Override
	@Listener
	public void onPlayerQuit(ClientConnectionEvent.Disconnect e,@First Player p) {
		// TODO Auto-generated method stub
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		
		if(r.contains(p)) {
			r.remove(p);
		}else if(b.contains(p)) {
			b.remove(p);
		}
		
		gm.teamLeaveCommand(p);
		
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§d a quitté la partie !"));
		
	}

	@Override
	@Listener
	public void onPlayerUseItem(InteractItemEvent e,@First Player p) {
		// TODO Auto-generated method stub
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		

		ItemStackSnapshot i = e.getItemStack();
		
		if(i.isEmpty())
			return;
		
		String n = i.get(Keys.DISPLAY_NAME).get().toPlain();
		System.out.println(n);
		
		if(n.contains("Rejoindre l'équipe rouge")) {
			// utilisation team rouge
			if(r.size() > (MainGravityWars.getMaxPlayer() / 2) | (((r.size() + 1) - b.size()) >= 2)) {
				
				// plus de place
				p.sendMessage(Text.of("§c§lL'équipe rouge est complète !"));
				
			}else if(!r.contains(p)) {
				// ajout dans l'§quipe
				if(b.contains(p))	// si d§j§ dans une §quipe
					b.remove(p);	// alors remove
				
				r.add(p);
				gm.teamJoinCommand(p, "rouge");
				p.sendMessage(Text.of("§c§lVous avez rejoins l'équipe rouge"));	// envoie du message au joueur
				
				Sponge.getServer().getBroadcastChannel().send(Text.of("§c§lLe joueur §r§6" + p.getName() + "§r§c a rejoint l'équipe rouge"));
				
			}else {
				//d§j§ dans l'§quipe
				p.sendMessage(Text.of("§cVous faites déjà parti de l'équipe rouge !"));
			}			
			
		}else if(n.contains("Rejoindre l'équipe bleu")) {
			// utilisation team bleu
			if(b.size() > (MainGravityWars.getMaxPlayer() / 2) | (((b.size() + 1) - r.size()) >= 2)) {
				
				// plus de place
				p.sendMessage(Text.of("§9§lL'équipe bleu est complète !"));
				
			}else if(!b.contains(p)) {
				
				if(r.contains(p))	// si d§j§ dans une §quipe
					r.remove(p);	// alors remove
				
				// ajout dans l'§quipe
				b.add(p);
				gm.teamJoinCommand(p, "blue");
				p.sendMessage(Text.of("§9§lVous avez rejoint l'équipe bleu"));	// envoie du message au joueur
				
				Sponge.getServer().getBroadcastChannel().send(Text.of("§9§lLe joueur §r§6" + p.getName() + "§r§9 rejoint l'équipe bleu"));

			}else {
				//d§j§ dans l'§quipe
				p.sendMessage(Text.of("§9§lVous faites déjà parti de l'équipe bleu !"));
			}
			
		}else if(n.contains("Retour au lobby")) {
			// utilisation retour lobby
			p.sendMessage(Text.of("§2§lVous allez etre renvoyé au lobby dans quelques secondes .... Patientez"));
			// bungeecord renvoie au lobby
			Sponge.getChannelRegistrar().getOrCreateRaw(MainGravityWars.getInstance(), "BungeeCord").sendTo(p, buf -> buf.writeUTF("Connect").writeUTF("lobby"));
			
		}else {
			
			// item non valide
			
		}

		e.setCancelled(true);
		
		p.getInventory().clear();
		giveWaintingStuff(p);	
		
	}

	@Override
	@Listener
	public void onPlayerDropItem(Dispense e,@First Player p) {
		// TODO Auto-generated method stub
		e.setCancelled(true);
		//reset de l'inventaire
		p.getInventory().clear();
		giveWaintingStuff(p);
	}

	@Override
	@Listener
	public void onPlayerClicInventory(ClickInventoryEvent e,@First Player p) {
		// TODO Auto-generated method stub
		e.setCancelled(true);
		//reset de l'inventaire
		p.getInventory().clear();
		giveWaintingStuff(p);
	}

	@Override
	@Listener
	public void onPlayerMove(MoveEntityEvent e,@First Player p) {
		// TODO Auto-generated method stub
		
		// pas de restriction
		
		if(e.getToTransform().getLocation().getBlockY() < 55) {
			
			p.offer(Keys.GAME_MODE,GameModes.ADVENTURE);
			p.setLocation(gm.getMap().getRed_team_spawn());
					
			// mise § jour de l'inventaire
			giveWaintingStuff(p);
			
		}
		
	}

	@Override
	@Listener
	public void onPlayerRespawn(RespawnPlayerEvent e,@First Player p) {
		// TODO Auto-generated method stub		
	}

	@Override
	@Listener
	public void onPlayerDeath(Death e,@First Player p) {
		// TODO Auto-generated method stub
		
		
	}

	
	
	
}
