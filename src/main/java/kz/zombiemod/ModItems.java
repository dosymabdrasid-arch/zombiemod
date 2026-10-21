package kz.zombiemod;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class ModItems {
    public static Item BULLET, SHELL, BANDAGE, BAT, MACHETE, PISTOL, SHOTGUN, RIFLE;

    private static <T extends Item> T reg(String name, Item.Properties p, Function<Item.Properties, T> f) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ZombieMod.id(name));
        return Registry.register(BuiltInRegistries.ITEM, key, f.apply(p.setId(key)));
    }

    public static void init() {
        BULLET = reg("bullet", new Item.Properties(), Item::new);
        SHELL = reg("shell", new Item.Properties(), Item::new);
        BANDAGE = reg("bandage", new Item.Properties().stacksTo(16), p -> new Item(p) {
            @Override
            public InteractionResult use(Level level, Player player, InteractionHand hand) {
                ItemStack stack = player.getItemInHand(hand);
                if (player.getHealth() >= player.getMaxHealth()) return InteractionResult.PASS;
                if (!level.isClientSide()) {
                    player.heal(8.0f);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.WOOL_PLACE, net.minecraft.sounds.SoundSource.PLAYERS, 1f, 1f);
                    if (!player.hasInfiniteMaterials()) stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        });

        BAT = reg("bat", new Item.Properties().sword(ToolMaterial.WOOD, 5.0f, -2.6f), Item::new);
        MACHETE = reg("machete", new Item.Properties().sword(ToolMaterial.IRON, 4.0f, -1.8f), Item::new);

        PISTOL = reg("pistol", new Item.Properties().stacksTo(1),
                p -> new GunItem(p, () -> BULLET, 7.0f, 1, 0.01, 8, 60, SoundEvents.FIREWORK_ROCKET_BLAST, 1.6f));
        SHOTGUN = reg("shotgun", new Item.Properties().stacksTo(1),
                p -> new GunItem(p, () -> SHELL, 4.0f, 8, 0.07, 30, 22, SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 0.7f));
        RIFLE = reg("rifle", new Item.Properties().stacksTo(1),
                p -> new GunItem(p, () -> BULLET, 5.0f, 1, 0.02, 4, 80, SoundEvents.FIREWORK_ROCKET_BLAST, 1.2f));

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.accept(BAT); entries.accept(MACHETE);
            entries.accept(PISTOL); entries.accept(SHOTGUN); entries.accept(RIFLE);
            entries.accept(BULLET); entries.accept(SHELL); entries.accept(BANDAGE);
        });
    }
}
