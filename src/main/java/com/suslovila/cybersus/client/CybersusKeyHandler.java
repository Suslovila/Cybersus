package com.suslovila.cybersus.client;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.client.gui.CybersusGui;
import com.suslovila.cybersus.client.gui.GuiImplants;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.item.implants.reactionIncreaser.ImplantReactionIncreaser;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.PacketOpenImplantGui;
import com.suslovila.cybersus.common.sync.implant.PacketEnableImplantSync;
import com.suslovila.cybersus.common.sync.implant.PacketReactionIncreaserActivated;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.*;

import static com.suslovila.cybersus.utils.SusVec3.getVectorFromHeadRotationHorizontal;

public class CybersusKeyHandler {

    public static final KeyBinding nextImplantTrigger = new KeyBinding("next implant", Keyboard.KEY_V, Cybersus.MOD_ID + ".key.category");
    public static final KeyBinding previousImplantTrigger = new KeyBinding("previous implant", Keyboard.KEY_C, Cybersus.MOD_ID + ".key.category");
    public static final KeyBinding firstAbilityTrigger = new KeyBinding("use first ability", Keyboard.KEY_F, Cybersus.MOD_ID + ".key.category");
    public static final KeyBinding secondAbilityTrigger = new KeyBinding("use second ability", Keyboard.KEY_G, Cybersus.MOD_ID + ".key.category");
    public static final KeyBinding thirdAbilityTrigger = new KeyBinding("use third ability", Keyboard.KEY_P, Cybersus.MOD_ID + ".key.category");
    public static final KeyBinding renderImplants = new KeyBinding("disable implant render", Keyboard.KEY_I, Cybersus.MOD_ID + ".key.category");
    public static final KeyBinding implantSelectorGui = new KeyBinding("open implant selector", Keyboard.KEY_J, Cybersus.MOD_ID + ".key.category");

    private static final KeyBinding openImplantGui = new KeyBinding("open implant gui", Keyboard.KEY_J, Cybersus.MOD_ID + ".key.category");



    public static void register() {
        ClientRegistry.registerKeyBinding(nextImplantTrigger);
        ClientRegistry.registerKeyBinding(previousImplantTrigger);
        ClientRegistry.registerKeyBinding(firstAbilityTrigger);
        ClientRegistry.registerKeyBinding(secondAbilityTrigger);
        ClientRegistry.registerKeyBinding(thirdAbilityTrigger);
        ClientRegistry.registerKeyBinding(renderImplants);
        ClientRegistry.registerKeyBinding(openImplantGui);
        ClientRegistry.registerKeyBinding(implantSelectorGui);

        FMLCommonHandler.instance().bus().register(new CybersusKeyHandler());
        MinecraftForge.EVENT_BUS.register(new CybersusKeyHandler());
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        checkImplantSwitch();
        if (firstAbilityTrigger.isPressed()) {
            handleAbilityClick(0);
        }
        if (secondAbilityTrigger.isPressed()) {
            handleAbilityClick(1);
        }
        if (renderImplants.isPressed()) {
            GuiImplants.shouldRenderGui = !GuiImplants.shouldRenderGui;
        }
        if (openImplantGui.isPressed()) {
            CybersusPacketHandler.INSTANCE.sendToServer(new PacketOpenImplantGui());
        }
        if (implantSelectorGui.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player != null) {
            player.openGui(
                        Cybersus.MOD_ID,
                        CybersusGui.IMPLANT_SELECTOR.ordinal(),
                        player.worldObj,
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ
                );
            }
        }
    }

    private void handleAbilityClick(int abilityId) {

        CybersusPacketHandler.INSTANCE.sendToServer(new PacketEnableImplantSync(GuiImplants.currentImplantSlotId, abilityId));
        CybersusPlayerExtendedData.getWrapped(Minecraft.getMinecraft().thePlayer).ifPresent(data -> {
            ItemStack implant = data.implantStorage.getStackInSlot(GuiImplants.currentImplantSlotId);
            if (implant != null) {
                ItemImplant implantClass = (ItemImplant) implant.getItem();
                if(implantClass instanceof ImplantReactionIncreaser) {

                    CybersusPacketHandler.INSTANCE.sendToServer(new PacketReactionIncreaserActivated(GuiImplants.currentImplantSlotId, abilityId, getMovementDirection()));

                }
                List<Ability> abilities = implantClass.getAbilities(Minecraft.getMinecraft().thePlayer, GuiImplants.currentImplantSlotId, implant);
                if (abilities.size() > abilityId) {
                    abilities.get(abilityId).tryToActivateAbility(Minecraft.getMinecraft().thePlayer, GuiImplants.currentImplantSlotId, implant);
                }
            }
        });
    }


    public SusVec3 getMovementDirection() {

             final Minecraft mc = Minecraft.getMinecraft();

                // Ссылки на стандартные биндинги из настроек игры
                KeyBinding keyForward  = mc.gameSettings.keyBindForward;   // W
                KeyBinding keyBack     = mc.gameSettings.keyBindBack;      // S
                KeyBinding keyLeft     = mc.gameSettings.keyBindLeft;      // A
                KeyBinding keyRight    = mc.gameSettings.keyBindRight;     // D
                KeyBinding keyJump     = mc.gameSettings.keyBindJump;      // Пробел (вверх)
                KeyBinding keySneak    = mc.gameSettings.keyBindSneak;     // Shift (вниз)

                boolean forwardPressed = keyForward.getIsKeyPressed();
                boolean backPressed    = keyBack.getIsKeyPressed();
                boolean leftPressed    = keyLeft.getIsKeyPressed();
                boolean rightPressed   = keyRight.getIsKeyPressed();
                boolean upPressed      = keyJump.getIsKeyPressed();
                boolean downPressed    = keySneak.getIsKeyPressed();


                SusVec3 resultVector = new SusVec3(0,0,0);
                if(upPressed) {
                    resultVector = resultVector.add(0, 1, 0);
                }
                if(downPressed) {
                    resultVector = resultVector.add(0, -1, 0);
                }

                if(forwardPressed) {
                    resultVector = resultVector.add(0, 0, 1);

                }
                if(backPressed) {
                    resultVector = resultVector.add(0, 0, -1);

                }

        if(leftPressed) {
            resultVector = resultVector.add(1, 0, 0);

        }
        if(rightPressed) {
            resultVector = resultVector.add(-1, 0, 0);

        }

        SusVec3 southVector = new SusVec3(0, 0, 1);
        SusVec3 eastVector = new SusVec3(1, 0, 0);

        if(resultVector.x == 0 && resultVector.y == 0 && resultVector.z == 0) {
            return SusVec3.getLookVec(mc.thePlayer);
        }
        if(resultVector.x == 0 && resultVector.z == 0 && resultVector.y != 0) {
            return resultVector;
        }
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        SusVec3 lookVector = getVectorFromHeadRotationHorizontal(player);
        if(lookVector.x == 0 && lookVector.y == 0 && lookVector.z == 0) {
            return new SusVec3(0.0, 1.0, 0.0);
        }
        SusVec3 xzProjection = new SusVec3(resultVector.x, 0, resultVector.z);
        double angleBetweenLookVectorAndCordSystem = SusVec3.angleBetweenVec3(lookVector, southVector) * (lookVector.x > 0 ? 1 : -1);
        double angleBetweenDisplacementAndStandartSystem = SusVec3.angleBetweenVec3(xzProjection, southVector) * (xzProjection.x > 0 ? 1 : -1);

        double resultAngle = angleBetweenLookVectorAndCordSystem + angleBetweenDisplacementAndStandartSystem;
        SusVec3 rotatedVector = southVector.scale(Math.cos(resultAngle)).add(eastVector.scale(Math.sin(resultAngle)));
                return new SusVec3(rotatedVector.x, resultVector.y, rotatedVector.z);





//        return new SusVec3(0,0,0);

    }


    public void checkImplantSwitch() {
        if (nextImplantTrigger.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return;

            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
            if (data != null) {
                int nextIndex = (GuiImplants.currentImplantSlotId + 1) % ImplantType.getTotalSlotAmount();
                List<Integer> indexes = getIndicesCycledFrom(nextIndex, ImplantType.getTotalSlotAmount());
                setNextImplant(data, indexes);
            }
        }

        if (previousImplantTrigger.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return;

            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
            if (data != null) {
                int previousIndex = GuiImplants.currentImplantSlotId;
                List<Integer> indexes = getIndicesCycledFrom(previousIndex, ImplantType.getTotalSlotAmount());
                Collections.reverse(indexes);
                setNextImplant(data, indexes);
            }
        }
    }

    public static void setNextImplant(CybersusPlayerExtendedData data, List<Integer> indexes) {
        for (int index : indexes) {
            ItemStack implant = data.implantStorage.getStackInSlot(index);
            if (implant != null) {
                GuiImplants.currentImplantSlotId = index;
                return;
            }
        }
    }

    public static LinkedList<Integer> getIndicesCycledFrom(int index, int maxSize) {
        LinkedList<Integer> listWithIndexes = new LinkedList<>();
        for (int j = index; j < maxSize; j++) {
            listWithIndexes.add(j);
        }
        for (int j = 0; j < index; j++) {
            listWithIndexes.add(j);
        }
        return listWithIndexes;
    }

}