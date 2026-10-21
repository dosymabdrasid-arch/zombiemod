package kz.zombiemod;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Supplier;

/** Огнестрел: хитскан-выстрел, расходует патроны из инвентаря. */
public class GunItem extends Item {
    private final Supplier<Item> ammo;
    private final float damage;
    private final int pellets;
    private final double spread;
    private final int cooldown;
    private final double range;
    private final SoundEvent sound;
    private final float pitch;

    public GunItem(Properties props, Supplier<Item> ammo, float damage, int pellets,
                   double spread, int cooldown, double range, SoundEvent sound, float pitch) {
        super(props);
        this.ammo = ammo; this.damage = damage; this.pellets = pellets; this.spread = spread;
        this.cooldown = cooldown; this.range = range; this.sound = sound; this.pitch = pitch;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack gun = player.getItemInHand(hand);
        if (!(level instanceof ServerLevel server)) {
            return InteractionResult.SUCCESS;
        }
        if (!player.hasInfiniteMaterials() && !takeAmmo(player)) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    net.minecraft.sounds.SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1.0f, 1.5f);
            return InteractionResult.FAIL;
        }

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle();
        for (int i = 0; i < pellets; i++) {
            Vec3 dir = look.add(
                    (player.getRandom().nextDouble() - 0.5) * 2 * spread,
                    (player.getRandom().nextDouble() - 0.5) * 2 * spread,
                    (player.getRandom().nextDouble() - 0.5) * 2 * spread).normalize();
            fire(server, player, eye, dir);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                sound, SoundSource.PLAYERS, 1.2f, pitch);
        Vec3 muzzle = eye.add(look.scale(0.8));
        server.sendParticles(ParticleTypes.SMOKE, muzzle.x, muzzle.y - 0.1, muzzle.z, 3, 0.03, 0.03, 0.03, 0.01);
        server.sendParticles(ParticleTypes.FLAME, muzzle.x, muzzle.y - 0.1, muzzle.z, 1, 0.0, 0.0, 0.0, 0.01);
        player.getCooldowns().addCooldown(gun, cooldown);
        return InteractionResult.SUCCESS;
    }

    private void fire(ServerLevel server, Player player, Vec3 eye, Vec3 dir) {
        Vec3 end = eye.add(dir.scale(range));
        BlockHitResult block = server.clip(new ClipContext(eye, end,
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 limit = block.getType() == HitResult.Type.MISS ? end : block.getLocation();

        Entity best = null;
        Vec3 bestPos = limit;
        double bestDist = eye.distanceToSqr(limit);
        AABB search = new AABB(eye, limit).inflate(1.0);
        for (Entity e : server.getEntities(player, search,
                en -> en instanceof LivingEntity && en.isAlive() && !en.isSpectator())) {
            Optional<Vec3> hit = e.getBoundingBox().inflate(0.15).clip(eye, limit);
            if (hit.isPresent()) {
                double d = eye.distanceToSqr(hit.get());
                if (d < bestDist) { bestDist = d; best = e; bestPos = hit.get(); }
            }
        }
        if (best != null) {
            best.hurtServer(server, server.damageSources().playerAttack(player), damage);
            server.sendParticles(ParticleTypes.CRIT, bestPos.x, bestPos.y, bestPos.z, 4, 0.1, 0.1, 0.1, 0.1);
        }
        // трассер
        double len = Math.sqrt(bestDist);
        for (double t = 1.5; t < len; t += 1.5) {
            Vec3 p = eye.add(dir.scale(t));
            server.sendParticles(ParticleTypes.CRIT, p.x, p.y, p.z, 1, 0, 0, 0, 0);
        }
    }

    private boolean takeAmmo(Player player) {
        Item need = ammo.get();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(need)) { s.shrink(1); return true; }
        }
        return false;
    }
}
