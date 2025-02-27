package com.suslovila.cybersus.client.clientProcess.processes.shadowGates;

import com.suslovila.cybersus.api.process.ClientProcess;
import com.suslovila.cybersus.api.process.ISerializableProcess;
import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.client.particles.FXShadowDrop;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusUtils;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static com.suslovila.cybersus.utils.SusUtils.nextDouble;
import static org.apache.commons.lang3.RandomUtils.nextInt;

public class ProcessShadowGates extends ClientProcess implements ISerializableProcess {
    Random random = new Random();
    ConcurrentSet<ShadowTail> tails = new ConcurrentSet<>();
    public Integer ownerId = null;


    public ProcessShadowGates(SusVec3 vec3, int duration) {
        super(vec3, duration);
    }

    public ProcessShadowGates(Entity entity, SusVec3 vec3, int duration) {
        super(vec3, duration);
        this.ownerId = entity.getEntityId();
    }

    public ProcessShadowGates() {
    }

    @Override
    public void tickClient(TickEvent.ClientTickEvent event) {
        super.tickClient(event);
        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;


        for(int i = 0; i < 10; i++) {
            FXShadowDrop particle = new FXShadowDrop(
                    world,
                    position.x + SusUtils.nextDouble(-1.3, 1.3),
                    position.y - 1 + SusUtils.nextDouble(0, 0.1),
                    position.z + SusUtils.nextDouble(-1.3, 1.3),
                    0.0,
                    SusUtils.nextDouble(0.03, 0.09),
                    0.0,
                    40,
                    0.35f,
                    true
            );
            ParticleEngine.instance.addEffect(world, particle);
        }
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player != null && player.worldObj != null) {
            spawnShadowParticles(player.worldObj, 11, 10, 3, 0.3, 1.4, 0.5, 1.254D, -0.7, 0.7, 0, 0.8, -0.7, 0.7);
        }
    }


    @Override
    public void render(RenderWorldLastEvent event) {

        if(ownerId != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.worldObj != null) {
            Entity entity = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(ownerId);
            if (entity != null) {
                position = SusGraphicHelper.getRenderPos(entity, event.partialTicks);

            }
        }

        GL11.glPushMatrix();
        SusGraphicHelper.translateFromPlayerTo(this.position, event.partialTicks);
        SusGraphicHelper.makeSystemOrthToVectorAndHandle(
                new SusVec3(0.0, 1.0, 0.0),
                1.6,
                ()-> {
                    GL11.glPushAttrib(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    SusGraphicHelper.bindColor(Color.black.getRGB(), 0.85f, 1.0f);
                    SusGraphicHelper.bindTexture(FXShadowDrop.FXTexture);
                    SusGraphicHelper.drawFromCenter(3.0f);
                    SusGraphicHelper.setStandartColors();
                    GL11.glPopAttrib();
                }
        );
        GL11.glPopMatrix();
    }

    @Override
    public String getTypeId() {
        return "shadow_gates";
    }

    private void spawnKharuParticles(
            EntityPlayer player,
            World world,
            int maxTailsAmount,
            int minTailSpeed,
            int maxTailSpeed,
            int deleteAndAddTailChanceFactor,
            double minRadius,
            double maxRadius,
            double radiusChangeMinSpeed,
            double radiusChangeMaxSpeed,
            int particleLifeTime,
            float particleSize
    ) {

        for (int hl = 0; hl < deleteAndAddTailChanceFactor; hl++) {
            if (random.nextInt(50) == 37 && tails.size() < maxTailsAmount) {
                tails.add(
                        new ShadowTail(
                                SusVec3.getEntityPos(player).add(0.5, 0.5, 0.5),
                                nextInt(minTailSpeed, maxTailSpeed),
                                nextDouble(minRadius, maxRadius),
                                nextDouble(radiusChangeMinSpeed, radiusChangeMaxSpeed),
                                new SusVec3(
                                        nextDouble(0.0, 1.0) * (random.nextInt(2) == 1 ? 1 : -1),
                                        nextDouble(0.0, 1.0) * (random.nextInt(2) == 1 ? 1 : -1),
                                        nextDouble(0.0, 1.0) * (random.nextInt(2) == 1 ? 1 : -1)
                                ).normalize()
                        )
                );
            }
        }

        for (ShadowTail shadowTail : tails) {
            int h = 0;
            while (h < shadowTail.tailSpeed && tails.contains(shadowTail)) {
                SusVec3 m = SusVec3.getOrthogonalVec3(shadowTail.aimVec3).scale(shadowTail.getActualRadius());
                SusVec3 k = shadowTail.aimVec3.cross(m).normalize().scale(shadowTail.getActualRadius());
                SusVec3 offsetFromHome = m.scale(Math.sin(shadowTail.timer * Math.PI / 90))
                        .add(k.scale(Math.cos(shadowTail.timer * Math.PI / 90)));
                shadowTail.timer++;
                SusVec3 particlePosition = shadowTail.homePos.add(offsetFromHome);
                FXShadowDrop particle = new FXShadowDrop(
                        world,
                        particlePosition.x,
                        particlePosition.y,
                        particlePosition.z,
                        0.0,
                        0.0,
                        0.0,
                        particleLifeTime,
                        particleSize,
                        true
                );
                ParticleEngine.instance.addEffect(world, particle);
                if (random.nextInt(300) == 37 && tails.size() >= maxTailsAmount) {
                    tails.remove(shadowTail);
                }
                h++;
            }
        }
    }

    protected void spawnShadowParticles(World world, int maxShadowsAmount, int speed, int wholeIterationAmount, double minRadius, double maxRadius, double minParticleSize, double maxParticleSize, double minXOffset, double maxXOffset, double minYOffset, double maxYOffset, double minZOffset, double maxZOffset) {
        for (int hl = 0; hl < wholeIterationAmount; hl++) {
            if (tails.size() < maxShadowsAmount) {
                double radius = nextDouble(minRadius, maxRadius);
                int timer = 0;
                SusVec3 lookVector = SusVec3.randomVector();
                SusVec3 lookVectorNormal = new SusVec3(lookVector.x + nextDouble(-2, 2), 0, lookVector.z + nextDouble(-2, 2));
                SusVec3 m = new SusVec3(lookVectorNormal.z, 0, -lookVectorNormal.x);
                m = m.normalize();
                SusVec3 k = new SusVec3(0, -1, 0);
                tails.add(new ShadowTail(
                        new SusVec3(
                                position.x + nextDouble(minXOffset, maxXOffset),
                                position.y - 0.3 + nextDouble(minYOffset, maxYOffset),
                                position.z + nextDouble(minZOffset, maxZOffset)
                        ),
                        radius,
                        m,
                        k,
                        nextDouble(minParticleSize, maxParticleSize),
                        timer
                ));
            }
        }
        Iterator<ShadowTail> iterator = tails.iterator();
        while (iterator.hasNext()) {
            ShadowTail shadowTail = iterator.next();
            double radius = shadowTail.radius;
            SusVec3 m = (shadowTail.m).scale(radius);
            SusVec3 k = (shadowTail.k).scale(radius);
            double particleSize = shadowTail.particleSize;
            for (int h = 0; h < speed; h++) {
                int timer = shadowTail.timer;
                if (timer % 65 == 0 && timer != 0 && random.nextBoolean()) {

                    double newRadius = nextDouble(minRadius, maxRadius);
                    double chis = k.y * Math.cos(timer * Math.PI / 65);
                    SusVec3 newDotInSpace = new SusVec3(shadowTail.homePos.x, shadowTail.homePos.y + chis / Math.abs(chis) * (newRadius + radius), shadowTail.homePos.z);
                    SusVec3 newK = new SusVec3(0, -k.y, 0);
                    shadowTail.homePos = newDotInSpace;
                    shadowTail.radius = newRadius;
                    shadowTail.m = m.normalize();
                    shadowTail.k = newK.normalize();
                    break;
                }
                SusVec3 a = m.scale(Math.sin(timer * Math.PI / 65)).add(k.scale(Math.cos(timer * Math.PI / 65)));
                shadowTail.timer += 1;
                SusVec3 endPosition = shadowTail.homePos.add(a);

                FXShadowDrop particle = new FXShadowDrop(
                        world,
                        endPosition.x,
                        endPosition.y,
                        endPosition.z,
                        0.0,
                        0.0,
                        0.0,
                        17,
                        (float) particleSize,
                        true
                );
                ParticleEngine.instance.addEffect(world, particle);

                if (random.nextInt(300) == 37) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    @Override
    public void writeTo(ByteBuf buf) {
        super.writeTo(buf);
        buf.writeBoolean(ownerId != null);
        if(ownerId != null) {
            buf.writeInt(ownerId);
        }
    }
    @Override
    public void readFrom(ByteBuf buf) {
        super.readFrom(buf);
        if(buf.readBoolean()) {
            ownerId = buf.readInt();
        }

    }

}
