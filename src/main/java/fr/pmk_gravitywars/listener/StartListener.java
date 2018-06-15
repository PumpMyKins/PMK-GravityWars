package fr.pmk_gravitywars.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.projectile.Firework;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent.Death;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent.Dispense;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent.Disconnect;
import org.spongepowered.api.event.network.ClientConnectionEvent.Join;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.item.FireworkShapes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.utils.CoolDownData;
import fr.pmk_gravitywars.utils.CooldownUUIDData;
import fr.pmk_gravitywars.utils.EffectData;

public class StartListener implements IPhaseGame{

	private GravityManager gm;
	private boolean win = false;
	
	private HashMap<UUID, EffectData> hashBonus = new HashMap<>();
	private HashMap<String, CoolDownData> cooldown = new HashMap<>(); 
	
	public StartListener(GravityManager gravityManager) {
		// TODO Auto-generated constructor stub
		this.gm = gravityManager;
	}

	@Override
	@Listener
	public void onPlayerJoin(Join e,@First Player p) {
		// TODO Auto-generated method stub
		
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§d a rejoins les spectateurs !"));
		//teleportation du joueur
		p.setLocation(gm.getMap().getSpec_spawn());
		// mise § jour du gamemode
		p.offer(Keys.GAME_MODE,GameModes.SPECTATOR);
		
	}

	@Override
	@Listener
	public void onPlayerQuit(Disconnect e,@First Player p) {
		// TODO Auto-generated method stub
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		
		if(r.contains(p)) {
			r.remove(p);
			checkWin();
		}else if(b.contains(p)) {
			b.remove(p);
			checkWin();
		}
		
		gm.teamLeaveCommand(p);
		
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§d a quitté la partie !"));
		
		
	}

	private void checkWin() {
		// TODO Auto-generated method stub
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		
		if(this.win)
			return;
		
		if(r.isEmpty()) {
			// bleu gagne
			
			Sponge.getServer().getBroadcastChannel().send(Text.of("§l§5[GravityWars]§c L'équipe bleu gagne !"));
			this.win = true;
			gm.finishGame();
			
		}else if(b.isEmpty()) {
			// rouge gagne
			
			Sponge.getServer().getBroadcastChannel().send(Text.of("§l§5[GravityWars]§c L'équipe rouge gagne !"));
			this.win = true;
			gm.finishGame();
			
		}else {
			return;
		}
		
	}

	@Override
	@Listener
	public void onPlayerUseItem(InteractItemEvent e,@First Player p) {
		
		if(!e.getInteractionPoint().isPresent())
			return;
		
		if(e.getItemStack() == null)
			return;
		
		
		
		if(!p.get(Keys.GAME_MODE).get().equals(GameModes.SURVIVAL))
			return;
		
		ItemStackSnapshot i = e.getItemStack();
		
		CooldownUUIDData c = gm.getHashPlayerData().get(p.getUniqueId().toString());
		
		if(i.getType().equals(ItemTypes.GUNPOWDER)) {
			
			UUID uuid = c.getNormalTNT();
			
			if(!cooldown.containsKey(uuid.toString())) {
				
				cooldown.put(uuid.toString(), new CoolDownData(0));
				
			}
			
			if(!((System.currentTimeMillis() - cooldown.get(uuid.toString()).getCooldown()) <= 750)) {
				
				cooldown.get(uuid.toString()).setCooldown(System.currentTimeMillis());
				
				spawnTNT(p, e.getInteractionPoint().get());
				
			}
			
		}else {
			
			// bonus blindness
			if(i.getType().equals(ItemTypes.BEEF)) {
				
				UUID uuid = c.getBlindTNT();
				
				if(!cooldown.containsKey(uuid.toString())) {
					
					cooldown.put(uuid.toString(), new CoolDownData(0));
					
				}
				
				if(!((System.currentTimeMillis() - cooldown.get(uuid.toString()).getCooldown()) <= 10000)) {
					
					cooldown.get(uuid.toString()).setCooldown(System.currentTimeMillis());
					
					hashBonus.put(spawnTNT(p, e.getInteractionPoint().get()),new EffectData(Color.BLACK, PotionEffect.builder().potionType(PotionEffectTypes.BLINDNESS).amplifier(4).duration(80).build()));
					
				}
				
			}else if(i.getType().equals(ItemTypes.POISONOUS_POTATO)) { 		// bonus poisson
				
				UUID uuid = c.getPoissonTNT();
				
				if(!cooldown.containsKey(uuid.toString())) {
					
					cooldown.put(uuid.toString(), new CoolDownData(0));
					
				}
				
				if(!((System.currentTimeMillis() - cooldown.get(uuid.toString()).getCooldown()) <= 10000)) {
					
					cooldown.get(uuid.toString()).setCooldown(System.currentTimeMillis());
					
					hashBonus.put(spawnTNT(p, e.getInteractionPoint().get()),new EffectData(Color.GREEN, PotionEffect.builder().potionType(PotionEffectTypes.NAUSEA).duration(180).build()));
					
				}				
				
			}else if(i.getType().equals(ItemTypes.SHULKER_SHELL)) { 			// bonus levitation
				
				UUID uuid = c.getLevitateTNT();
				
				if(!cooldown.containsKey(uuid.toString())) {
					
					cooldown.put(uuid.toString(), new CoolDownData(0));
					
				}
				
				if(!((System.currentTimeMillis() - cooldown.get(uuid.toString()).getCooldown()) <= 20000)) {
					
					cooldown.get(uuid.toString()).setCooldown(System.currentTimeMillis());
					
					hashBonus.put(spawnTNT(p, e.getInteractionPoint().get()),new EffectData(Color.GRAY, PotionEffect.builder().potionType(PotionEffectTypes.LEVITATION).amplifier(10).duration(25).build()));
					
				}
				
			}/*else if(i.getType().equals(ItemTypes.TNT)){
				
				UUID uuid = UUID.randomUUID();
				
				if(!cooldown.containsKey(uuid.toString())) {
					
					cooldown.put(uuid.toString(), new CoolDownData(0));
					
				}
				
				if(!((System.currentTimeMillis() - cooldown.get(uuid.toString()).getCooldown()) <= 5)) {
					
					cooldown.get(uuid.toString()).setCooldown(System.currentTimeMillis());
					
					spawnTNT(p, e.getInteractionPoint().get());
					
				}
				
			}*/else {
				
				
				
			}
			
		}
		
	}
	
	private UUID spawnTNT(Player p, Vector3d v) {
		
		World w = p.getLocation().getExtent();
    	
    	PrimedTNT tnt = (PrimedTNT) w.createEntity(EntityTypes.PRIMED_TNT, v);
    	tnt.offer(tnt.explosionRadius().setTo(4));
    	
    	//hashBonus.put(tnt.getUniqueId(), EffectData.buildNormalTnt());	
    	
		w.spawnEntity(tnt);
		
		return tnt.getUniqueId();
		
	}
	
	@Listener
	public void onItemPickup(CollideEntityEvent event, @First Player player) {
		
		for (Entity e : event.getEntities()) {
			
			if(e instanceof Item) {
				event.setCancelled(true);
				e.remove();
				
			}
			
		}		
		
	}
	
	@Listener
	public void OnPlayerPlaceBlock(ChangeBlockEvent.Place e, @First Player p) {
		/*
		for (Transaction<BlockSnapshot> transaction : e.getTransactions()) {
			
		    BlockSnapshot b = transaction.getFinal(); // Block after change
		    
		    if(b.getState().getType().equals(BlockTypes.TNT)) {
		    	
		    	World w = b.getLocation().get().getExtent();
		    	
		    	Location<World> l = b.getLocation().get();
		    	
		    	PrimedTNT tnt = (PrimedTNT) w.createEntity(EntityTypes.PRIMED_TNT, b.getPosition());
		    	tnt.offer(tnt.explosionRadius().setTo(8));
		    	
		    	//hashBonus.put(tnt.getUniqueId(), EffectData.buildNormalTnt());	
		    	
				w.spawnEntity(tnt);
		    	
				e.setCancelled(true);
				gm.setStuff(p);
				
				return;
				
		    }else {
		    	
		    	/*Optional<ItemStackSnapshot> io = e.getCause().getContext().get(EventContextKeys.USED_ITEM);
		    	
		    	if(io.isPresent()) {
		    		
		    		String n = io.get().get(Keys.DISPLAY_NAME).get().toPlain();
		    		
		    		
		    		
		    	}*/
		   /* 	
		    	e.setCancelled(true);
		    	gm.setStuff(p);
		    	
		    					
		    }
		    
		    
		}
		*/
	}
	
	@Listener
	public void onTntDetonate(ExplosionEvent.Detonate event) {
		
		World w = event.getTargetWorld();
		
		/*Firework f = (Firework) w.createEntity(EntityTypes.FIREWORK, event.getExplosion().getLocation().getPosition());
		
		List<FireworkEffect> l = new ArrayList<>();
		l.add(FireworkEffect.builder().color(Color.RED).fade(Color.BLACK).shape(FireworkShapes.LARGE_BALL).build());
		
		f.offer(Keys.FIREWORK_EFFECTS, l);
		f.offer(Keys.FIREWORK_FLIGHT_MODIFIER,0);
		//f.getFireworgetFireworkData().addElement(FireworkEffect.builder().color(Color.RED).build());
		
		w.spawnEntity(f);
		
		f.detonate();*/
		
		Optional<PrimedTNT> o = event.getCause().first(PrimedTNT.class);
		
		if(o.isPresent()) {
			
			PrimedTNT p = o.get();
			
			if(!hashBonus.containsKey(p.getUniqueId())) {
				return;
			}
			
			EffectData data = hashBonus.get(p.getUniqueId());
			
			Firework f = (Firework) w.createEntity(EntityTypes.FIREWORK, event.getExplosion().getLocation().getPosition());
			
			List<FireworkEffect> l = new ArrayList<>();
			l.add(FireworkEffect.builder().color(data.getColor()).fade(data.getColor()).shape(FireworkShapes.LARGE_BALL).build());
			
			f.offer(Keys.FIREWORK_EFFECTS, l);
			f.offer(Keys.FIREWORK_FLIGHT_MODIFIER,0);
			//f.getFireworgetFireworkData().addElement(FireworkEffect.builder().color(Color.RED).build());
			
			w.spawnEntity(f);
			
			f.detonate();
			
			for (Entity e : event.getEntities()) {
				
				if(e instanceof Player) {
					
					System.out.println("player touché");
					
					Player player = (Player) e;
					
					PotionEffectData effects = player.getOrCreate(PotionEffectData.class).get();
					
					effects.addElement(data.getPotionEffect());
					
					System.out.println("player touché");
					
					player.offer(effects);
					
				}
				
			}
			
		}
		
		/*if(hashBonus.containsKey(p.getUniqueId())) {
			
			hashBonus.get(p.getUniqueId()).setColor(f);
			
		}else {
			
			f.getFireworkData().addElement(FireworkEffect.builder().color(Color.RED).build());
			
		}*/
		
	}

	@Override
	@Listener
	public void onPlayerDropItem(Dispense e,@First Player p) {
		// TODO Auto-generated method stub
		gm.setStuff(p);
		e.setCancelled(true);
	}

	@Override
	@Listener
	public void onPlayerClicInventory(ClickInventoryEvent e,@First Player p) {
		// TODO Auto-generated method stub
		//e.setCancelled(true);
	}

	@Override
	@Listener
	public void onPlayerMove(MoveEntityEvent e,@First Player p) {
		// TODO Auto-generated method stub
		
		// pas de restriction
		
		if(p.get(Keys.GAME_MODE).get().equals(GameModes.SPECTATOR)) {
			
			
			if(e.getToTransform().getLocation().getBlockY() < gm.getMap().getMinY()) {
				
				p.setLocation(gm.getMap().getSpec_spawn());
				
			}
			
		}
		
	}

	@Override
	@Listener
	public void onPlayerRespawn(RespawnPlayerEvent e,@First Player p) {
		// TODO Auto-generated method stub
		System.out.println("Respawn");
		e.getTargetEntity().getInventory().clear();
		e.getTargetEntity().offer(Keys.GAME_MODE,GameModes.SPECTATOR);
		e.setToTransform(new Transform<>(gm.getMap().getSpec_spawn()));
		
	}

	@Override
	@Listener
	public void onPlayerDeath(Death e,@First Player p) {
		
		e.setMessage(Text.of("§l§5[GravityWars]§d Le joueur §r§6" + p.getName() + "§r§d est mort !"));
		
		List<Player> r = gm.getRedTeamList();
		List<Player> b = gm.getBlueTeamList();
		
		if(r.contains(p)) {
			r.remove(p);
			checkWin();
		}else if(b.contains(p)) {
			b.remove(p);
			checkWin();
		}
		
		gm.teamLeaveCommand(p);
		
		
	}

}
