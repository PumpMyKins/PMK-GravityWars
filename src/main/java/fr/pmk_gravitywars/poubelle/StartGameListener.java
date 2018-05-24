package fr.pmk_gravitywars;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

public class StartGameListener {
	
	public static Task s;
	public static boolean start;

	public static void start() {
		
		System.out.println("démarrage de la parit" + StartGameListener.class.getName());
		Task task = Task.builder().execute(new StartTimerTask()).interval(5, TimeUnit.SECONDS).submit(MainGravityWars.getInstace());
		start = true; 
		
	}
	
	public static void stop() {
		start = false;
	}
	
	static class StartTimerTask implements Consumer<Task> {
	    
		private int seconds = 15;
	    
	    @Override
	    public void accept(Task task) {
	    	if(start) {
	    		
	    		s = task;
		        seconds -= 5;
		        
		        if(seconds > 1) {
		        	 System.out.println("task start " + seconds + "  " + StartGameListener.class.getName());
		 	        
		 	         Sponge.getServer().getBroadcastChannel().send(Text.of("Démarrage de la partie dans " + seconds + " secondes"));
		        }else {
		        	if(!MainGravityWars.getGravityManager().getPartyState().equals("ingame"))
		        		GravityManager.startParty();
		        }
	    		
	    	}
	        
	    }
	}
	
}
