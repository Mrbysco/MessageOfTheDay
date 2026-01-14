package com.mrbysco.motd.data;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.util.Config;
import com.mrbysco.motd.MOTDPlugin;
import com.mrbysco.motd.config.MOTDConfig;
import com.mrbysco.motd.util.MOTDParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MOTDDatabase {
    private static final Random random = new Random();
    private static List<String> rawMOTDs = new ArrayList<>();
    private static List<Message> motdMessages = new ArrayList<>();

    public static void populateList(MOTDConfig config) {
        motdMessages.clear();
        for (String motd : config.motdMessages) {
            rawMOTDs.add(motd);
            Message message = MOTDParser.parse(motd);
            motdMessages.add(message);
        }
    }

    public static List<Message> getMOTDList() {
        return motdMessages;
    }

    public static void addMOTD(String motd) {
        rawMOTDs.add(motd);

        Message message = MOTDParser.parse(motd);
        motdMessages.add(message);

        Config<MOTDConfig> config = MOTDPlugin.get().getMOTDConfig();
        MOTDConfig motdConfig = config.get();
        motdConfig.motdMessages = rawMOTDs.toArray(new String[0]);
        config.save();
    }

    public static void remove(int index) {
        if (index >= 0 && index < motdMessages.size()) {
            rawMOTDs.remove(index);
            motdMessages.remove(index);

            Config<MOTDConfig> config = MOTDPlugin.get().getMOTDConfig();
            MOTDConfig motdConfig = config.get();
            motdConfig.motdMessages = rawMOTDs.toArray(new String[0]);
            config.save();
        }
    }

    public static Message getMOTD() {
        if (motdMessages.isEmpty()) {
            return Message.empty();
        }
        int index = random.nextInt(motdMessages.size());
        return motdMessages.get(index);
    }
}
