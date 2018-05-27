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
					
					if(r.size() + b.size() >= 2) {
						// ok pour start
						// lancement de la partie
						Sponge.getServer().getBroadcastChannel().send(Text.of("Lancement du compte à rebourd pour le démarage"));
						new StartingScheduler().start(gm);
						
						state = false;
					}else {
						
						Sponge.getServer().getBroadcastChannel().send(Text.of("il manque des joueurs pour que la partie se lance"));
						
					}
					
				}	
				
				//
				
				
				
			}
		}).delay(5, TimeUnit.SECONDS).interval(30, TimeUnit.SECONDS).submit(MainGravityWars.getInstance());
		
	}
	
}
