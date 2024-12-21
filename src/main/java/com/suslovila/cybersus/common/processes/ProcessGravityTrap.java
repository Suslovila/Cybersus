package com.suslovila.cybersus.common.processes;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.process.CommonProcess;
import com.suslovila.cybersus.client.ResourceLocationPreLoad;
import com.suslovila.cybersus.client.particles.FXGravity;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusUtils;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;

import java.awt.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class ProcessGravityTrap extends CommonProcess {
    int angle = Cybersus.random.nextInt(360);
    float captureRadius = 10.0f;
    int appearTimeTicks = 3 * 20;

    public ProcessGravityTrap(SusVec3 vec3, int duration) {
        super(vec3, duration);
    }

    public static final ResourceLocationPreLoad textureInner = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/processes/gravity_trap_inner.png");
    public static final ResourceLocationPreLoad textureOuter = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/processes/gravity_trap_outer.png");

    public ProcessGravityTrap() {
    }

    @Override
    public boolean isExpired(TickEvent.WorldTickEvent event) {
        return super.isExpired(event);
    }

    @Override
    public boolean isExpired(TickEvent.ClientTickEvent event) {
        return super.isExpired(event);
    }

    @Override
    public void tickServer(TickEvent.WorldTickEvent event) {

        super.tickServer(event);

        if (!isActivated()) return;

        List<Entity> entities = event.world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(x - captureRadius, y, z - captureRadius, x + captureRadius, y + captureRadius * 2, z + captureRadius));
        for (Entity entity : entities) {
            if (entity instanceof EntityPlayer) {
                entity.motionX = 0;
                entity.motionZ = 0;
                entity.motionY = -2;
                entity.velocityChanged = true;

                entity.setPosition(entity.lastTickPosX, entity.posY, entity.lastTickPosZ);
            } else {
                if(!(entity instanceof IProjectile)) {
                    entity.motionX = 0;
                    entity.motionZ = 0;
                    entity.motionY = -0.3;
                    entity.setPosition(entity.lastTickPosX, entity.posY, entity.lastTickPosZ);
                }
                else {
                    entity.motionY = Math.max(-2, entity.motionY - 0.3);
                }
                entity.velocityChanged = true;

            }
        }
    }

    @Override
    public void tickClient(TickEvent.ClientTickEvent event) {
        super.tickClient(event);

        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;

        if(isActivated()) {
            for (int i = 0; i < 6; i++) {
                FXGravity gravity = new FXGravity(
                        world,
                        this.x + SusUtils.nextDouble(-captureRadius, captureRadius),
                        this.y + SusUtils.nextDouble(0, captureRadius * 2),
                        this.z + SusUtils.nextDouble(-captureRadius, captureRadius),
                        0.0,
                        -2.5,
                        0.0,
                        10,
                        0.8f,
                        true
                );
                ParticleEngine.instance.addEffect(Minecraft.getMinecraft().theWorld, gravity);
            }
        }
    }

    @Override
    public void render(RenderWorldLastEvent event) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7f);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

//        SusVec3 lookVector = new SusVec3(x, y, z).subtract(SusGraphicHelper.getRenderPos(player, event.partialTicks).subtract(0.0, player.eyeHeight, 0.0));
        SusVec3 lookVector = new SusVec3(0.0, 1.0, 0.0);
        GL11.glPushMatrix();
        SusGraphicHelper.translateFromPlayerTo(new SusVec3(x, y, z), event.partialTicks);
        SusGraphicHelper.makeSystemOrthToVectorAndHandle(lookVector, -0.1, () -> {
            glPushAttrib(GL_BLEND);
            glPushAttrib(GL_LIGHTING);
            glPushAttrib(GL_CULL_FACE);

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisable(GL_CULL_FACE);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            Color color = new Color(229, 9, 9);

            SusGraphicHelper.bindColor(color.getRGB(), 0.6f, 1.0f);
            double angle = SusGraphicHelper.getRenderGlobalTime(event.partialTicks) % 360;
            double scale = 1.5 * Math.min(1.0, (totalDuration - timeLeft + event.partialTicks) / ((double) appearTimeTicks + event.partialTicks));
            glScaled(scale, scale, scale);

            GL11.glPushMatrix();
            SusGraphicHelper.bindTexture(textureInner);
            GL11.glRotated(angle, 0.0, 0.0, 1.0);
            SusGraphicHelper.drawFromCenter(10.0f);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            SusGraphicHelper.bindTexture(textureOuter);
            GL11.glRotated(-angle + 30, 0.0, 0.0, 1.0);
            SusGraphicHelper.drawFromCenter(10.0f);
            GL11.glPopMatrix();

            glPopAttrib();
            glPopAttrib();
            glPopAttrib();
        });
        GL11.glPopMatrix();
    }

    public boolean isPlayerInside(EntityPlayer player) {
        return AxisAlignedBB.getBoundingBox(x - captureRadius, y, z - captureRadius, x + captureRadius, y + captureRadius * 2, z + captureRadius)
                .isVecInside(Vec3.createVectorHelper(player.posX, player.posY, player.posZ));
    }

    public boolean isActivated() {
        return (totalDuration - timeLeft) >= appearTimeTicks;
    }

    @Override
    public String getTypeId() {
        return "gravity_trap";
    }


}
