package org.windy.removeitemright;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class RemoveItemRight extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("lang.yml", false);

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("DisableShiftRightClickPlugin 已启用!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DisableShiftRightClickPlugin 已禁用!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 检查事件是否由Shift + 右键触发
        if (event.useInteractedBlock() != org.bukkit.event.Event.Result.DENY &&
                event.useItemInHand() != org.bukkit.event.Event.Result.DENY &&
                !event.getAction().name().contains("RIGHT_CLICK")) {
            return;
        }

        // 检查玩家手持物品是否为配置文件中指定的禁止物品类型
        ItemStack heldItem = event.getItem();
        if (heldItem != null && isItemDisabled(heldItem.getType())) {
            event.setUseItemInHand(org.bukkit.event.Event.Result.DENY);
            event.setCancelled(true);
            String message = getConfig().getString("messages.cannotUseShiftRightClick");
            if (message != null) {
                event.getPlayer().sendMessage(message);
            }
        }
    }

    private boolean isItemDisabled(Material itemType) {
        FileConfiguration config = getConfig();
        return config.getStringList("disabledItems").contains(itemType.name());
    }
}