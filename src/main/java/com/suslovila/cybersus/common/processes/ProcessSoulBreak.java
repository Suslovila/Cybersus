package com.suslovila.cybersus.common.processes;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.process.ClientProcess;
import com.suslovila.cybersus.client.gui.GuiImplants;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public class ProcessSoulBreak extends ClientProcess {
    int angle = Cybersus.random.nextInt(360);
    public ProcessSoulBreak(SusVec3 vec3, int duration) {
        super(vec3, duration);
    }
    public static final ResourceLocation texture = new ResourceLocation(Cybersus.MOD_ID, "textures/particles/particleBrokenSoul.png");

    public ProcessSoulBreak() {
    }


    @Override
    public void render(RenderWorldLastEvent event) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7f);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if(player == null) return;

//        SusVec3 lookVector = new SusVec3(x, y, z).subtract(SusGraphicHelper.getRenderPos(player, event.partialTicks).subtract(0.0, player.eyeHeight, 0.0));
        SusVec3 lookVector = new SusVec3(-player.getLookVec().xCoord, -player.getLookVec().yCoord, -player.getLookVec().zCoord);
        GL11.glPushMatrix();
        SusGraphicHelper.translateFromPlayerTo(new SusVec3(x, y, z),  event.partialTicks);
        SusGraphicHelper.makeSystemOrthToVectorAndHandle(lookVector,0.1,  () -> {
            glPushAttrib(GL_BLEND);
            glPushAttrib(GL_LIGHTING);
            glPushAttrib(GL_CULL_FACE);

            glEnable(GL_BLEND);
            glDisable(GL_LIGHTING);
            glDisable(GL_CULL_FACE);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            int appearanceTime = 7;
            float alpha = 1 - Math.min(1.0f, (totalDuration - timeLeft + event.partialTicks) / ((float)totalDuration + event.partialTicks));
            GL11.glColor4f(1.0f, 1.0f, 1.0f, alpha);
            SusGraphicHelper.bindTexture(texture);
            GL11.glRotated(angle, 0.0, 0.0, 1.0);
            GuiImplants.drawPartFromLeftSide(1.0f, 1.0f, (totalDuration - timeLeft + event.partialTicks) / (float) (appearanceTime + event.partialTicks));

            glPopAttrib();
            glPopAttrib();
            glPopAttrib();
        });
        GL11.glPopMatrix();
    }

    @Override
    public String getTypeId() {
        return "soul_break";
    }


}
