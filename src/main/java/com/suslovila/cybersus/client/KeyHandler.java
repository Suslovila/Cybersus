package com.suslovila.cybersus.client;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.client.gui.GuiImplants;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.PacketOpenImplantGui;
import com.suslovila.cybersus.common.sync.implant.PacketEnableImplantSync;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.*;

public class KeyHandler {

    private static final KeyBinding nextImplantTrigger = new KeyBinding("next implant", Keyboard.KEY_V, Cybersus.MOD_ID + ".key.category");
    private static final KeyBinding previousImplantTrigger = new KeyBinding("previous implant", Keyboard.KEY_C, Cybersus.MOD_ID + ".key.category");
    private static final KeyBinding firstAbilityTrigger = new KeyBinding("use first ability", Keyboard.KEY_F, Cybersus.MOD_ID + ".key.category");
    private static final KeyBinding secondAbilityTrigger = new KeyBinding("use second ability", Keyboard.KEY_G, Cybersus.MOD_ID + ".key.category");
    private static final KeyBinding thirdAbilityTrigger = new KeyBinding("use third ability", Keyboard.KEY_P, Cybersus.MOD_ID + ".key.category");
    private static final KeyBinding renderImplants = new KeyBinding("disable implant render", Keyboard.KEY_I, Cybersus.MOD_ID + ".key.category");

    private static final KeyBinding openImplantGui = new KeyBinding("open implant gui", Keyboard.KEY_J, Cybersus.MOD_ID + ".key.category");


    public static void register() {
        ClientRegistry.registerKeyBinding(nextImplantTrigger);
        ClientRegistry.registerKeyBinding(previousImplantTrigger);
        ClientRegistry.registerKeyBinding(firstAbilityTrigger);
        ClientRegistry.registerKeyBinding(secondAbilityTrigger);
        ClientRegistry.registerKeyBinding(thirdAbilityTrigger);
        ClientRegistry.registerKeyBinding(renderImplants);
        ClientRegistry.registerKeyBinding(openImplantGui);

        FMLCommonHandler.instance().bus().register(new KeyHandler());
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
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
    }

    private void handleAbilityClick(int abilityId) {
        CybersusPacketHandler.INSTANCE.sendToServer(new PacketEnableImplantSync(GuiImplants.currentImplantSlotId, abilityId));
        CybersusPlayerExtendedData.getWrapped(Minecraft.getMinecraft().thePlayer).ifPresent(data -> {
            ItemStack implant = data.implantStorage.getStackInSlot(GuiImplants.currentImplantSlotId);
            if (implant != null) {
                ItemImplant implantClass = (ItemImplant) implant.getItem();
                List<Ability> abilities = implantClass.getAbilities(Minecraft.getMinecraft().thePlayer, GuiImplants.currentImplantSlotId, implant);
                if (abilities.size() > abilityId) {
                    abilities.get(abilityId).tryToActivateAbility(Minecraft.getMinecraft().thePlayer, GuiImplants.currentImplantSlotId, implant);
                }
            }
        });
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

    private void setNextImplant(CybersusPlayerExtendedData data, List<Integer> indexes) {
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