package fr.pmk_gravitywars.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.explosive.PrimedTNT;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.projectile.Firework;
import org.spongepowered.api.entity.vehicle.minecart.TNTMinecart;
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
import org.spongepowered.api.item.FireworkShape;
import org.spongepowered.api.item.FireworkShapes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import fr.pmk_gravitywars.GravityManager;
import fr.pmk_gravitywars.utils.EffectData;

public class StartListener implements IPhaseGame{

	private GravityManager gm;
	private boolean win = false;
	
	private HashMap<UUID, EffectData> hashBonus = new HashMap<>();
	
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
		
		System.out.println("use");
		
		if(!p.get(Keys.GAME_MODE).get().equals(GameModes.SURVIVAL))
			return;
		
		ItemStackSnapshot i = e.getItemStack();
		
		if(i.getType().equals(ItemTypes.GUNPOWDER)) {
			
			World w = p.getLocation().getExtent();
	    	
	    	PrimedTNT tnt = (PrimedTNT) w.createEntity(EntityTypes.PRIMED_TNT, e.getInteractionPoint().get());
	    	tnt.offer(tnt.explosionRadius().setTo(4));
	    	
	    	//hashBonus.put(tnt.getUniqueId(), EffectData.buildNormalTnt());	
	    	
			w.spawnEntity(tnt);
	    	
			e.setCancelled(true);
			
		}else {
			
			// bonus
			
		}
		
		/*BlockSnapshot b = e.getTransactions().get(0).getFinal();
		
		if(e.getTransactions().get(0).getFinal().getState().getType().equals(BlockTypes.TNT)){
			
			World w = b.getLocation().get().getExtent();
			
			Entity tnt = w.createEntity(EntityTypes.PRIMED_TNT, b.getLocation().get().getTileEntity().get().getLocation().getBlockPosition());
			
			w.spawnEntity(tnt);
			
		}else {
			
			ItemStackSnapshot i = e.getCause().getContext().get(EventContextKeys.USED_ITEM).get();
			String n = i.get(Keys.DISPLAY_NAME).get().toPlain();
			
			/*for (Bon iterable_element : gm.getBonusList()) {
				
			}*/
		/*	
		}
		e.setCancelled(true);
		*/
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
	public void onTntDetonate(ExplosionEvent.Detonate event, @First PrimedTNT p) {
		
		World w = event.getTargetWorld();
		Firework f = (Firework) w.createEntity(EntityTypes.FIREWORK, event.getExplosion().getLocation().getPosition());
		
		List<FireworkEffect> l = new ArrayList<>();
		l.add(FireworkEffect.builder().color(Color.RED).fade(Color.BLACK).shape(FireworkShapes.LARGE_BALL).build());
		
		f.offer(Keys.FIREWORK_EFFECTS, l);
		f.offer(Keys.FIREWORK_FLIGHT_MODIFIER,0);
		//f.getFireworgetFireworkData().addElement(FireworkEffect.builder().color(Color.RED).build());
		
		w.spawnEntity(f);
		f.detonate();
		/*if(hashBonus.containsKey(p.getUniqueId())) {
			
			hashBonus.get(p.getUniqueId()).setColor(f);
			
		}else {
			
			f.getFireworkData().addElement(FireworkEffect.builder().color(Color.RED).build());
			
		}*/
		
		
		
		
		
		for (Entity e : event.getEntities()) {
			
			if(e instanceof Player) {
				
				Player player = (Player) e;
				
				System.out.println("player " + player.getName());
				// ajout des effects
				
			}
			
		}
		
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
