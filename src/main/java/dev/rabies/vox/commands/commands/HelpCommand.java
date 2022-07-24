package dev.rabies.vox.commands.commands;

import dev.rabies.vox.Constants;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.commands.Command;
import dev.rabies.vox.utils.misc.ChatUtil;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "h");
    }

    @Override
    public void execute(String[] args) {
        ChatUtil.info(String.format("\247aVox \247fv%s", Constants.VERSION));
        ChatUtil.info(String.format("\2479Git\2477hub\247f: %s", Constants.URL));
        ChatUtil.info("");
        VoxMod.get().getCommands().forEach(it -> ChatUtil.info(" \2477:" + it.getAliases()[0]));
    }
}
