package com.mrbysco.motd.command;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.mrbysco.motd.data.MOTDDatabase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class MOTDListCommand extends CommandBase {
    public MOTDListCommand() {
        super("list", "Lists all MOTD messages with their indexes");
        this.requirePermission(HytalePermissions.fromCommand("motd.list"));
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        var motdList = MOTDDatabase.getMOTDList();
        if (motdList.isEmpty()) {
            commandContext.sendMessage(Message.raw("The MOTD list is currently empty.").bold(true));
        } else {
            commandContext.sendMessage(Message.raw("MOTD Messages:").bold(true));
            for (int i = 0; i < motdList.size(); i++) {
                commandContext.sendMessage(Message.raw(i + ": " ).insert(motdList.get(i)));
            }
        }
    }
}
