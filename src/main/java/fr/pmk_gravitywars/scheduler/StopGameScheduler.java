package fr.pmk_gravitywars.scheduler;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.MainGravityWars;

public class StopGameScheduler {

	public void start() {
		
		Task.Builder t = Task.builder();
		
		
		t.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				Sponge.getServer().getBroadcastChannel().send(Text.of("§2Retour au lobby dans quelques secondes !"));
				
				for (Player p : Sponge.getServer().getOnlinePlayers()) {
					
					Sponge.getChannelRegistrar().getOrCreateRaw(MainGravityWars.getInstance(), "BungeeCord").sendTo(p, buf -> buf.writeUTF("Connect").writeUTF("lobby"));
					
				}
				
				new StopServerScheduler().start();
			}
		}).delay(15, TimeUnit.SECONDS).submit(MainGravityWars.getInstance());
	}
	
}
