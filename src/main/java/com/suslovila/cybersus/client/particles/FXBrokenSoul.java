package com.suslovila.cybersus.client.particles;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;

import static com.suslovila.cybersus.utils.SusGraphicHelper.*;

@SideOnly(Side.CLIENT)
public class FXBrokenSoul extends FXBase {
    public static final ArrayDeque<FXBase> queuedDepthIgnoringRenders = new ArrayDeque<>();


    public static final ArrayDeque<FXBase> queuedRenders = new ArrayDeque<>();


    public static final ResourceLocation texture = new ResourceLocation(Cybersus.MOD_ID, "textures/particles/particleBrokenSoul.png");


    public FXBrokenSoul(World world, double x, double y, double z, double mX, double mY, double mZ, int lifeTime, float particleSize, boolean depthTest) {
        super(world, x, y, z, mX, mY, mZ, lifeTime, particleSize, depthTest, texture, true);
    }

    public void onUpdate() {
        super.onUpdate();
        //it seems it does not work because particles are black. I need to "play" with blend and alpha to find out the solution
        fadeOut();
    }

    @Override
    public ArrayDeque<FXBase> getQueuedDepthIgnoringRenders() {
        return queuedDepthIgnoringRenders;
    }

    @Override
    public ArrayDeque<FXBase> getQueuedRenderers() {
        return queuedRenders;
    }

    @Override
    protected void injectRender(Tessellator tessellator) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7f);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player == null) return;

        SusVec3 lookVector = new SusVec3(posX, posY, posZ).subtract(SusGraphicHelper.getRenderPos(player, partialTicks));
        GL11.glPushMatrix();
        SusGraphicHelper.translateFromPlayerTo(getPos(), partialTicks);
        SusGraphicHelper.makeSystemOrthToVectorAndHandle(lookVector, 0.0, () -> {
            SusGraphicHelper.bindTexture(texture);

            double radius = 1.0f;
            tessellator.addVertexWithUV(-radius, radius, 0.0, u2, v2);
            tessellator.addVertexWithUV(radius, radius, 0.0, u2, v1);
            tessellator.addVertexWithUV(radius, -radius, 0.0, u1, v1);
            tessellator.addVertexWithUV(-radius, -radius, 0.0, u1, v2);

        });
        GL11.glPopMatrix();
    }

    private void fadeOut() {
        particleScale = maxParticleScale * (particleMaxAge - particleAge) / particleMaxAge;
        particleAlpha = (float) (particleMaxAge - particleAge) / particleMaxAge;
    }
}