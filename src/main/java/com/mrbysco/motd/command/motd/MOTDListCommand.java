package com.mrbysco.motd.command.motd;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandUtil;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.permissions.HytalePermissions;
import com.mrbysco.motd.data.MOTDDatabase;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class MOTDListCommand extends CommandBase {
    public MOTDListCommand() {
        super("list", "Lists all MOTD messages with their indexes");
        this.setPermissionGroup(GameMode.Creative);
        this.requirePermission(HytalePermissions.fromCommand("motd.list"));
    }

    @Override
    protected void executeSync(@NonNullDecl CommandContext commandContext) {
        CommandUtil.requirePermission(commandContext.sender(), HytalePermissions.fromCommand("motd.list"));

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
