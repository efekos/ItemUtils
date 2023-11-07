package dev.efekos.iu.events;

import dev.efekos.iu.Main;
import dev.efekos.iu.util.ItemRarity;
import dev.efekos.iu.util.ListeningCache;
import net.minecraft.world.item.EnumItemRarity;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftItem;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EntityEvents implements Listener {

    private static final Map<UUID, ListeningCache> caches = new HashMap<>();

    private void add2Cache(Item item){
        caches.put(item.getUniqueId(),new ListeningCache(item.getWorld().getUID(),item.getItemStack().getAmount()));
    }

    private void markGlowingYellow(Item item){
        if(!caches.containsKey(item.getUniqueId())) add2Cache(item);
        ListeningCache cache = caches.get(item.getUniqueId());

        cache.setYellowGlowed(true);

        caches.put(item.getUniqueId(),cache);
    }
    private void markGlowingPurple(Item item){
        if(!caches.containsKey(item.getUniqueId())) add2Cache(item);
        ListeningCache cache = caches.get(item.getUniqueId());

        cache.setPurpleGlowed(true);

        caches.put(item.getUniqueId(),cache);
    }
    private void markGlowingAqua(Item item){
        if(!caches.containsKey(item.getUniqueId())) add2Cache(item);
        ListeningCache cache = caches.get(item.getUniqueId());

        cache.setAquaGlowed(true);

        caches.put(item.getUniqueId(),cache);
    }

    public final BukkitRunnable TICK = new BukkitRunnable() {
        @Override
        public void run() {
            List<UUID> removals = new ArrayList<>();

            caches.forEach((uuid, cache) -> {
                World world = Main.getInstance().getServer().getWorld(cache.getWorldId());
                Optional<Entity> entity = world.getEntities().stream().filter(r -> r.getUniqueId().equals(uuid)).findFirst();
                if(!entity.isPresent()){
                    removals.add(uuid);
                } else {
                    Item item = ((Item) entity.get());

                    if(cache.isAquaGlowed())world.spawnParticle(Particle.REDSTONE,item.getLocation().add(0,0.25,0),1,.2,.2,.2,new Particle.DustOptions(Color.AQUA,0.5F));
                    if(cache.isYellowGlowed())world.spawnParticle(Particle.REDSTONE,item.getLocation().add(0,0.25,0),1,.2,.2,.2,new Particle.DustOptions(Color.YELLOW,0.5F));
                    if(cache.isPurpleGlowed())world.spawnParticle(Particle.REDSTONE,item.getLocation().add(0,0.25,0),1,.2,.2,.2,new Particle.DustOptions(Color.PURPLE,0.5F));

                    if(item.getItemStack().getAmount()!=cache.getLastCount()||getAge(item)>5500){
                        cache.setLastCount(item.getItemStack().getAmount());
                        updateText(item);
                    }
                }
            });

            removals.forEach(caches::remove);
        }
    };

    private int getAge(Item item){
        return ((CraftItem) item).getHandle().g;
    }

    private ItemRarity updateText(Item item){
        CraftItem craftItem = (CraftItem) item;

        ItemStack stack = item.getItemStack();

        List<String> lines = new ArrayList<>();


        net.minecraft.world.item.ItemStack craftCopy = CraftItemStack.asNMSCopy(stack);

        ItemRarity rarity = toUnderstandable(craftCopy.C()); // ItemStacks and Items have a method called .getRarity(), obfuscated to .C() in here

        int age = getAge(item);
        int i = 17;
        if(age>5500) i-=5;
        if(age>5600) i-=4;
        if(age>5700) i-=3;
        if(age>5800) i-=2;
        if(age>5900) i-=1;
        boolean b = age > 5500&&age%i==0;

        if(stack.hasItemMeta()&&stack.getItemMeta().hasDisplayName()){
            lines.add("{\"text\":\""+stack.getItemMeta().getDisplayName()+"\"}");
        } else lines.add("{\"translate\":\""+stack.getTranslationKey()+"\",\"color\":\""+(b?"red":rarity.getChatColor().asBungee().getName())+"\"}");
        // used .asBungee() because bukkit ChatColors doesn't contain the name while bungee colors do

        lines.add("{\"text\":\" \"}");
        lines.add("{\"text\":\"x"+stack.getAmount()+"\",\"color\":\""+(b?"dark_red":"gray")+"\"}");

        // I have to use this because bukkit item doesn't support TextComponents.
        craftItem.getHandle().b(CraftChatMessage.fromJSON("["+String.join(",",lines)+"]"));

        return rarity;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e){
        if(e.getEntityType() != EntityType.DROPPED_ITEM)return;

        Item item = (Item) e.getEntity();

        if(item.isUnlimitedLifetime())return;

        item.setCustomNameVisible(true);

        add2Cache(item);

        switch (updateText(item)){ // With this way, I don't have to store the rarity or get it again.
            case UNCOMMON: markGlowingYellow(item);break;
            case RARE: markGlowingAqua(item);break;
            case EPIC: markGlowingPurple(item);break;
        }

    }

    private static ItemRarity toUnderstandable(EnumItemRarity rarity){
        switch (rarity){
            case b: return ItemRarity.UNCOMMON;
            case c: return ItemRarity.RARE;
            case d: return ItemRarity.EPIC;
            default: return ItemRarity.COMMON;
        }
    }
}
