package fr.pmk_gravitywars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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
import org.spongepowered.api.text.Text;

import fr.pmk_gravitywars.listener.StartListener;
import fr.pmk_gravitywars.listener.StopListener;
import fr.pmk_gravitywars.listener.WaitingListener;
import fr.pmk_gravitywars.map.GravityMap;
import fr.pmk_gravitywars.scheduler.BonusScheduler;
import fr.pmk_gravitywars.scheduler.StopGameScheduler;
import fr.pmk_gravitywars.scheduler.WaitingScheduler;
import fr.pmk_gravitywars.utils.CooldownUUIDData;

public class GravityManager {

	private MainGravityWars instance;
	
	private GravityMap map = null;
	
	private  HashMap<String, CooldownUUIDData> hashPlayerData = new HashMap<>();
	
	private List<Player> redTeamList = new ArrayList<>();
	private List<Player> blueTeamList = new ArrayList<>();
	
	private WaitingListener waitingListener;
	private StartListener startListener;
	
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
		
		waitingListener = new WaitingListener(this);
		
		Sponge.getGame().getEventManager().registerListeners(MainGravityWars.getInstance(), waitingListener);
		
		WaitingScheduler.start(this);
		
	}
	
	public void startGame() {
		
		Sponge.getGame().getEventManager().unregisterListeners(waitingListener);
		
		startListener = new StartListener(this);
		Sponge.getGame().getEventManager().registerListeners(MainGravityWars.getInstance(), startListener);
		
		new BonusScheduler().start();
		
		// téléportation de l'équipe rouge
		
		for (Player player : blueTeamList) {
			
			// téléportation
			player.setLocation(getMap().getBlue_team_spawn());
			
			// set du stuff
			setStuff(player);
			player.offer(Keys.GAME_MODE,GameModes.SURVIVAL);
			
		}
		
		// téléportation de l'équipe bleu
		
		for (Player player : redTeamList) {
			
			// téléportation
			player.setLocation(getMap().getRed_team_spawn());
			
			// set du stuff
			setStuff(player);
			player.offer(Keys.GAME_MODE,GameModes.SURVIVAL);
			
		}
		
		// téléportation des spectateurs;
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			
			if(!redTeamList.contains(player) & !blueTeamList.contains(player)) {
				
				//spec
				
				player.setLocation(getMap().getSpec_spawn());
				// mise à jour du gamemode
				player.offer(Keys.GAME_MODE,GameModes.SPECTATOR);
				
			}
			
		}
		
	}
	
	public void finishGame() {
		// TODO Auto-generated method stub
		Sponge.getGame().getEventManager().unregisterListeners(startListener);
		Sponge.getGame().getEventManager().registerListeners(MainGravityWars.getInstance(), new StopListener());
		
		
		
		for (Player player : Sponge.getServer().getOnlinePlayers()) {
			
			player.setLocation(getMap().getSpec_spawn());
			// mise à jour du gamemode
			player.offer(Keys.GAME_MODE,GameModes.SPECTATOR);
			
			
		}
		
		new StopGameScheduler().start();
		
	}
	
	
	
	// setter & getter

	public void teamInitCommand() {
		// TODO Auto-generated method stub
		// création de la team rouge
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "scoreboard teams add rouge Equipe rouge");
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "scoreboard teams option rouge color red");
		
		// creation de la team bleu
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "scoreboard teams add blue Equipe bleu");
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "scoreboard teams option blue color blue");
	}
	
	
	public void teamJoinCommand(Player p, String name) {
		// join équipe
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "scoreboard teams join " + name + " " + p.getName());
		
	}
	
	public void teamLeaveCommand(Player p) {
		// leave équipe
		Sponge.getGame().getCommandManager().process(Sponge.getServer().getConsole(), "scoreboard teams leave " + p.getName());
		
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
		
		if(hashPlayerData.containsKey(p.getUniqueId().toString()))
			return;
		
		CooldownUUIDData c = new CooldownUUIDData();
		
		Inventory i = p.getInventory();
		
		i.clear();
		
		Hotbar hotbar = i.query(Hotbar.class);
		hotbar.set(new SlotIndex(0), ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "gravitygun:gravitygun").get(), 1));
		
		ItemStack item = ItemStack.builder().itemType(ItemTypes.GUNPOWDER).quantity(1).build();
		item.offer(Keys.DISPLAY_NAME,Text.of("§cTNT"));
		
		hotbar.set(new SlotIndex(1), item);
		
		UUID uuid = UUID.randomUUID();
		c.setNormalTNT(uuid);
		
		ItemStack item1 = ItemStack.builder().itemType(ItemTypes.BEEF).quantity(1).build();
		item1.offer(Keys.DISPLAY_NAME,Text.of("§5L'aveuglante"));
		
		hotbar.set(new SlotIndex(2), item1);
		
		uuid = UUID.randomUUID();
		c.setBlindTNT(uuid);
		
		ItemStack item2 = ItemStack.builder().itemType(ItemTypes.POISONOUS_POTATO).quantity(1).build();
		item2.offer(Keys.DISPLAY_NAME,Text.of("§aBMX26"));
		
		hotbar.set(new SlotIndex(3), item2);
		
		uuid = UUID.randomUUID();
		c.setPoissonTNT(uuid);
		
		ItemStack item3 = ItemStack.builder().itemType(ItemTypes.SHULKER_SHELL).quantity(1).build();
		item3.offer(Keys.DISPLAY_NAME,Text.of("§7Envole-toi"));
		
		hotbar.set(new SlotIndex(4), item3);
		
		uuid = UUID.randomUUID();
		c.setLevitateTNT(uuid);
		
		hashPlayerData.put(p.getUniqueId().toString(), c);
		
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

	public HashMap<String, CooldownUUIDData> getHashPlayerData() {
		return hashPlayerData;
	}

	public void setHashPlayerData(HashMap<String, CooldownUUIDData> hashPlayerData) {
		this.hashPlayerData = hashPlayerData;
	}
	
}
