package io.github.jaronetje.autopickup;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.util.List;

public class EventListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        Config config = AutoPickup.instance.getConfig();
        Player player = event.getPlayer();
        List<String> enabledWorlds = config.getStringList("worlds");

        if(!enabledWorlds.contains(player.getLevel().getName()) || (config.getBoolean("needsTool") && !event.getItem().isTool())) return;

        PlayerInventory inventory = event.getPlayer().getInventory();

        boolean sentWarning = false;

        for (Item item : event.getDrops()) {
            boolean canAddItem = inventory.canAddItem(item);

            if(!canAddItem && !sentWarning) {
                player.sendTitle(TextFormat.DARK_RED + config.getString("fullTitle"), TextFormat.DARK_RED + config.getString("fullText"));
                sentWarning = true;
            }

            if(canAddItem || !config.getBoolean("dropItems")) {
                if(canAddItem) inventory.addItem(item);
                event.setDrops(new Item[]{Item.get(0)});
            }
        }
    }
}
