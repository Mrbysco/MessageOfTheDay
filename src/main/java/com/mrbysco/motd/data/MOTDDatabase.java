package com.mrbysco.motd.data;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.receiver.IMessageReceiver;
import com.hypixel.hytale.server.core.util.Config;
import com.mrbysco.motd.MOTDPlugin;
import com.mrbysco.motd.config.MOTDConfig;
import com.mrbysco.motd.util.MOTDParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class MOTDDatabase {
	private static final Random random = new Random();
	private static boolean sendRandomMOTD = true;
	private static List<String> rawMOTDs = new ArrayList<>();
	private static List<Message> motdMessages = new ArrayList<>();

	private static Map<UUID, Integer> playerRepeatingIndices = new HashMap<>();
	private static boolean sendEveryRepeatingMessage = false;
	private static int intervalBetweenRepeatingMessages = 300;
	private static List<String> rawRepeating = new ArrayList<>();
	private static List<Message> repeatingMessages = new ArrayList<>();

	public static void populateList(MOTDConfig config) {
		sendRandomMOTD = config.sendRandomMOTD;
		motdMessages.clear();
		for (String motd : config.motdMessages) {
			rawMOTDs.add(motd);
			Message message = MOTDParser.parse(motd);
			motdMessages.add(message);
		}

		sendEveryRepeatingMessage = config.sendEveryRepeatingMessage;
		intervalBetweenRepeatingMessages = config.intervalBetweenRepeatingMessages;
		repeatingMessages.clear();
		for (String motd : config.repeatingMessages) {
			rawRepeating.add(motd);
			Message message = MOTDParser.parse(motd);
			repeatingMessages.add(message);
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

	public static int getIntervalBetweenRepeatingMessages() {
		return intervalBetweenRepeatingMessages;
	}

	/**
	 * Sends repeating messages to the specified message receiver.
	 *
	 * @param messageReceiver The message receiver to send the repeating messages to.
	 */
	public static void sendRepeatingMessage(IMessageReceiver messageReceiver, UUID uuid) {
		if (sendEveryRepeatingMessage) {
			for (Message repeatingMessage : repeatingMessages) {
				messageReceiver.sendMessage(repeatingMessage);
			}
		} else {
			int index = playerRepeatingIndices.getOrDefault(uuid, 0);
			if (repeatingMessages.isEmpty()) {
				return;
			}
			Message message = repeatingMessages.get(index);
			messageReceiver.sendMessage(message);
			index = (index + 1) % repeatingMessages.size();
			playerRepeatingIndices.put(uuid, index);
		}
	}

	/**
	 * Sends the MOTD to the specified player.
	 *
	 * @param messageReceiver The player to send the MOTD to.
	 */
	public static void sendMOTD(IMessageReceiver messageReceiver) {
		if (sendRandomMOTD) {
			messageReceiver.sendMessage(getMOTD());
		} else {
			for (Message motd : motdMessages) {
				messageReceiver.sendMessage(motd);
			}
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
