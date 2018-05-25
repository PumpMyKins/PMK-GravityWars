package fr.pmk_gravitywars.map;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

public class GravityMap {

	private Location<World> red_team_spawn;
	private Location<World> blue_team_spawn;
	private Location<World> spec_spawn;
	
	private int minY;


	private int max_team_members;
	
	public GravityMap(int n , Location<World> r , Location<World> b , Location<World> s) {
		this.red_team_spawn = r;
		this.blue_team_spawn = b;
		this.spec_spawn = s;
		
		this.minY = b.getBlockY() + n;
		
		
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

	public Location<World> getSpec_spawn() {
		return spec_spawn;
	}

	public void setSpec_spawn(Location<World> spec_spawn) {
		this.spec_spawn = spec_spawn;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}
	
}
