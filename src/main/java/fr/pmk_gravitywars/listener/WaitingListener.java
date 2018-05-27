package fr.pmk_gravitywars.listener;

import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.entity.DestructEntityEvent.Death;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent.Dispense;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Disconnect;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
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
	public void onPlayerJoin(Join e, Player p) {
		// TODO Auto-generated method stub
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§l§d a rejoins la partie !"));
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
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		
		if(r.contains(p)) {
			r.remove(p);
		}else if(b.contains(p)) {
			b.remove(p);
		}
		
		gm.teamLeaveCommand(p);
		
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§l§d a quitté la partie !"));
		
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
			if(r.size() > (MainGravityWars.getMaxPlayer() / 2)) {
				
				// plus de place
				p.sendMessage(Text.of("§c§lL'équipe rouge est complète !"));
				
			}else if(!r.contains(p)) {
				// ajout dans l'équipe
				if(b.contains(p))	// si déjà dans une équipe
					b.remove(p);	// alors remove
				
				r.add(p);
				gm.teamJoinCommand(p, "rouge");
				p.sendMessage(Text.of("§c§lVous avez rejoins l'équipe rouge"));	// envoie du message au joueur
				
				for (Player player : Sponge.getServer().getOnlinePlayers()) {	// envoie du message en broadcast
					
					if(player != p)
						p.sendMessage(Text.of("§c§lLe joueur §r§6" + p.getName() + "§r§c§l à rejoint l'équipe rouge"));
					
				}
				
			}else {
				//déjà dans l'équipe
				p.sendMessage(Text.of("§cVous faites déjà parti de l'équipe rouge !"));
			}			
			
		}else if(n.equals("§9§lRejoindre l'équipe bleu")) {
			// utilisation team bleu
			if(b.size() > (MainGravityWars.getMaxPlayer() / 2)) {
				
				// plus de place
				p.sendMessage(Text.of("§9§lL'équipe bleu est complète !"));
				
			}else if(!b.contains(p)) {
				
				if(r.contains(p))	// si déjà dans une équipe
					r.remove(p);	// alors remove
				
				// ajout dans l'équipe
				b.add(p);
				gm.teamJoinCommand(p, "blue");
				p.sendMessage(Text.of("§9§lVous avez rejoins l'équipe bleu"));	// envoie du message au joueur
				
				for (Player player : Sponge.getServer().getOnlinePlayers()) {	// envoie du message en broadcast
					
					if(player != p)
						p.sendMessage(Text.of("§9§lLe joueur §r§6" + p.getName() + "§r§9§l à rejoint l'équipe bleu"));
					
				}

			}else {
				//déjà dans l'équipe
				p.sendMessage(Text.of("§9§lVous faites déjà parti de l'équipe bleu !"));
			}
			
		}else if(n.equals("§2§lRetour au lobby")) {
			// utilisation retour lobby
			p.sendMessage(Text.of("§2§lVous allez etre renvoyé au lobby dans quelques secondes .... Patientez"));
			// bungeecord renvoie au lobby
			Sponge.getChannelRegistrar().getOrCreateRaw(MainGravityWars.getInstance(), "BungeeCord").sendTo(p, buf -> buf.writeUTF("Connect").writeUTF("lobby"));
			
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

	@Override
	public void onPlayerMove(MoveEntityEvent e, Player p) {
		// TODO Auto-generated method stub
		// pas de restriction
	}
	
}
