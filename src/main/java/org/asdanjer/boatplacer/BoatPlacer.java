package org.asdanjer.boatplacer;

import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.events.ClaimPermissionCheckEvent;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import java.sql.SQLOutput;


public final class BoatPlacer extends JavaPlugin implements Listener {
    public static String BOATTAG = "[.boat]";
    GriefPrevention api;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onClaimPermissionCheck(ClaimPermissionCheckEvent event) {
        Claim cl = event.getClaim();
        Event causalEvent = event.getTriggeringEvent();
        boolean allowable = false;
        if (causalEvent == null) return;
        if (causalEvent instanceof PlayerInteractEvent) {
            allowable = handlePlayerInteractEvent((PlayerInteractEvent) causalEvent, cl);
        } else if (causalEvent instanceof VehicleDamageEvent) {
            allowable = handleVehicleDamageEvent((VehicleDamageEvent) causalEvent, cl);
        }
        if (allowable) {
            event.setCancelled(false);
        }
    }

    private boolean handlePlayerInteractEvent(PlayerInteractEvent event, Claim cl) {
        if (cl == null) return false;
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (Tag.ITEMS_BOATS.isTagged(itemInHand.getType())) {
            return ClaimPermission.Build.equals(cl.getPermission(BOATTAG));
        }

        return false;
    }
    public boolean handleVehicleDamageEvent(VehicleDamageEvent event, Claim cl) {
        if(cl==null) return false;
        if(event.getAttacker() instanceof Player){
            if(event.getVehicle() instanceof Boat){
                return ClaimPermission.Build.equals(cl.getPermission(BOATTAG));
            }
    }
        return false;
    }
}
