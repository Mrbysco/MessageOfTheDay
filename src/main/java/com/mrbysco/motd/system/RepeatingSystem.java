package com.mrbysco.motd.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.mrbysco.motd.MOTDPlugin;
import com.mrbysco.motd.component.MOTDAgeComponent;
import com.mrbysco.motd.data.MOTDDatabase;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;

public class RepeatingSystem extends EntityTickingSystem<EntityStore> {
    private int tickCounter = 0;

    @Nonnull
    private static final Query<EntityStore> QUERY = Query.and(
            Player.getComponentType()
    );

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {
        tickCounter += 1;
        int TICKS_PER_SECOND = 30;
        if (tickCounter < TICKS_PER_SECOND) {
            return; // Only process once per second
        }
        tickCounter = 0;

        PlayerRef playerref = archetypeChunk.getComponent(index, PlayerRef.getComponentType());
        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(index);

        assert playerref != null;

        MOTDAgeComponent ageComponent = commandBuffer.getComponent(ref, MOTDPlugin.ageComponent);
        if (ageComponent == null) {
            ageComponent = new MOTDAgeComponent();
            commandBuffer.addComponent(ref, MOTDPlugin.ageComponent, ageComponent);
        }

        // Just to be safe
        if (ageComponent == null) {
            return;
        }

        int currentAge = ageComponent.getAge();
        ageComponent.setAge(currentAge + 1);

        int ageInSeconds = ageComponent.getAge();
        // Check if age is multiple of configured interval
        int interval = MOTDDatabase.getIntervalBetweenRepeatingMessages();

        if (interval > 0 && ageInSeconds > 0 && ageInSeconds % interval == 0) {
            MOTDDatabase.sendRepeatingMessage(playerref, playerref.getUuid());
        }
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return QUERY;
    }
}
