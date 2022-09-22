package com.vertmix.profiles.api;

import com.vertmix.packager.api.PacketService;
import com.vertmix.packager.api.packets.ProfileMessagePacket;
import me.lucko.helper.Services;
import me.lucko.helper.mongo.external.morphia.annotations.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity(value = "profiles", noClassnameStored = true)
public class Profile {

    private UUID playerUuid;
    private String playerName;

    private String serverId;

    private long lastSeenTimestamp;

    public Profile() {}
    public Profile(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.lastSeenTimestamp = System.currentTimeMillis();
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public void setLastSeenTimestamp(long lastSeenTimestamp) {
        this.lastSeenTimestamp = lastSeenTimestamp;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getServerId() {
        return serverId;
    }

    public long getLastSeenTimestamp() {
        return lastSeenTimestamp;
    }

    public void msg(@NotNull Profile profile, @NotNull String... messages) {
        final PacketService packetService = Services.load(PacketService.class);
        for (String message : messages)
            packetService.send(new ProfileMessagePacket(profile.getPlayerName(), message));
    }

}
