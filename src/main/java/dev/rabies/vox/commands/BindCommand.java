package dev.rabies.vox.commands;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.utils.misc.ChatUtil;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {

    public BindCommand() {
        super("bind", "b");
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length <= 0) {
            ChatUtil.info("\2477 - Usage");
            ChatUtil.info(" :bind \2477<\247eCheat\2477> \2477<\247eKeyCode\2477>");
            ChatUtil.info(" :bind \2477<\247eCheat\2477> \2477<\247eKeyCode\2477> \2477<\247eType\2477>");
            return;
        }

        String first = args[0];
        CheatWrapper cheat = VoxMod.get().getCheatByName(first);
        if (cheat == null) {
            ChatUtil.error(String.format("%s was not found", first));
            return;
        }

        int keyCode = Keyboard.getKeyIndex(args[1].toUpperCase());
        if (args.length > 2) {
            cheat.getBind().update(keyCode, args[2]);
        } else {
            cheat.getBind().setKeyCode(keyCode);
        }
        ChatUtil.info("Updated key bindings");
    }
}
