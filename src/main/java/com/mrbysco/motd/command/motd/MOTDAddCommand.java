package com.mrbysco.motd.command.motd;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.mrbysco.motd.data.MOTDDatabase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class MOTDAddCommand extends CommandBase {
    private final RequiredArg<String> messageArg = this.withRequiredArg("message", "The MOTD to add", ArgTypes.STRING);

    public MOTDAddCommand() {
        super("add", "Adds a new MOTD message", true);
        this.setPermissionGroup(GameMode.Creative);
        this.requirePermission(HytalePermissions.fromCommand("motd.add"));
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        String message = this.messageArg.get(commandContext);
        // Remove encasing quotes
        if (message.startsWith("\"") && message.endsWith("\"")) {
            message = message.substring(1, message.length() - 1);
        }
        MOTDDatabase.addMOTD(message);
    }
}
