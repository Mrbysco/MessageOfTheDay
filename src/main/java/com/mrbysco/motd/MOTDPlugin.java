package com.mrbysco.motd;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import com.mrbysco.motd.command.motd.MOTDCommand;
import com.mrbysco.motd.component.MOTDAgeComponent;
import com.mrbysco.motd.config.MOTDConfig;
import com.mrbysco.motd.data.MOTDDatabase;
import com.mrbysco.motd.system.RepeatingSystem;

import javax.annotation.Nonnull;

public class MOTDPlugin extends JavaPlugin {
    protected static MOTDPlugin instance;
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static ComponentType<EntityStore, MOTDAgeComponent> ageComponent;
    private final Config<MOTDConfig> config;


    public static MOTDPlugin get() {
        return instance;
    }

    public MOTDPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        this.config = this.withConfig("MOTDConfig", MOTDConfig.CODEC);
    }

    private void onPlayerConnect(PlayerConnectEvent event) {
        PlayerRef playerRef = event.getPlayerRef();
        if (playerRef != null && playerRef.isValid()) {
            playerRef.sendMessage(MOTDDatabase.getMOTD());
        }
    }

    @Override
    protected void setup() {
        instance = this;
        this.getCommandRegistry().registerCommand(new MOTDCommand());
        this.getEventRegistry().registerGlobal(PlayerConnectEvent.class, this::onPlayerConnect);

        ageComponent = this.getEntityStoreRegistry().registerComponent(MOTDAgeComponent.class, MOTDAgeComponent::new);
        this.getEntityStoreRegistry().registerSystem(new RepeatingSystem());
    }

    @Override
    protected void start() {
        super.start();
        this.config.save();
        MOTDConfig config = this.config.get();
        MOTDDatabase.populateList(config);
    }

    public Config<MOTDConfig> getMOTDConfig() {
        return this.config;
    }
}