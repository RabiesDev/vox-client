package dev.rabies.vox.commands;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.utils.ChatUtils;

public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("toggle", "t");
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length <= 0) {
            ChatUtils.info("\2477 - Usage");
            ChatUtils.info(" :toggle \2477<\247eCheat\2477>");
            return;
        }

        String first = args[0];
        Cheat cheat = VoxMod.get().getCheatByName(first);
        if (cheat == null) {
            ChatUtils.error(String.format("%s was not found", first));
            return;
        }

        cheat.toggle();
        ChatUtils.info(String.format("\2479%s\247f has been %s",
                cheat.getName(),
                cheat.isEnabled() ? "\247aenabled" : "\247cdisabled"
        ));
    }
}
