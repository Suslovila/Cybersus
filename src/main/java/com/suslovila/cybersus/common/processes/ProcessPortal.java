package com.suslovila.cybersus.common.processes;

import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.process.CommonProcess;
import com.suslovila.cybersus.client.TextureStorage;
import com.suslovila.cybersus.client.particles.FXGravity;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusUtils;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusWorldHelper;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;

import java.awt.*;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

public class ProcessPortal extends CommonProcess {

    public static final String destinationDimensionIdKey = Cybersus.prefixAppender.doAndGet("destinationDimensionId");
    public static final String destinationPosKey = Cybersus.prefixAppender.doAndGet("destinationPos");

    public static final int appearTimeTicks = 20 * 3;
    public static final Random random = new Random();
    public static double captureRadius = 0.1;
    public int destinationDimensionId;
    public SusVec3 destinationPos;

    public ProcessPortal(SusVec3 position, int duration, int destinationDimensionId, SusVec3 destinationPos) {
        super(position, duration);
        this.destinationDimensionId = destinationDimensionId;
        this.destinationPos = destinationPos;
    }


    // we must have empty constructor for instantiation!!!!!!!!
    public ProcessPortal() {
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

        if (!isActivated() || timeLeft % 8 != 0) return;

        List<Entity> entities = event.world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(position.x - captureRadius, position.y, position.z - captureRadius, position.x + captureRadius, position.y + captureRadius * 4, position.z + captureRadius));
        for (Entity entity : entities) {
            SusVec3 delta = new SusVec3(random.nextInt(), 0.0, random.nextInt()).normalize();
            SusWorldHelper.teleportToLocation(event.world, destinationPos.x + delta.x, destinationPos.y + delta.y, destinationPos.z + delta.z, destinationDimensionId, entity, true);

        }
    }


    @Override
    public void tickClient(TickEvent.ClientTickEvent event) {
        super.tickClient(event);

        World world = Minecraft.getMinecraft().theWorld;
        if (world == null) return;

        if (isActivated()) {
            for (int i = 0; i < 6; i++) {
                FXGravity gravity = new FXGravity(
                        world,
                        this.position.x + SusUtils.nextDouble(-captureRadius, captureRadius),
                        this.position.y + SusUtils.nextDouble(0, captureRadius * 4),
                        this.position.z + SusUtils.nextDouble(-captureRadius, captureRadius),
                        0.0,
                        -2.5,
                        0.0,
                        (int) captureRadius,
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
        SusGraphicHelper.translateFromPlayerTo(this.position, event.partialTicks);
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
            SusGraphicHelper.bindTexture(TextureStorage.textureInner);
            GL11.glRotated(angle, 0.0, 0.0, 1.0);
            SusGraphicHelper.drawFromCenter(captureRadius);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            SusGraphicHelper.bindTexture(TextureStorage.textureOuter);
            GL11.glRotated(-angle + 30, 0.0, 0.0, 1.0);
            SusGraphicHelper.drawFromCenter(captureRadius);
            GL11.glPopMatrix();

            glPopAttrib();
            glPopAttrib();
            glPopAttrib();
        });
        GL11.glPopMatrix();
    }

    public boolean isPlayerInside(EntityPlayer player) {
        return AxisAlignedBB.getBoundingBox(this.position.x - captureRadius, position.y, position.z - captureRadius, position.x + captureRadius, position.y + captureRadius * 4, position.z + captureRadius)
                .isVecInside(Vec3.createVectorHelper(player.posX, player.posY, player.posZ));
    }

    public boolean isActivated() {
        return (totalDuration - timeLeft) >= appearTimeTicks;
    }

    @Override
    public String getTypeId() {
        return "portal";
    }


    @Override
    public void writeTo(ByteBuf buf) {
        super.writeTo(buf);
        buf.writeInt(destinationDimensionId);
        destinationPos.writeTo(buf);

    }

    @Override
    public void readFrom(ByteBuf buf) {
        super.readFrom(buf);
        destinationDimensionId = buf.readInt();
        destinationPos = SusVec3.readFrom(buf);
    }


    @Override
    public void writeTo(NBTTagCompound rootTag) {
        super.writeTo(rootTag);
        rootTag.setInteger(destinationDimensionIdKey, destinationDimensionId);
        NBTTagCompound tagForPos = new NBTTagCompound();
        destinationPos.writeTo(tagForPos);
        rootTag.setTag(destinationPosKey, tagForPos);

    }

    @Override
    public void readFrom(NBTTagCompound rootTag) {
        super.readFrom(rootTag);
        destinationDimensionId = rootTag.getInteger(destinationDimensionIdKey);
        NBTTagCompound tagForPos = rootTag.getCompoundTag(destinationPosKey);
        destinationPos = SusVec3.readFrom(tagForPos);
    }


}
