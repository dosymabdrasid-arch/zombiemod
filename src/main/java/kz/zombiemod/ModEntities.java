package kz.zombiemod;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.EnumMap;
import java.util.Map;

public class ModEntities {
    public static final Map<Variant, EntityType<InfectedZombie>> TYPES = new EnumMap<>(Variant.class);

    public static void init() {
        for (Variant v : Variant.values()) {
            ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, ZombieMod.id(v.id));
            EntityType<InfectedZombie> type = EntityType.Builder
                    .<InfectedZombie>of((t, level) -> new InfectedZombie(t, level, v), MobCategory.MONSTER)
                    .sized(0.6f * v.scale, 1.95f * v.scale)
                    .clientTrackingRange(10)
                    .build(key);
            Registry.register(BuiltInRegistries.ENTITY_TYPE, key, type);
            TYPES.put(v, type);

            FabricDefaultAttributeRegistry.register(type, InfectedZombie.createAttributes(v));
            SpawnPlacements.register(type, SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules);
            BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), MobCategory.MONSTER,
                    type, v.weight, v.minGroup, v.maxGroup);
        }
    }
}
