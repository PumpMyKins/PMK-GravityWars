package fr.pmk_gravitywars;

import java.util.Optional;
import java.util.logging.Logger;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;

import me.lucko.luckperms.api.LuckPermsApi;

@Plugin(id = "pmkgravitywars", name = "PMK-GravityWars", version = "1.0")
public class MainGravityWars {

	private static LuckPermsApi luckAPI;
	private static GravityManager gravityManager;
	
	
	public static LuckPermsApi getLuckPermsAPI() {
		return luckAPI;
	}
	
	@Inject
	private Logger logger;
	
	public Logger getLogger() {
		return this.logger;
	}
	
	
	private static MainGravityWars instance;	
	private static Location<World> spawn1; 
	private static Location<World> spawn2; 
	private static Location<World> spawnSpec; 
	
	private static int maxPlayer = 8;
	private static int minPlayer = 2;
	
	@Listener
	public void onStartServer(GameStartingServerEvent event) {
		
		// init map
		spawn1 = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(245.601 , 60 , -6.5));
		spawn2 = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(149.317 , 60 , -6.5));
		spawnSpec = new Location<World>(Sponge.getServer().getWorld("tntmap").get(), new Vector3d(245.601 , 80 , -6.5));
		
		// register bungeecord channel
		
		instance = this;
		
		Sponge.getGame().getChannelRegistrar().createRawChannel(this, "BungeeCord");
		
		// R�cup�ration de l'api luckperms
		Optional<ProviderRegistration<LuckPermsApi>> provider = Sponge.getServiceManager().getRegistration(LuckPermsApi.class);
		if (provider.isPresent()) {
		    luckAPI = provider.get().getProvider();
		    
		}
		
		// get du manager		
		gravityManager = GravityManager.getManager(this); 
        
	    // ajout des maps et spawn
		gravityManager.initGame();
		
		
		
	}

	public static MainGravityWars getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}

	public static Location<World> getSpawn1() {
		return spawn1;
	}

	public static Location<World> getSpawn2() {
		return spawn2;
	}

	public static Location<World> getSpawnSpec() {
		return spawnSpec;
	}

	public static int getMaxPlayer() {
		return maxPlayer;
	}

	public static int getMinPlayer() {
		return minPlayer;
	}

	public static GravityManager getGravityManager() {
		return gravityManager;
	}

	public static void setGravityManager(GravityManager gravityManager) {
		MainGravityWars.gravityManager = gravityManager;
	}

}
