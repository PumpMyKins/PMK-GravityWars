package fr.pmk_gravitywars.map;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

public class GravityMap {

	private Location<World> red_team_spawn;
	private Location<World> blue_team_spawn;
	
	private int positiveX;
	private int negativeX;
	
	private int positiveY;
	private int negativeY;
	
	private int positiveZ;
	private int negativeZ;

	private int max_team_members;
	
	public GravityMap(int n , Location<World> r , Location<World> b) {
		this.max_team_members = n;
		this.red_team_spawn = r;
		this.blue_team_spawn = b;
		
		positiveX = r.getBlockX() + 300;
		negativeX = r.getBlockX() - 300;
		
		positiveY = r.getBlockY() + 50;
		negativeY = r.getBlockY() - 50;
		
		positiveZ = r.getBlockZ() + 300;
		negativeZ = r.getBlockZ() - 300;
	}

	public Location<World> getRed_team_spawn() {
		return red_team_spawn;
	}

	public void setRed_team_spawn(Location<World> red_team_spawn) {
		this.red_team_spawn = red_team_spawn;
	}

	public Location<World> getBlue_team_spawn() {
		return blue_team_spawn;
	}

	public void setBlue_team_spawn(Location<World> blue_team_spawn) {
		this.blue_team_spawn = blue_team_spawn;
	}

	public int getMax_team_members() {
		return max_team_members;
	}

	public void setMax_team_members(int max_team_members) {
		this.max_team_members = max_team_members;
	}

	public int getNegativeX() {
		return negativeX;
	}

	public void setNegativeX(int negativeX) {
		this.negativeX = negativeX;
	}
	
	public int getPositiveX() {
		return positiveX;
	}

	public void setPositiveX(int positiveX) {
		this.positiveX = positiveX;
	}

	public int getPositiveY() {
		return positiveY;
	}

	public void setPositiveY(int positiveY) {
		this.positiveY = positiveY;
	}

	public int getNegativeY() {
		return negativeY;
	}

	public void setNegativeY(int negativeY) {
		this.negativeY = negativeY;
	}

	public int getPositiveZ() {
		return positiveZ;
	}

	public void setPositiveZ(int positiveZ) {
		this.positiveZ = positiveZ;
	}

	public int getNegativeZ() {
		return negativeZ;
	}

	public void setNegativeZ(int negativeZ) {
		this.negativeZ = negativeZ;
	}

	public Location<World> getSpectateLocation() {
		// TODO Auto-generated method stub
		return this.blue_team_spawn;
	}
	
}
