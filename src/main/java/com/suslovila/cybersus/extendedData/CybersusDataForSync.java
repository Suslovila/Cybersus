package com.suslovila.cybersus.extendedData;

import com.suslovila.cybersus.api.implants.ImplantStorage;

public class CybersusDataForSync {
    public final int playerId;
    public ImplantStorage implantStorage;

    public CybersusDataForSync(int playerId, ImplantStorage storage) {
        this.playerId = playerId;
        this.implantStorage = storage;
    }

    // Getters and setters
}