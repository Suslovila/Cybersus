package com.suslovila.cybersus.extendedData;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.process.ISaveableProcess;
import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.api.process.ProcessRegistry;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.PacketSyncAllProcess;
import com.suslovila.cybersus.common.sync.PacketSyncProcess;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public class CustomWorldData extends WorldSavedData {

    public static final String TAG_PROCESS_OF_ONE_TYPE_DATA = "process_of_type_data";
    public static final String TAG_ALL_PROCESS_DATA = "all_processes_data";
    public static final String TAG_PROCESS_TYPE_NAME = "process_type";
    public static final String TAG_PROCESS_UUID = "process_uuid";

    public static CustomWorldData getCustomData(World world) {
        int dimensionId = world.provider.dimensionId;
        String name = Cybersus.MOD_ID + "_" + dimensionId;
        CustomWorldData data = (CustomWorldData) world.perWorldStorage.loadData(CustomWorldData.class, name);
        if (data == null) {
            data = new CustomWorldData(name);
            data.markDirty();
            world.perWorldStorage.setData(name, data);
        }
        return data;
    }

//    public HashMap<UUID, Process> processes = new HashMap<>();
    public HashMap<String, HashMap<UUID, WorldProcess>> processesMapsByType = new HashMap<>();

    public CustomWorldData(String datakey) {
        super(datakey);
    }

//    @Override
//    public void readFromNBT(NBTTagCompound tag) {
//        if (tag == null) return;
//        NBTTagList processTagList = tag.getTagList(TAG_PROCESS_OF_ONE_TYPE_DATA, KhariumSusNBTHelper.TAG_COMPOUND);
//        if (processTagList == null) return;
//        processes.clear();
//        for (int i = 0; i < processTagList.tagCount(); i++) {
//            NBTTagCompound processTag = processTagList.getCompoundTagAt(i);
//            processTag.getString("typeId");
//
//            Class[] objects = new Class[0];
//            Class<? extends com.suslovila.cybersus.api.process.Process> clazz = ProcessRegistry.getClassType(processTag.getString("typeId"));
//            if (clazz == null) continue;
//            Process process;
//            try {
//                process = clazz.getDeclaredConstructor(objects).newInstance();
//            } catch (Exception e) {
//                continue;
//            }
//
//            if (!(process instanceof ISaveableProcess)) continue;
//            ((ISaveableProcess) process).readFrom(processTag);
//            UUID uuid = KhariumSusNBTHelper.getUUID(processTag, "process_uuid");
//            process.uuid = uuid;
//            this.processes.put(uuid, process);
//        }
//    }
    @Override
    public void readFromNBT(NBTTagCompound rootTag) {
        if (rootTag == null) return;
        NBTTagList allProcessTagList = rootTag.getTagList(TAG_ALL_PROCESS_DATA, KhariumSusNBTHelper.TAG_COMPOUND);
        if (allProcessTagList == null) return;
        processesMapsByType.clear();
        for (int i = 0; i < allProcessTagList.tagCount(); i++) {
            NBTTagCompound processOfOneTypeTag = allProcessTagList.getCompoundTagAt(i);
            String processesType = processOfOneTypeTag.getString(TAG_PROCESS_TYPE_NAME);

            HashMap<UUID, WorldProcess> mapToPutIn = new HashMap<>();

            Class<? extends WorldProcess> clazz = ProcessRegistry.getClassType(processesType);
            NBTTagList processTagList = processOfOneTypeTag.getTagList(TAG_PROCESS_OF_ONE_TYPE_DATA, KhariumSusNBTHelper.TAG_COMPOUND);
            if(processTagList == null) continue;
            readOneProcessType(processTagList, mapToPutIn, clazz);

            this.processesMapsByType.put(processesType, mapToPutIn);
        }
    }

    public void readOneProcessType(NBTTagList rootTagOfType, HashMap<UUID, WorldProcess> mapToPutIn, Class<? extends WorldProcess> clazz) {
        if (clazz == null) return;
        for (int i = 0; i < rootTagOfType.tagCount(); i++) {
            NBTTagCompound processTag = rootTagOfType.getCompoundTagAt(i);
            Class[] objects = new Class[0];
            WorldProcess process;
            try {
                process = clazz.getDeclaredConstructor(objects).newInstance();
            } catch (Exception e) {
                return;
            }

            if (!(process instanceof ISaveableProcess)) return;
            ((ISaveableProcess) process).readFrom(processTag);
            UUID uuid = KhariumSusNBTHelper.getUUID(processTag, TAG_PROCESS_UUID);
            process.uuid = uuid;
            mapToPutIn.put(uuid, process);
        }
    }

//    @Override
//    public void writeToNBT(NBTTagCompound rootTag) {
//        if (rootTag == null) return;
//        NBTTagList processTagList = new NBTTagList();
//        for (Map.Entry<UUID, Process> entry : processes.entrySet()) {
//            if(!(entry.getValue() instanceof ISaveableProcess)) continue;
//            NBTTagCompound processTag = new NBTTagCompound();
//            KhariumSusNBTHelper.setUUID(processTag, "process_uuid", entry.getKey());
//            processTag.setString("typeId", entry.getValue().getTypeId());
//            ((ISaveableProcess)entry.getValue()).writeTo(processTag);
//
//            processTagList.appendTag(processTag);
//        }
//        rootTag.setTag(TAG_PROCESS_OF_ONE_TYPE_DATA, processTagList);
//
//    }
@Override
public void writeToNBT(NBTTagCompound rootTag) {
        if (rootTag == null) return;
        NBTTagList allProcessTagList = new NBTTagList();
        for (Map.Entry<String, HashMap<UUID, WorldProcess>> typeEntry : processesMapsByType.entrySet()) {
            NBTTagCompound tagForType = new NBTTagCompound();
            tagForType.setString(TAG_PROCESS_TYPE_NAME, typeEntry.getKey());
            writeMapOfProcesses(tagForType, typeEntry.getValue());

            allProcessTagList.appendTag(tagForType);

        }
        rootTag.setTag(TAG_ALL_PROCESS_DATA, allProcessTagList);

        int t = 5;
    }

    public void writeMapOfProcesses(NBTTagCompound rootTag, HashMap<UUID, WorldProcess> processesIn) {
        if (rootTag == null) return;
        NBTTagList processTagList = new NBTTagList();
        for (Map.Entry<UUID, WorldProcess> entry : processesIn.entrySet()) {
            if(!(entry.getValue() instanceof ISaveableProcess)) continue;
            NBTTagCompound processTag = new NBTTagCompound();
            KhariumSusNBTHelper.setUUID(processTag, TAG_PROCESS_UUID, entry.getKey());
            ((ISaveableProcess)entry.getValue()).writeTo(processTag);

            processTagList.appendTag(processTag);
        }
        rootTag.setTag(TAG_PROCESS_OF_ONE_TYPE_DATA, processTagList);

    }
//    public void addProcess(Process process) {
//        this.processes.put(process.uuid, process);
//        markDirty();
//    }

    public HashMap<UUID, WorldProcess> getProcessesOfType(String type) {
        return processesMapsByType.getOrDefault(type, new HashMap<>());
    }
    public HashMap<UUID, WorldProcess> getProcessesOfType(Class<? extends WorldProcess> clazz) {
        String type = ProcessRegistry.getKey(clazz);
        return processesMapsByType.getOrDefault(type, new HashMap<>());
    }

    public WorldProcess getProcessByUUID(UUID requiredUuid) {
        for(String type : processesMapsByType.keySet()) {
            HashMap<UUID, WorldProcess> processHashMap = processesMapsByType.getOrDefault(type, new HashMap<>());
            if(processHashMap.containsKey(requiredUuid)) {
                return processHashMap.get(requiredUuid);
            }
        }

        return null;
    }

    public WorldProcess getProcessByUUID(String type, UUID uuid) {
        return processesMapsByType.getOrDefault(type, new HashMap<>()).get(uuid);
    }

    public void addProcess(WorldProcess process) {
        String type = process.getTypeId();
        if(!processesMapsByType.containsKey(type)) {
            processesMapsByType.put(type, new HashMap<>());
        }
        processesMapsByType.get(type).put(process.uuid, process);

        markDirty();
    }

    public static void syncProcess(WorldProcess process, int dimensionId) {
        CybersusPacketHandler.INSTANCE.sendToDimension(new PacketSyncProcess(process), dimensionId);
    }
    public static void syncProcess(WorldProcess process, EntityPlayerMP playerMP) {
        CybersusPacketHandler.INSTANCE.sendTo(new PacketSyncProcess(process), playerMP);
    }
    public static void syncProcess(WorldProcess process) {
        CybersusPacketHandler.INSTANCE.sendToAll(new PacketSyncProcess(process));
    }

    public void syncAllProcess(int dimensionId) {
        CybersusPacketHandler.INSTANCE.sendToDimension(new PacketSyncAllProcess(this.processesMapsByType), dimensionId);
    }
    public void syncAllProcess(EntityPlayerMP playerMP) {
        CybersusPacketHandler.INSTANCE.sendTo(new PacketSyncAllProcess(this.processesMapsByType), playerMP);
    }
    public void syncAllProcess() {
        CybersusPacketHandler.INSTANCE.sendToAll(new PacketSyncAllProcess(this.processesMapsByType));
    }

    public void foreachProcess(BiConsumer<UUID, WorldProcess> lambda) {
        for(HashMap<UUID, WorldProcess> processHashMap : processesMapsByType.values()) {
            for(Map.Entry<UUID, WorldProcess> entry : processHashMap.entrySet()) {
                lambda.accept(entry.getKey(), entry.getValue());
            }
        }
    }
}

