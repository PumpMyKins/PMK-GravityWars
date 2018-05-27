package fr.pmk_gravitywars.poubelle;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.MainGravityWars;

public class StartGameListener {
	
	public static Task s;
	public static boolean start;
	public static boolean first_start = true;

	public static void start() {
		
		/*System.out.println("démarrage de la parit" + StartGameListener.class.getName());
		
		if(first_start) {
			
			Task task = Task.builder().execute(new StartTimerTask()).interval(5, TimeUnit.SECONDS).submit(MainGravityWars.getInstace());
			first_start = false;
			
		}
		
		start = true; 
		*/
	}
	
	public static void stop() {
		start = false;
	}
	
	static class StartTimerTask implements Consumer<Task> {
	    
		private int seconds = 180;
	    
	    @Override
	    public void accept(Task task) {
	    	/*if(start) {
	    		
	    		s = task;
		        seconds -= 5;
		        
		        if(seconds > 1) {
		        	 System.out.println("task start " + seconds + "  " + StartGameListener.class.getName());
		 	        
		 	         Sponge.getServer().getBroadcastChannel().send(Text.of("§aDémarrage de la partie dans " + seconds + " secondes"));
		        }else {
		        	if(!MainGravityWars.getGravityManager().getPartyState().equals("ingame"))
		        		GravityManager.startParty();
		        }
	    		
	    	}*/
	        
	    }
	}
	
}
