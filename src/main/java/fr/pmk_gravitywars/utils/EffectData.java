package fr.pmk_gravitywars.utils;

import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Firework;
import org.spongepowered.api.item.FireworkEffect;
import org.spongepowered.api.util.Color;

public class EffectData {	

	private Color color;
	private PotionEffect potionEffect;
	
	public EffectData(Color c, PotionEffect p) {
		
		this.color = c;
		this.potionEffect = p;
		
	}
	
	
	public static EffectData buildNormalTnt() {
		return null;
	}
	
	public static EffectData buildBlindTnt() {
		return null;
	}
	
	public static EffectData buildLevitateTnt() {
		return null;
	}
	
	public static EffectData buildTnt() {
		return null;
	}

	public void setColor(Firework f) {
		// TODO Auto-generated method stub
		f.getFireworkData().addElement(FireworkEffect.builder().color(this.color).build());
	}
	
	public void setEffect(Player p) {
		PotionEffect.builder().particles(true).potionType(PotionEffectTypes.JUMP_BOOST).amplifier(10).duration(10).build();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}


	public PotionEffect getPotionEffect() {
		return potionEffect;
	}


	public void setPotionEffect(PotionEffect potionEffect) {
		this.potionEffect = potionEffect;
	}
	
	
}
