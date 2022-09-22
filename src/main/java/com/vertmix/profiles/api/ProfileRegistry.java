package com.vertmix.profiles.api;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRegistry {

    void invalidate(@NotNull Profile profile);

    @NotNull Profile register(@NotNull UUID uuid, @NotNull Profile profile);

    @NotNull Optional<Profile> get(@NotNull UUID uuid);

    @NotNull Optional<Profile> getByName(@NotNull String playerName);
}
