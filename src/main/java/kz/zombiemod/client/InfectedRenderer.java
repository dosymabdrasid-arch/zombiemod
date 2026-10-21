package kz.zombiemod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import kz.zombiemod.ZombieMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

/** Ванильная 3D-модель зомби + своя текстура и масштаб для каждого мутанта. */
public class InfectedRenderer extends ZombieRenderer {
    private final Identifier texture;
    private final float scale;

    public InfectedRenderer(EntityRendererProvider.Context ctx, String name, float scale) {
        super(ctx);
        this.texture = ZombieMod.id("textures/entity/" + name + ".png");
        this.scale = scale;
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return texture;
    }

    @Override
    protected void scale(ZombieRenderState state, PoseStack poseStack) {
        super.scale(state, poseStack);
        poseStack.scale(scale, scale, scale);
    }
}
