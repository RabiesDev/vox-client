package dev.rabies.vox.commands.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.lwjgl.input.Keyboard;

public class KeyArgumentType implements ArgumentType<Integer> {
    @Override
    public Integer parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        return Keyboard.getKeyIndex(argument.toUpperCase());
    }
}
