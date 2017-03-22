package net.richardsprojects.disasters.events;

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
		if(e.getEntity().getWorld().getName().equals(Disasters.worldName)) {
			if(e.getEntity().getCustomName() != null) {
				if(e.getEntity().getCustomName().equals("SmallMeteor")) {
					World w = this.plugin.getServer().getWorld(Disasters.worldName);
					w.createExplosion(e.getEntity().getLocation(), Disasters.smallMeteor);
					e.getEntity().getPassenger().remove();
					e.getEntity().remove();
				}
				if(e.getEntity().getCustomName().equals("MediumMeteor")) {
					World w = this.plugin.getServer().getWorld(Disasters.worldName);
					w.createExplosion(e.getEntity().getLocation(), Disasters.mediumMeteor);
					e.getEntity().getPassenger().remove();
					e.getEntity().remove();
				}
				if(e.getEntity().getCustomName().equals("LargeMeteor")) {
					World w = this.plugin.getServer().getWorld(Disasters.worldName);
					w.createExplosion(e.getEntity().getLocation(), Disasters.largeMeteor);
					e.getEntity().getPassenger().remove();
					e.getEntity().remove();
				}
			}
		}
	}
	
}
