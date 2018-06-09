package fr.pmk_gravitywars.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.MainGravityWars;

public class WaitingScheduler {

	public static boolean state = true;
	
	public static void start(GravityManager gm) {
		
		Task.Builder t = Task.builder();
		
		t.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// vérification si ok pour start ou non
				
				if(state) {
					
					List<Player> r = gm.getRedTeamList();
					List<Player> b = gm.getBlueTeamList();
					
					if(r.size() + b.size() >= MainGravityWars.getMinPlayer()) {
						// ok pour start
						
						// vérification si 2 personne alors 1 dans chaque équipe
						if((r.size() + b.size() == MainGravityWars.getMinPlayer()) & !(r.size() == b.size())) {
							// pas ok car équipe vide
							
							Sponge.getServer().getBroadcastChannel().send(Text.of("§l§5[GravityWars]§c Une des équipes est vide ... Impossible de lancer !"));
							
							
						}else {
							
							// lancement de la partie
							Sponge.getServer().getBroadcastChannel().send(Text.of("§l§5[GravityWars]§d Partie en cours de lancement !"));
							new StartingScheduler().start(gm);
							
							state = false;
							
						}
						
					}else {
						
						Sponge.getServer().getBroadcastChannel().send(Text.of("§l§5[GravityWars]§c Il manque des joueurs pour que la partie démarre !"));
						
					}
					
				}	
				
				//
				
				
				
			}
		}).delay(5, TimeUnit.SECONDS).interval(25, TimeUnit.SECONDS).submit(MainGravityWars.getInstance());
		
	}
	
}