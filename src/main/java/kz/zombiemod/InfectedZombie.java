package kz.zombiemod;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;

public class InfectedZombie extends Zombie {
    private final Variant variant;

    public InfectedZombie(EntityType<? extends Zombie> type, Level level, Variant variant) {
        super(type, level);
        this.variant = variant;
    }

    public static AttributeSupplier.Builder createAttributes(Variant v) {
        return Zombie.createAttributes()
                .add(Attributes.MAX_HEALTH, v.health)
                .add(Attributes.MOVEMENT_SPEED, v.speed)
                .add(Attributes.ATTACK_DAMAGE, v.damage)
                .add(Attributes.KNOCKBACK_RESISTANCE, v.kbResist)
                .add(Attributes.FOLLOW_RANGE, 40.0);
    }

    @Override
    protected boolean isSunSensitive() {
        return false; // заражённые не горят на солнце
    }

    @Override
    public boolean isBaby() {
        return false; // без «детей» — у мутантов другие размеры
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && variant.poison && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 0), this);
        }
        return hit;
    }
}
