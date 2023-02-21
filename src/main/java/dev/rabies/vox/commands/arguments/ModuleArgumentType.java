package dev.rabies.vox.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.modules.ModuleRegistry;

public class ModuleArgumentType implements ArgumentType<Module> {
    @Override
    public Module parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        return ModuleRegistry.fromString(argument);
    }
}
