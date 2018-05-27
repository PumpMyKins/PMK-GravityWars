package fr.pmk_gravitywars.poubelle;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import fr.pmk_gravitywars.MainGravityWars;

public class StopServerScheduler {

	public void start() {
		// TODO Auto-generated method stub
		
		Task.Builder t = Task.builder();
		
		t.execute(new Runnable() {
			
			@Override
			public void run() {
				/*// TODO Auto-generated method stub
				Game game = Sponge.getGame();
		    	game.getCommandManager().process(game.getServer().getConsole(), "stop");
				*/
			}
		}).delay(15, TimeUnit.SECONDS).submit(MainGravityWars.getInstance());
		
	}

	
	
}
