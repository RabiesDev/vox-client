package dev.rabies.vox.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.rabies.vox.Constants;
import dev.rabies.vox.commands.Command;
import dev.rabies.vox.commands.CommandRegistry;
import dev.rabies.vox.utils.misc.ChatHelper;
import net.minecraft.command.ICommandSender;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void build(LiteralArgumentBuilder<ICommandSender> builder) {
        builder.executes(context -> {
            ChatHelper.info(String.format("\247aVox \247fv%s", Constants.VERSION));
            ChatHelper.info(String.format("\2479Github\247f: %s", Constants.GITHUB_URL));
            ChatHelper.info("");
            CommandRegistry.getCommands().forEach(it ->
                    ChatHelper.info(" \2477:" + it.getAliases()[0])
            );
            return 1;
        });
    }
}
