package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImplantPhoenixHeart extends ItemCybersusImplant {
    public static final String name = "phoenix_heart";
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantPhoenixHeart() {
        super(ImplantType.HEART);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityPassive("resurrection") {

            public FuelComposite fuelForRessurection = FuelComposite.allRequired(new FuelEssentia(new AspectList().add(Aspect.LIFE, 712).add(Aspect.FIRE, 128).add(Aspect.EXCHANGE, 128)));

            @Override
            public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {
                return null;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 20 * 60;
            }


            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.EMPTY;
            }


            @Override
            public void onPlayerDeathEvent(LivingDeathEvent event, EntityPlayer player, int index, ItemStack stack) {
                if (isOnCooldown(stack) || !isActive(stack) || event.isCanceled()) return;

                FuelComposite fuelComposite = FuelComposite.allRequired(new FuelEssentia(new AspectList().add(Aspect.LIFE, 712).add(Aspect.FIRE, 128).add(Aspect.EXCHANGE, 128)));
//                FuelComposite fuelComposite = FuelComposite.EMPTY;
                if (fuelComposite.tryTakeFuelFromPlayer(player)) {
                    event.setCanceled(true);
                    player.setHealth(4f);
                    World world = player.worldObj;
                    if (world instanceof WorldServer) {
                        WorldServer worldServer = (WorldServer) world;
                        worldServer.playSoundAtEntity(
                                player,
                                Cybersus.MOD_ID + ":ability_phoenix_heart",
                                1.5f,
                                1.4f + worldServer.rand.nextFloat() * 0.2f
                        );
                            worldServer.func_147487_a("flame", player.posX, player.posY + 1.2, player.posZ, 40,
                                    SusUtils.nextDouble(-1, 1),
                                    SusUtils.nextDouble(-1, 1),
                                    SusUtils.nextDouble(-1, 1),
                                    0.3
                                    );

                        sendToCooldown(player, index, stack);
                        notifyClient(player, index, stack);
                    }
                }
            }

            @Override
            public void onRenderWorldLastEvent(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
//                if(!isActive(implant)) return;
//                GL11.glPushMatrix();
//
//                GL11.glTranslated(1.0f, 1.0f, 1.0f);
//                GL11.glRotatef(60.0f, 1.0f, 0.0f, 0.0f);
//                GL11.glPushAttrib(GL11.GL_CULL_FACE);
//                GL11.glDisable(GL11.GL_CULL_FACE);
//                SusGraphicHelper.bindTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/implants/ability_resurrection.png"));
//                SusGraphicHelper.drawFromCenter(16.0);
//                GL11.glPopAttrib();
//
//                GL11.glPopMatrix();
            }

            @Override
            public boolean hasFuel(EntityPlayer player, int index, ItemStack implant) {

                return fuelForRessurection.hasPlayerEnough(player);
            }
        });
    }

    @Override
    public String getName() {
        return name;
    }
}

