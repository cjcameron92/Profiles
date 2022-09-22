package com.vertmix.profiles.service;

import com.vertmix.profiles.api.Profile;
import com.vertmix.profiles.api.ProfileRegistry;
import com.vertmix.profiles.api.ProfileService;
import me.lucko.helper.Schedulers;
import me.lucko.helper.mongo.Mongo;
import me.lucko.helper.mongo.external.morphia.Datastore;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class SimpleProfileService implements ProfileService {

    private final Mongo mongo;
    private final Datastore datastore;
    private final ProfileRegistry profileRegistry;

    public SimpleProfileService(Mongo mongo, ProfileRegistry profileRegistry) {
        this.mongo = mongo;
        this.datastore = mongo.getMorphiaDatastore();
        this.profileRegistry = profileRegistry;
    }

    @Override
    public @NotNull Profile fetch(@NotNull UUID uuid, @NotNull String playerName) {
        final Profile profile = profileRegistry.get(uuid).orElse(new Profile(uuid));
        profile.setPlayerName(playerName);
        profile.setServerId(Bukkit.getServer().getName());
        profile.setLastSeenTimestamp(System.currentTimeMillis());

        profileRegistry.register(uuid, profile);

        return profile;

    }

    @Override
    public @NotNull Optional<Profile> fetchByName(@NotNull String playerName) {
        return profileRegistry.getByName(playerName);
    }

    @Override
    public void save(@NotNull Profile profile) {
        Schedulers.async().run(() -> datastore.save(profile));
        profileRegistry.register(profile.getPlayerUuid(), profile);
    }

    @Override
    public void delete(@NotNull Profile profile) {
        Schedulers.async().run(() -> datastore.delete(profile));
    }

    @Override
    public void saveAndInvalidate(@NotNull Profile profile) {
        save(profile);
        profileRegistry.invalidate(profile);
    }
}
