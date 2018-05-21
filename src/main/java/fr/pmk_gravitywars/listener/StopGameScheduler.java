package fr.pmk_gravitywars.listener;

import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import fr.pmk_gravitywars.MainGravityWars;

public class StopGameScheduler {

	public void start() {
		
		Task.Builder t = Task.builder();
		
		
		t.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				for (Player p : Sponge.getServer().getOnlinePlayers()) {
					
					Sponge.getChannelRegistrar().getOrCreateRaw(MainGravityWars.getInstace(), "BungeeCord").sendTo(p, buf -> buf.writeUTF("Connect").writeUTF("lobby"));
					
				}
				
				Game game = Sponge.getGame();
		    	game.getCommandManager().process(game.getServer().getConsole(), "stop");
			}
		}).delay(10, TimeUnit.SECONDS).submit(MainGravityWars.getInstace());
	}
	
}
