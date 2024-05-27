package dev.efekos.iu.util;

import java.util.UUID;

public class ListeningCache {
    private boolean purpleGlowed;
    private boolean yellowGlowed;
    private boolean aquaGlowed;
    private UUID worldId;
    private int lastCount;

    public ListeningCache(UUID worldId, int lastCount) {
        this(false, false, false, worldId, lastCount);
    }

    public ListeningCache(boolean purpleGlowed, boolean yellowGlowed, boolean aquaGlowed, UUID worldId, int lastCount) {
        this.purpleGlowed = purpleGlowed;
        this.yellowGlowed = yellowGlowed;
        this.aquaGlowed = aquaGlowed;
        this.worldId = worldId;
        this.lastCount = lastCount;
    }

    public boolean isPurpleGlowed() {
        return purpleGlowed;
    }

    public void setPurpleGlowed(boolean purpleGlowed) {
        this.purpleGlowed = purpleGlowed;
    }

    public int getLastCount() {
        return lastCount;
    }

    public void setLastCount(int lastCount) {
        this.lastCount = lastCount;
    }

    public boolean isYellowGlowed() {
        return yellowGlowed;
    }

    public void setYellowGlowed(boolean yellowGlowed) {
        this.yellowGlowed = yellowGlowed;
    }

    public boolean isAquaGlowed() {
        return aquaGlowed;
    }

    public void setAquaGlowed(boolean aquaGlowed) {
        this.aquaGlowed = aquaGlowed;
    }

    public UUID getWorldId() {
        return worldId;
    }

    public void setWorldId(UUID worldId) {
        this.worldId = worldId;
    }
}
