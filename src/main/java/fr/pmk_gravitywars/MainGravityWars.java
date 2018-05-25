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

import fr.pmk_gravitywars.listener.ItemUseListener;
import fr.pmk_gravitywars.listener.SpectatorListener;
import fr.pmk_gravitywars.listener.TeamListener;
import fr.pmk_gravitywars.map.GravityMap;
import me.lucko.luckperms.api.LuckPermsApi;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
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
	
	private static Location<World> spawn1 = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(245.601 , 60 , -6.5));
	private static Location<World> spawn2 = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(149.317 , 60 , -6.5));
	private static Location<World> spawnSpec = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(245.601 , 80 , -6.5));
	
	@Listener
	public void onStartServer(GameStartingServerEvent event) {
		
		// register bungeecord channel
		
		Sponge.getGame().getChannelRegistrar().createRawChannel(this, "BungeeCord");
		
		// Récupération de l'api luckperms
		Optional<ProviderRegistration<LuckPermsApi>> provider = Sponge.getServiceManager().getRegistration(LuckPermsApi.class);
		if (provider.isPresent()) {
		    luckAPI = provider.get().getProvider();
		    
		}
		
		// get du manager		
		gravityManager = GravityManager.getManager(this); 
        
	    // ajout des maps et spawn
	    gravityManager.setMap(new GravityMap(15,spawn1,spawn2,spawnSpec));  
	    
	    gravityManager.setSpectatorProtectListener(new SpectatorListener(gravityManager));
	    
	    gravityManager.setManagerTeamListener(new TeamListener(gravityManager));
	    
	    gravityManager.setItemUseListener(new ItemUseListener(gravityManager));
	    
	    //gravityManager.startWaitingCheck();
		
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
