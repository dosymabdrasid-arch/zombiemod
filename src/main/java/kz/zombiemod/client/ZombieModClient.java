package kz.zombiemod.client;

import kz.zombiemod.ModEntities;
import kz.zombiemod.Variant;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ZombieModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Variant v : Variant.values()) {
            EntityRendererRegistry.register(ModEntities.TYPES.get(v),
                    ctx -> new InfectedRenderer(ctx, v.id, v.scale));
        }
    }
}
