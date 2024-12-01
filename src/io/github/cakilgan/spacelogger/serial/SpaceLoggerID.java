package io.github.cakilgan.spacelogger.serial;

import java.util.UUID;

public class SpaceLoggerID {
    final UUID uuid;
    final String name;
    public SpaceLoggerID(String name){
        this.name = name;
        this.uuid = UUID.randomUUID();
    }
    public SpaceLoggerID(String name,UUID uuid){
        this.name = name;
        this.uuid = uuid;
    }

    public UUID uuid() {
        return uuid;
    }
    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpaceLoggerID){
            return ((SpaceLoggerID) obj).name.equals(name)&&((SpaceLoggerID) obj).uuid.equals(uuid);
        }
        return false;
    }
}
