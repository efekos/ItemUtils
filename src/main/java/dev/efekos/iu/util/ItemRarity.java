package dev.efekos.iu.util;

import org.bukkit.ChatColor;

public enum ItemRarity {
    COMMON("common", ChatColor.WHITE),
    UNCOMMON("uncommon", ChatColor.YELLOW),
    RARE("rare", ChatColor.AQUA),
    EPIC("epic", ChatColor.LIGHT_PURPLE);

    private final ChatColor chatColor;
    private final String id;

    ItemRarity(String id, ChatColor chatColor) {
        this.chatColor = chatColor;
        this.id = id;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id;
    }
}
