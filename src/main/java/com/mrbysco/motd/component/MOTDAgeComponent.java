package com.mrbysco.motd.component;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.mrbysco.motd.MOTDPlugin;

public class MOTDAgeComponent implements Component<EntityStore> {
    private int age;

    public MOTDAgeComponent() {
        this.age = 0;
    }

    public static ComponentType<EntityStore, MOTDAgeComponent> getComponentType() {
        return MOTDPlugin.ageComponent;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public Component<EntityStore> clone() {
        MOTDAgeComponent component = new MOTDAgeComponent();
        component.age = this.age;
        return component;
    }
}