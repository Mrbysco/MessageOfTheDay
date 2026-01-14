package com.mrbysco.motd.command;

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
        super("add", "Adds a new MOTD message");
        this.requirePermission(HytalePermissions.fromCommand("motd.add"));
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        String message = this.messageArg.get(commandContext);
        MOTDDatabase.addMOTD(message);
    }
}
