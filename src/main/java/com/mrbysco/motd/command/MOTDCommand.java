package com.mrbysco.motd.command;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.mrbysco.motd.data.MOTDDatabase;

import javax.annotation.Nonnull;

public class MOTDCommand extends CommandBase {

    public MOTDCommand() {
        super("motd", "Prints the MOTD to the chat");
        this.setPermissionGroup(GameMode.Adventure);
        this.addSubCommand(new MOTDAddCommand());
        this.addSubCommand(new MOTDListCommand());
        this.addSubCommand(new MOTDRemoveCommand());
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(MOTDDatabase.getMOTD());
    }
}