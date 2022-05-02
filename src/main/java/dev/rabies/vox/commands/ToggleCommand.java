package dev.rabies.vox.commands;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.utils.misc.ChatUtil;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "t");
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length <= 0) {
            ChatUtil.info("\2477 - Usage");
            ChatUtil.info(" :toggle \2477<\247eCheat\2477>");
            return;
        }

        String first = args[0];
        Cheat cheat = VoxMod.get().getCheatByName(first);
        if (cheat == null) {
            ChatUtil.error(String.format("%s was not found", first));
            return;
        }

        cheat.toggle();
        ChatUtil.info(String.format("\2479%s\247f has been %s",
                cheat.getName(),
                cheat.isEnabled() ? "\247aenabled" : "\247cdisabled"
        ));
    }
}
