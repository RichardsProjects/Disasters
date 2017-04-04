package net.richardsprojects.disasters.events;

import net.richardsprojects.disasters.Config;
import net.richardsprojects.disasters.Disasters;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ImpactEvent implements Listener {

private Disasters plugin;
	
	public ImpactEvent(Disasters plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onImpact(ProjectileHitEvent e) {
		if(e.getEntity().getWorld().getName().equals(Config.worldName)) {
			if(e.getEntity().getCustomName() != null) {
				String name = e.getEntity().getCustomName();
				if(name.equals("small-sized") || name.equals("medium-sized")
						|| name.equals("large-sized")) {
					int power = 0;
					if (name.equals("small-sized")) power = Config.smallMeteor;
					if (name.equals("medium-sized")) power = Config.mediumMeteor;
					if (name.equals("large-sized")) power = Config.largeMeteor;


					World w = this.plugin.getServer().getWorld(Config.worldName);
					double x = e.getEntity().getLocation().getX();
					double y = e.getEntity().getLocation().getY();
					double z = e.getEntity().getLocation().getZ();
					w.createExplosion(x, y, z, power, Config.meteorDamageTerrain, Config.meteorDamageTerrain);

					e.getEntity().getPassenger().remove();
					e.getEntity().remove();
				}
			}
		}
	}
	
}
