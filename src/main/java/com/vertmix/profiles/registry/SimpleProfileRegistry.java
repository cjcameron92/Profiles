package com.vertmix.profiles.registry;

import com.vertmix.profiles.api.Profile;
import com.vertmix.profiles.api.ProfileRegistry;
import me.lucko.helper.mongo.Mongo;
import me.lucko.helper.mongo.external.morphia.Datastore;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SimpleProfileRegistry implements ProfileRegistry {

    private final Map<UUID, Profile> profiles = new LinkedHashMap<>();
    private final Map<String, UUID> profilesByName = new LinkedHashMap<>();

    private final Mongo mongo;
    private final Datastore datastore;

    public SimpleProfileRegistry(Mongo mongo) {
        this.mongo = mongo;
        this.datastore = mongo.getMorphiaDatastore();
    }

    @Override
    public void invalidate(@NotNull Profile profile) {
        profiles.remove(profile.getPlayerUuid());
        profilesByName.remove(profile.getPlayerName());
    }

    @Override
    public @NotNull Profile register(@NotNull UUID uuid, @NotNull Profile profile) {
        profiles.put(uuid, profile);
        profilesByName.put(profile.getPlayerName(), profile.getPlayerUuid());
        return profile;
    }

    @Override
    public @NotNull Optional<Profile> get(@NotNull UUID uuid) {

        if (profiles.containsKey(uuid)) {
            return Optional.of(profiles.get(uuid));
        }

        final Profile profile = datastore.find(Profile.class).filter("playerUuid", uuid).get();
        if (profile == null)
            return Optional.empty();

        return Optional.of(register(uuid, profile));
    }

    @Override
    public @NotNull Optional<Profile> getByName(@NotNull String playerName) {
        if (profilesByName.containsKey(playerName)) {
            return get(profilesByName.get(playerName));
        }

        final Profile profile = datastore.find(Profile.class).filter("playerName", playerName).get();
        if (profile == null)
            return Optional.empty();

        return Optional.of(register(profile.getPlayerUuid(), profile));
    }
}
