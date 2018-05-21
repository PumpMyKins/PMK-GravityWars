package fr.pmk_gravitywars;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;

import fr.pmk_gravitywars.listener.TeamListener;
import fr.pmk_gravitywars.map.GravityMap;
import me.lucko.luckperms.api.LuckPermsApi;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "pmk-gravitywars", name = "PMK-GravityWars", version = "1.0")
public class MainGravityWars {

	private static LuckPermsApi luckAPI;
	private static GravityManager gravityManager;
	
	
	public static LuckPermsApi getLuckPermsAPI() {
		return luckAPI;
	}
	
	@Inject
	private Logger logger;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configManager;

	
	public Logger getLogger() {
		return this.logger;
	}
	
	public Path getDefaultPathConfig() {
		return defaultConfig;
	}
	
	private static MainGravityWars instance;
	
	@Listener
    public void onPreInit(GamePreInitializationEvent  event) {
        instance = this;
		/*L’événement GamePreInitializationEvent est levé. Durant cet état, le plugin se prépare à l’initialisation. 
		 * Les accès à l’instance du logger par défaut et aux informations concernant les localisations de fichiers de configurations préférées 
		 * sont disponibles.
		 */
        
       
		
    }
	
	@Listener
	public void onInit(GameInitializationEvent event) {
		
		/*L’événement GameInitializationEvent est levé. Durant cet état, le plugin devrait avoir finit tout ce qu’il avait à faire afin de fonctionner. 
		 * Les gestionnaires d’événements sont traités à ce moment là.
		 */
		
		// ajout de la class de listener
		
		
		
	}
	
	@Listener
	public void onPostInit(GamePostInitializationEvent event) {
		
		/* L’événement GamePostInitializationEvent est levé. Par cet état, les communications inter-plugin devraient être prêtes à se produire. 
		 * Les plugins fournissant une API devraient être prêts à accepter des requêtes de base.
		 */
		
	}
	
	@Listener
	public void onStartServer(GameStartingServerEvent event) {
		
		Sponge.getGame().getChannelRegistrar().createRawChannel(this, "BungeeCord");
		
		// Récupération de l'api luckperms
		Optional<ProviderRegistration<LuckPermsApi>> provider = Sponge.getServiceManager().getRegistration(LuckPermsApi.class);
		if (provider.isPresent()) {
		    luckAPI = provider.get().getProvider();
		    
		}
		System.out.println(" instance manager " + StartGameListener.class.getName());
		
		gravityManager = GravityManager.getManager(this); 
        
	    // ajout des maps et spawn
		System.out.println(" création location " + StartGameListener.class.getName());
	    Location<World> l1 = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(245.601 , 60 , -6.5));
	    Location<World> l2 = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(149.317 , 60 , -6.5));
	      
	    System.out.println(" set MAP " + StartGameListener.class.getName());
	    gravityManager.setMap(new GravityMap(2, l1 , l2));  
	    
	    System.out.println(" set du listener " + StartGameListener.class.getName());
	    gravityManager.setManagerTeamListener(new TeamListener(gravityManager));
		
	}

	public static MainGravityWars getInstace() {
		// TODO Auto-generated method stub
		return instance;
	}

	public static GravityManager getGravityManager() {
		return gravityManager;
	}

	public static void setGravityManager(GravityManager gravityManager) {
		MainGravityWars.gravityManager = gravityManager;
	}

}
