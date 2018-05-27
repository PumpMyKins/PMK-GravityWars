package fr.pmk_gravitywars.scheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.MainGravityWars;

public class StartingScheduler {

	private boolean state = true;
	private int count = 60;
	
	public void start(GravityManager gm) {
		// TODO Auto-generated method stub
		
		Task.Builder t = Task.builder();
		
		t.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// vérification si ok pour start ou non
				
				List<Player> r = gm.getRedTeamList();
				List<Player> b = gm.getBlueTeamList();
				
				if(state) {
					
					if(r.size() + b.size() <= 2) {
						// pas ok pour start
						
						Sponge.getServer().getBroadcastChannel().send(Text.of("Lancement de la partie impossible car une des équipes n'a pas le nombre de participant suffisant"));
						
						// stop du Starting compteur
						state = false;
						// nouveau lancement du check Waiting
						WaitingScheduler.state = true;
						
					}else {
						
						// soustract count timer
						count -= 5;
						
						
					}
					
					if(count <= 0) {
						
						// start de la partie
						Sponge.getServer().getBroadcastChannel().send(Text.of("Attention lancement de la partie maintenant !"));
						state = false;
						
					}else {
						
						Sponge.getServer().getBroadcastChannel().send(Text.of("Lancement de la partie dans " + count + " secondes"));
						
					}
					
					
					
				}
				
			}
		}).interval(5, TimeUnit.SECONDS).submit(MainGravityWars.getInstance());
		
	}

}
