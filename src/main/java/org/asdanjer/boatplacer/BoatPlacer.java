package org.asdanjer.boatplacer;

import me.ryanhamshire.GriefPrevention.ClaimPermission;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import java.sql.SQLOutput;


public final class BoatPlacer extends JavaPlugin implements Listener {
    public static String BOATTAG="[.boat]";
    GriefPrevention api;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        api=GriefPrevention.instance;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        Block block= event.getClickedBlock();
        if(block==null) return;
        Claim cl= api.dataStore.getClaimAt(block.getLocation(), true, null);
        if(cl==null) return;
        if(Tag.ITEMS_BOATS.isTagged(itemInHand.getType())) {
            if(ClaimPermission.Build.equals(cl.getPermission(BOATTAG))){
                event.setCancelled(false);
            }
        }

    }
    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event){
        if(event.getAttacker() instanceof Player){
            if(event.getVehicle() instanceof Boat){
                Boat boat = (Boat) event.getVehicle();
                Location loc = boat.getLocation();
                Claim cl= api.dataStore.getClaimAt(loc, true, null);
                if(cl==null) return;
                if(ClaimPermission.Build.equals(cl.getPermission(BOATTAG))){
                    event.setCancelled(false);
                }


            }
        }
    }
}
