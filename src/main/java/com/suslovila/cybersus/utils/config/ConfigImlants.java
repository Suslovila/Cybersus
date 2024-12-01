package com.suslovila.cybersus.utils.config;

import com.suslovila.cybersus.api.implants.ImplantType;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigImlants {


    public static void registerServerConfig(File modCfg) {
        Configuration cfg = new Configuration(modCfg);
        try {
            for (ImplantType type : ImplantType.values()) {
                ImplantType.slotAmounts.add(
                    type.ordinal(), cfg.getInt(
                        "implant " + type + " slot amount",
                        "Implants",
                        type.defaultSlotAmount,
                        1, Integer.MAX_VALUE,
                        ""
                    )
                );
            }
        } catch (Exception exception) {
            System.out.println("config died :(");
        } finally {
            cfg.save();
        }
    }
}

