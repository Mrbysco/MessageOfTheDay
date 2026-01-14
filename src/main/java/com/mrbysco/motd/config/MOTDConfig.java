package com.mrbysco.motd.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MOTDConfig {
	public static final BuilderCodec<MOTDConfig> CODEC = BuilderCodec.builder(
					MOTDConfig.class, MOTDConfig::new
			)

			.append(new KeyedCodec<>(
							"SendRandomMOTD", Codec.BOOLEAN), (config, value) ->
							config.sendRandomMOTD = value,
					(config) -> config.sendRandomMOTD)
			.documentation("Whether to send a random MOTD from the list every message in order.").add()

			.append(new KeyedCodec<>(
							"MOTDMessages", Codec.STRING_ARRAY), (config, value) ->
							config.motdMessages = value,
					(config) -> config.motdMessages)
			.documentation("An array of messages to display as the MOTD.").add()

			.append(new KeyedCodec<>(
							"SendEveryRepeatingMessage", Codec.BOOLEAN), (config, value) ->
							config.sendEveryRepeatingMessage = value,
					(config) -> config.sendEveryRepeatingMessage)
			.documentation("Whether to send every message in the repeating messages list at set intervals.").add()

			.append(new KeyedCodec<>(
							"IntervalBetweenRepeatingMessages", Codec.INTEGER), (config, value) ->
							config.intervalBetweenRepeatingMessages = value,
					(config) -> config.intervalBetweenRepeatingMessages)
			.documentation("The interval (in seconds) between sending repeating messages.").add()

			.append(new KeyedCodec<>(
							"RepeatingMessages", Codec.STRING_ARRAY), (config, value) ->
							config.repeatingMessages = value,
					(config) -> config.repeatingMessages)
			.documentation("An array of messages to display as repeating messages.").add()

			.build();


	public boolean sendRandomMOTD = true;
	public String[] motdMessages = {
			"<color=#00FF00><b>Welcome</b></color> to the <i>Server</i>!",
			"<b>Rules:</b> Be kind, no griefing, have fun!"
	};

	public boolean sendEveryRepeatingMessage = false;
	public int intervalBetweenRepeatingMessages = 300; // in seconds
	public String[] repeatingMessages = {};
}
