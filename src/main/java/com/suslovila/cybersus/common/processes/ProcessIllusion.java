package com.suslovila.cybersus.common.processes;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.process.ClientProcess;
import com.suslovila.cybersus.api.process.CommonProcess;
import com.suslovila.cybersus.api.process.ISerializableProcess;
import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.client.particles.FXGravity;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusUtils;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class ProcessIllusion extends WorldProcess implements ISerializableProcess {

    int duration;
    int timeLeft;
    int illusionAmount = 7;
    int victimId;
    int hackerId;

    public ProcessIllusion(EntityPlayer hacker, EntityPlayer victim, int duration) {
        this.duration = duration;
        this.hackerId = hacker.getEntityId();
        this.victimId = victim.getEntityId();
        this.timeLeft = duration;
    }

    public ProcessIllusion() {
    }

    @Override
    public boolean isExpired(TickEvent.WorldTickEvent event) {
        return timeLeft <= 0;
    }

    @Override
    public boolean isExpired(TickEvent.ClientTickEvent event) {
        return timeLeft <= 0;
    }

    @Override
    public void tickServer(TickEvent.WorldTickEvent event) {
        timeLeft -= 1;
        super.tickServer(event);

    }

    @Override
    public void tickClient(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            timeLeft -= 1;
        }
        super.tickClient(event);


    }

    @Override
    public void render(RenderWorldLastEvent event) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7f);
        EntityPlayer victim = Minecraft.getMinecraft().thePlayer;
        if (victim == null) return;
        if (victim.getEntityId() == this.victimId && victim.worldObj.getEntityByID(this.hackerId) instanceof EntityPlayer) {
            EntityPlayer hacker = (EntityPlayer) victim.worldObj.getEntityByID(this.hackerId);
//        SusVec3 lookVector = new SusVec3(x, y, z).subtract(SusGraphicHelper.getRenderPos(player, event.partialTicks).subtract(0.0, player.eyeHeight, 0.0));
            glPushMatrix();
            // SusGraphicHelper.translateFromPlayerTo(hacker.getPosition(), event.partialTicks);
            SusVec3 vecFromPlayerToHacker = SusGraphicHelper.getRenderPos(hacker, event.partialTicks).subtract(SusGraphicHelper.getRenderPos(victim, event.partialTicks));
            SusVec3 XZProjection = new SusVec3(vecFromPlayerToHacker.x, 0.0, vecFromPlayerToHacker.z);
//            SusVec3 zVec3 = new SusVec3(0, 0, 1.0);
//            double angle = (SusVec3.angleBetweenVec3(zVec3, vecFromPlayerToHacker) * 180.0 / Math.PI) * (vecFromPlayerToHacker.x > 0 ? 1 : -1);
//            System.out.println("the angle is: " + angle);
//            glRotated(angle, 0.0, 1.0, 0.0);
            SusGraphicHelper.makeSystemOrthToVector(XZProjection, 0.0);
                SusGraphicHelper.drawGuideArrows();
                glTranslatef(0.0f, (float) (vecFromPlayerToHacker.y), 0.0f);
                double perCheckAngleDelta = 360.0 / (illusionAmount + 1);
                for (int i = 0; i < illusionAmount; i++) {
                    glPushMatrix();
                    glRotated(perCheckAngleDelta * (i + 1), 0.0, 1.0, 0.0);
                    glTranslated(0.0, 0.0, XZProjection.length());
                    glRotated(90.0, 0.0, 1.0, 0.0);
                    SusGraphicHelper.drawGuideArrows();
                    drawEntityOrthogonalToZAxis(hacker);
                    glPopMatrix();
            }

            glPopMatrix();
        }
    }


    @Override
    public String getTypeId() {
        return "illusion";
    }

    public void drawEntityOrthogonalToZAxis(
            EntityLivingBase entity
    ) {
        glPushAttrib(GL_COLOR_MATERIAL);
        glEnable(GL_COLOR_MATERIAL);
        glPushMatrix();
        RenderHelper.enableStandardItemLighting();
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        glPopMatrix();
        glPopAttrib();
    }

    @Override
    public void writeTo(ByteBuf byteBuf) {

        byteBuf.writeInt(hackerId);
        byteBuf.writeInt(victimId);
        byteBuf.writeInt(duration);
        byteBuf.writeInt(illusionAmount);
        byteBuf.writeInt(timeLeft);
    }

    @Override
    public void readFrom(ByteBuf byteBuf) {
        hackerId = byteBuf.readInt();
        victimId = byteBuf.readInt();
        duration = byteBuf.readInt();
        illusionAmount = byteBuf.readInt();
        timeLeft = byteBuf.readInt();

    }
}
