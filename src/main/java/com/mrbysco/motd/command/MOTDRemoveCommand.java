package com.mrbysco.motd.command;

import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.mrbysco.motd.data.MOTDDatabase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class MOTDRemoveCommand extends CommandBase {
    private final RequiredArg<Integer> index = this.withRequiredArg("index", "The index of the MOTD message to remove", ArgTypes.INTEGER);

    public MOTDRemoveCommand() {
        super("remove", "Removes a MOTD message by index");
        this.requirePermission(HytalePermissions.fromCommand("motd.remove"));
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        int messageIndex = this.index.get(commandContext);
        MOTDDatabase.remove(messageIndex);
    }
}
