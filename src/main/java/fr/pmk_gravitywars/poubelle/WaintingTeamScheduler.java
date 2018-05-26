package fr.pmk_gravitywars.poubelle;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.scheduler.Task;

import fr.pmk_gravitywars.MainGravityWars;

public class WaintingTeamScheduler {

	public void start() {
		// TODO Auto-generated method stub
		
		Task.Builder t = Task.builder();
		
		t.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			}
		}).delay(2, TimeUnit.SECONDS).interval(10, TimeUnit.SECONDS).submit(MainGravityWars.getInstace());
		
	}

	
	
}
