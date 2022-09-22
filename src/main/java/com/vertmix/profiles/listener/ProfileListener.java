package com.vertmix.profiles.listener;

import com.vertmix.packager.api.PacketService;
import com.vertmix.packager.api.packets.ProfileMessagePacket;
import com.vertmix.profiles.api.Profile;
import com.vertmix.profiles.api.ProfileService;
import me.lucko.helper.Events;
import me.lucko.helper.adventure.Text;
import me.lucko.helper.terminable.TerminableConsumer;
import me.lucko.helper.terminable.module.TerminableModule;
import me.lucko.helper.utils.Players;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProfileListener implements TerminableModule {

    private final PacketService packetService;
    private final ProfileService profileService;

    public ProfileListener(PacketService packetService, ProfileService profileService) {
        this.packetService = packetService;
        this.profileService = profileService;
    }

    @Override
    public void setup(@NotNull TerminableConsumer consumer) {
        Events.subscribe(AsyncPlayerPreLoginEvent.class).handler(event -> {
            final UUID playerUuid = event.getUniqueId();
            final String playerName = event.getName();
            final Profile profile = profileService.fetch(playerUuid, playerName);
            profileService.save(profile);

        }).bindWith(consumer);

        Events.merge(PlayerEvent.class, PlayerKickEvent.class, PlayerQuitEvent.class).handler(event -> {
            final Player player = event.getPlayer();
            profileService.saveAndInvalidate(profileService.fetch(player));
        }).bindWith(consumer);

        packetService.listen(ProfileMessagePacket.class, packet -> {
            Players.get(packet.getPlayerName()).ifPresent(player -> player.sendMessage(Text.colorize(packet.getMessage())));
        });


    }
}
