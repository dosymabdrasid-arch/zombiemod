package kz.zombiemod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class ZombieMod implements ModInitializer {
    public static final String MOD_ID = "zombiemod";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModItems.init();
        ModEntities.init();
    }
}
