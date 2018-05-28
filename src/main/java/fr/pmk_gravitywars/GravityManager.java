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

import fr.pmk_gravitywars.listener.StartListener;
import fr.pmk_gravitywars.listener.WaitingListener;
import fr.pmk_gravitywars.map.GravityMap;
import fr.pmk_gravitywars.scheduler.WaitingScheduler;

public class GravityManager {

	private MainGravityWars instance;
	
	private GravityMap map = null;
	
	private List<Player> redTeamList = new ArrayList<>();
	private List<Player> blueTeamList = new ArrayList<>();
	
	private String state = "stop";
	
	public static GravityManager getManager(MainGravityWars m) {
		
		return new GravityManager(m);
		
	}
	
	private GravityManager(MainGravityWars m ) {
		this.instance = m;
	}
	
	// methods d'initialization
	
	public void initGame() {
		
		// set de la map
		this.map = new GravityMap(15, MainGravityWars.getSpawn1(), MainGravityWars.getSpawn2(), MainGravityWars.getSpawnSpec());
		
		// register la classe d'event pour la fase waiting
		teamInitCommand();
		
		Sponge.getGame().getEventManager().registerListeners(MainGravityWars.getInstance(), new WaitingListener(this));
		
		WaitingScheduler.start(this);
		
	}
	
	public void startGame() {
		
		Sponge.getGame().getEventManager().unregisterListeners(new WaitingListener(this));
		Sponge.getGame().getEventManager().registerListeners(MainGravityWars.getInstance(), new StartListener(this));
		
		// t�l�portation de l'�quipe rouge
		
		for (Player player : blueTeamList) {
			
			// t�l�portation
			player.setLocation(getMap().getBlue_team_spawn());
			
			// set du stuff
			setStuff(player);
			
		}
		
		// t�l�portation de l'�quipe bleu
		
		for (Player player : redTeamList) {
			
			// t�l�portation
			player.setLocation(getMap().getBlue_team_spawn());
			
			// set du stuff
			setStuff(player);
			
		}
		
		// t�l�portation des spectateurs;
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			
			if(!redTeamList.contains(player) & !redTeamList.contains(player)) {
				
				//spec
				
				player.setLocation(getMap().getSpec_spawn());
				// mise � jour du gamemode
				player.offer(Keys.GAME_MODE,GameModes.ADVENTURE);
				
			}
			
		}
		
	}
	
	
	
	// setter & getter

	public void teamInitCommand() {
		// TODO Auto-generated method stub
		// cr�ation de la team rouge
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "/scoreboard teams add rouge Equipe rouge");
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "/scoreboard teams option rouge color red");
		
		// creation de la team bleu
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "/scoreboard teams add blue Equipe bleu");
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "/scoreboard teams option blue color blue");
	}
	
	
	public void teamJoinCommand(Player p, String name) {
		// join �quipe
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "/scoreboard teams join " + name + " " + p.getName());
		
	}
	
	public void teamLeaveCommand(Player p) {
		// leave �quipe
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "/scoreboard teams leave " + p.getName());
		
	}

	public MainGravityWars getInstance() {
		return instance;
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
	/*
	public void autoStart() {
		// TODO Auto-generated method stub
		
		new WaintingTeamScheduler().start();
		
	}	
	
	public static void PreStartParty() {
		
		
		
	}

	public void startParty() {
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
	
	public static void finishParty() {
		
		if(!stop) {
			
			stop = true;
			
			for (Player iterable_element : Sponge.getServer().getOnlinePlayers()) {
				
				
				
			}
			
		}
		
	}
	
	public static void stopParty() {
		
	}

	public List<BonusType> getBonusList() {
		// TODO Auto-generated method stub
		return null;
	}*/
	
}
