package dev.rabies.vox.commands;

import com.mojang.brigadier.CommandDispatcher;
import dev.rabies.vox.commands.commands.BindCommand;
import dev.rabies.vox.commands.commands.ConfigCommand;
import dev.rabies.vox.commands.commands.HelpCommand;
import dev.rabies.vox.commands.commands.ToggleCommand;
import lombok.Getter;
import net.minecraft.command.ICommandSender;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private static final Map<Class<? extends Command>, Command> instanceMap = new HashMap<>();
    @Getter
    private static final CommandDispatcher<ICommandSender> dispatcher = new CommandDispatcher<>();

    public static void initialize() {
        instanceMap.put(BindCommand.class, new BindCommand());
        instanceMap.put(ConfigCommand.class, new ConfigCommand());
        instanceMap.put(HelpCommand.class, new HelpCommand());
        instanceMap.put(ToggleCommand.class, new ToggleCommand());

        for (Command command : getCommands()) {
            command.registerTo(dispatcher);
        }
    }

    public static Collection<Command> getCommands() {
        return instanceMap.values();
    }
}
