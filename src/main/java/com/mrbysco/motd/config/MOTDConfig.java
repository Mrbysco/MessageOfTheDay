package com.mrbysco.motd.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;

public class MOTDConfig {
    public static final BuilderCodec<MOTDConfig> CODEC = BuilderCodec.builder(
                    MOTDConfig.class, MOTDConfig::new
            )
            .append(new KeyedCodec<>(
                            "MOTDMessages", Codec.STRING_ARRAY), (config, value) ->
                            config.motdMessages = value,
                    (config) -> config.motdMessages)
            .documentation("An array of messages to display as the MOTD.").add()
            .build();


    public String[] motdMessages = {
            "<color=#00FF00><b>Welcome</b></color> to the <i>Server</i>!",
            "<b>Rules:</b> Be kind, no griefing, have fun!"
    };
}
