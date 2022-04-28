package dev.rabies.vox.commands;

import dev.rabies.vox.utils.ChatUtils;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "h");
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length <= 0) {
            ChatUtils.info("help");
        } else {
            ChatUtils.info("help " + args.length);
        }
    }
}
