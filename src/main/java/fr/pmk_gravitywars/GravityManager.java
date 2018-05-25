package fr.pmk_gravitywars;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.property.SlotPos;

import fr.pmk_gravitywars.map.GravityMap;
import fr.pmk_gravitywars.scheduler.WaintingTeamScheduler;

public class GravityManager {

	private static boolean stop;

	private MainGravityWars instance;
	
	private GravityMap map = null;
	
	private List<Player> redTeamList = new ArrayList<>();
	private List<Player> blueTeamList = new ArrayList<>();
	
	private String state = "waiting";
	
	public static GravityManager getManager(MainGravityWars m) {
		
		return new GravityManager(m);
		
	}
	
	private GravityManager(MainGravityWars m ) {
		this.instance = m;
	}
	
	public void setManagerTeamListener(Object l) {
		Sponge.getEventManager().registerListeners(instance, l);
	}	
	
	public void setSpectatorProtectListener(Object spectatorListener) {
		Sponge.getEventManager().registerListeners(instance, spectatorListener);
		
	}
	
	public void setItemUseListener(Object l) {
		Sponge.getEventManager().registerListeners(instance, l);
	}

	public MainGravityWars getInstance() {
		return instance;
	}

	public void setInstance(MainGravityWars instance) {
		this.instance = instance;
	}

	public GravityMap getMap() {
		return map;
	}

	public void setMap(GravityMap map) {
		this.map = map;
	}

	public List<Player> getRedTeamList() {
		return redTeamList;
	}

	public void setRedTeamList(List<Player> redTeamList) {
		this.redTeamList = redTeamList;
	}

	public List<Player> getBlueTeamList() {
		return blueTeamList;
	}

	public void setBlueTeamList(List<Player> blueTeamList) {
		this.blueTeamList = blueTeamList;
	}

	public String getPartyState() {
		return state;
	}

	public void setPartyState(String state) {
		this.state = state;
	}
	
	@SuppressWarnings("deprecation")
	public void setStuff(Player p) {
		
		Inventory i = p.getInventory();
		
		i.clear();
		
		Hotbar hotbar = i.query(Hotbar.class);
		hotbar.set(new SlotIndex(0), ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "gravitygun:gravitygun").get(), 1));
		
		ItemStack item = ItemStack.builder().itemType(ItemTypes.TNT).quantity(64).build();
		
		hotbar.set(new SlotIndex(1), item);
		
	}
	
	public void autoStart() {
		// TODO Auto-generated method stub
		
		new WaintingTeamScheduler().start();
		
	}	
	
	public static void PreStartParty() {
		
		
		
	}

	public static void startParty() {
		// TODO Auto-generated method stub
		
		GravityManager gm = MainGravityWars.getGravityManager();
		
		gm.setPartyState("ingame");
		
		for (Player p : gm.getBlueTeamList()) {
			
			p.setLocation(gm.getMap().getBlue_team_spawn());
			p.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
			gm.setStuff(p);
			
		}
		
		for (Player p : gm.getRedTeamList()) {
			
			p.setLocation(gm.getMap().getRed_team_spawn());
			p.offer(Keys.GAME_MODE, GameModes.SURVIVAL);			
			gm.setStuff(p);
			
		}
		
		
		
	}
	
	public static void PauseParty() {
		
	}
	
	public static void finishParty() {
		
		if(!stop) {
			
			stop = true;
			
			for (Player iterable_element : Sponge.getServer().getOnlinePlayers()) {
				
				
				
			}
			
		}
		
	}
	
	public static void stopParty() {
		
	}
	
}
