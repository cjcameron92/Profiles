package com.vertmix.profiles.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface ProfileService {

    @NotNull Profile fetch(@NotNull UUID uuid, @NotNull String playerName);

    default @NotNull Profile fetch(@NotNull Player player) {
        return fetch(player.getUniqueId(), player.getName());
    }
    @NotNull Optional<Profile> fetchByName(@NotNull String playerName);
    void save(@NotNull Profile profile);

    void delete(@NotNull Profile profile);

    void saveAndInvalidate(@NotNull Profile profile);

}
