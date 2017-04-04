package net.richardsprojects.disasters.runnables;

import net.richardsprojects.disasters.Config;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.richardsprojects.disasters.Disasters;
import net.richardsprojects.disasters.Utils;

public class MeteorHandler extends BukkitRunnable {

	private Disasters plugin;
	private boolean random;
	
	public MeteorHandler(Disasters plugin, boolean random) {
		this.plugin = plugin;
		this.random = random;
	}

	public void run() {
		if(plugin.getServer().getWorld(Config.worldName) != null) {
			final World w = plugin.getServer().getWorld(Config.worldName);
			if(w.getPlayers().size() > 0) {
				int randomNumber = Utils.randInt(1, Config.meteorChanceMax);
				if(randomNumber <= Config.meteorChance || !random) {
					//Generate a random player					
					int playerNumber = Utils.randInt(0, w.getPlayers().size() - 1);
					final Player p = w.getPlayers().get(playerNumber);
					
					//Determine meteor size
					final int meteorSize = Utils.randInt(1, 3);
					
					//Pick location
					int locX = Utils.randInt(p.getLocation().getBlockX() - 20, p.getLocation().getBlockX() + 20);
					int locZ = Utils.randInt(p.getLocation().getBlockZ() - 20, p.getLocation().getBlockZ() + 20);
					final Location yawLoc = new Location(w, locX, 245, locZ, 270F, 20F);					
					final Location twoblocks = new Location(w, locX, 254, locZ);
					
					//Warn them of impending meteor
					String msg = Config.meteorMessage;
					msg = msg.replace("[SIZE]", Utils.meteorSize(meteorSize));
					msg = msg.replace("[COORDS]", "X: " + locX + ", Z: " + locZ);
					p.sendMessage(Utils.colorCodes(msg));
					
					//Send down meteor
					new BukkitRunnable() {
						public void run() {							
							LargeFireball fb = (LargeFireball) w.spawn(twoblocks, LargeFireball.class);
							Arrow e = (Arrow) w.spawn(yawLoc, Arrow.class);
							e.setCustomName(Utils.meteorSize(meteorSize));
							int angleX = Utils.randInt(0, 360);
							int angleY = Utils.randInt(0, 360);
							int angleZ = Utils.randInt(0, 360);
							Vector angle = new Vector(angleX, angleY, angleZ);
							Vector dir = new Vector(0, -2, 0);
							dir.angle(angle);
							fb.setVelocity(dir);
							e.setPassenger(fb);
							e.setVelocity(dir);
						}
					}.runTaskLater(plugin, 240);
				}
			}
		}
	}
		
}
