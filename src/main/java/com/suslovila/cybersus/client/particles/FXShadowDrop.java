package com.suslovila.cybersus.client.particles;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.awt.*;
import java.util.ArrayDeque;

@SideOnly(Side.CLIENT)
public class FXShadowDrop extends FXBase {
    public static final ArrayDeque<FXBase> queuedDepthIgnoringRenders = new ArrayDeque<>();


    public static final ArrayDeque<FXBase> queuedRenders = new ArrayDeque<>();


    public static final ResourceLocation FXTexture = new ResourceLocation(Cybersus.MOD_ID, "textures/particles/shadow_drop.png");


    public FXShadowDrop(World world, double x, double y, double z, double mX, double mY, double mZ, int lifeTime, float particleSize, boolean depthTest) {
        super(world, x, y, z, mX, mY, mZ, lifeTime, particleSize, depthTest, FXTexture, true);
    }

    public void onUpdate() {
        super.onUpdate();
        //it seems it does not work because particles are black. I need to "play" with blend and alpha to find out the solution
        fadeOut();
    }


    @Override
    protected void injectRender(Tessellator tessellator) {
        SusGraphicHelper.bindColor(Color.black.getRGB(), 1.0f, 1.0f);
        super.injectRender(tessellator);
    }
    @Override
    public ArrayDeque<FXBase> getQueuedDepthIgnoringRenders() {
        return queuedDepthIgnoringRenders;
    }

    @Override
    public ArrayDeque<FXBase> getQueuedRenderers() {
        return queuedRenders;
    }


    private void fadeOut() {
        particleScale = maxParticleScale * (particleMaxAge - particleAge) / particleMaxAge;
        particleAlpha = (float) (particleMaxAge - particleAge) / particleMaxAge;
    }
}