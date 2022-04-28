package dev.rabies.vox.commands;

import dev.rabies.vox.Constants;
import dev.rabies.vox.VoxMod;
import dev.rabies.vox.utils.ChatUtils;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "h");
    }

    @Override
    public void execute(String[] args) {
        ChatUtils.info(String.format("\247aVox \247fv%s", Constants.VERSION));
        ChatUtils.info(String.format("\2479Git\2477hub\247f: %s", Constants.URL));
        ChatUtils.info("");
        VoxMod.get().getCommands().forEach(it -> ChatUtils.info(" \2477:" + it.getName()));
    }
}
