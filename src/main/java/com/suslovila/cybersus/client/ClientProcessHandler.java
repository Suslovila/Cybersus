package com.suslovila.cybersus.client;

import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.common.item.implants.essentiaHeart.ImplantShadowSkin;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusObjectWrapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import static com.suslovila.cybersus.common.item.implants.essentiaHeart.ImplantShadowSkin.PREPARATION_TIMER;


public class ClientProcessHandler {

    public static ClientProcessHandler INSTANCE = new ClientProcessHandler();

    // в виду наличия лишь одного инстанса класса и только на клиенте, можно в лоб сделать поле, которое будет чекать считывания кнопки эври тик
    // и да, я знаю, что читами можно изменить переменные на локал машине. Но мне как-то все равно на это. Никто не будет ради этого заморачиваться... Надеюсь
    @SideOnly(Side.CLIENT)
    public boolean wasMovementDisabledBefore = false;

    private ClientProcessHandler() {
    }

    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;

        CustomWorldData.getCustomData(player.worldObj).foreachProcess((uuid, process) -> {
            process.render(event);
        });
    }

    @SubscribeEvent
    public void worldTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;
        World world = player.worldObj;
        if (event.phase == TickEvent.Phase.END) {
            CustomWorldData customWorldData = CustomWorldData.getCustomData(world);
            customWorldData.foreachProcess((uuid, process) -> {
                process.tickClient(event);
            });
            Iterator<Map.Entry<String, HashMap<UUID, WorldProcess>>> iteratorInWhole = customWorldData.processesMapsByType.entrySet().iterator();
            while (iteratorInWhole.hasNext()) {
                Map.Entry<String, HashMap<UUID, WorldProcess>> entry = iteratorInWhole.next();
                Iterator<Map.Entry<UUID, WorldProcess>> oneTypeIterator = entry.getValue().entrySet().iterator();
                while (oneTypeIterator.hasNext()) {
                    WorldProcess process = oneTypeIterator.next().getValue();
                    if (process.isExpired(event)) {
                        oneTypeIterator.remove();
                        process.onExpired(event);
                        customWorldData.markDirty();

                    }
                }

            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void blockPlayerMovement(LivingEvent.LivingUpdateEvent event) {

        EntityLivingBase entityLiving = event.entityLiving;
        if (entityLiving instanceof EntityPlayer && entityLiving.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            CustomWorldData worldData = CustomWorldData.getCustomData(entityLiving.worldObj);
            HashMap<UUID, WorldProcess> processes = worldData.getProcessesOfType(ProcessGravityTrap.class);
            for (WorldProcess process : processes.values()) {
                ProcessGravityTrap gravityTrap = (ProcessGravityTrap) process;
                if (gravityTrap.isPlayerInside(player) && gravityTrap.isActivated()) {
                    if (!wasMovementDisabledBefore) {
                        Minecraft.getMinecraft().thePlayer.movementInput = new MovementInput();
                        wasMovementDisabledBefore = true;
                    }
                    return;
                }
            }

            SusObjectWrapper<Boolean> hasDis = new SusObjectWrapper<>(false);

            CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack, isDisabled) -> {
                if (stack.getItem() instanceof ImplantShadowSkin) {
                    Ability shadowTravel = ((ImplantShadowSkin) stack.getItem()).getAbilities(player, index, stack).get(0);
                    NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(stack);
                    boolean hasCompletedPreparations = true;
                    if (tagCompound.hasKey(PREPARATION_TIMER)) {
                        hasCompletedPreparations = tagCompound.getInteger(PREPARATION_TIMER) <= 0;
                    }

                    if (!hasCompletedPreparations) {
                        Minecraft.getMinecraft().thePlayer.movementInput = new MovementInput();
                        wasMovementDisabledBefore = true;
                        isDisabled.value = true;
                    }
                }
            }, hasDis);

            if(hasDis.value) return;
            // if no movement disables were done:
            if (wasMovementDisabledBefore) {
                Minecraft.getMinecraft().thePlayer.movementInput = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
                wasMovementDisabledBefore = false;
            }

        }
    }


}