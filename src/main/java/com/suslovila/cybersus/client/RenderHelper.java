package com.suslovila.cybersus.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

// taken from Vazki's Botania mod
public class RenderHelper {
	public static class Helper {

		public static void rotateIfSneaking(EntityPlayer player) {
			if(player.isSneaking())
				applySneakingRotation();
		}

		public static void applySneakingRotation() {
			GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);
		}

		public static void translateToHeadLevel(EntityPlayer player) {
			GL11.glTranslated(0, (player != Minecraft.getMinecraft().thePlayer ? 1.75F : 0F) + player.getEyeHeight() - (player.isSneaking() ? 0.0625 : 0), 0);
		}

	}

	public static enum RenderType {
		BODY,

		HEAD;
	}

}
