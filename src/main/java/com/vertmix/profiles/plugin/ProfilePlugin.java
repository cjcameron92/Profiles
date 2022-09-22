package com.vertmix.profiles.plugin;

import com.vertmix.packager.api.PacketService;
import com.vertmix.profiles.api.Profile;
import com.vertmix.profiles.api.ProfileRegistry;
import com.vertmix.profiles.api.ProfileService;
import com.vertmix.profiles.listener.ProfileListener;
import com.vertmix.profiles.registry.SimpleProfileRegistry;
import com.vertmix.profiles.service.SimpleProfileService;
import me.lucko.helper.Commands;
import me.lucko.helper.Services;
import me.lucko.helper.mongo.Mongo;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;

public class ProfilePlugin extends ExtendedJavaPlugin {

    private ProfileRegistry profileRegistry;
    private ProfileService profileService;

    @Override protected void enable() {
        profileRegistry = provideService(ProfileRegistry.class, new SimpleProfileRegistry(Services.load(Mongo.class)));
        profileService = provideService(ProfileService.class, new SimpleProfileService(Services.load(Mongo.class), profileRegistry));

        bindModule(new ProfileListener(Services.load(PacketService.class), profileService));

        Bukkit.getOnlinePlayers().forEach(player -> {
            final Profile profile = profileService.fetch(player);
            profileService.save(profile);
        });

        Commands.parserRegistry().register(Profile.class, s -> profileService.fetchByName(s));
    }
}
