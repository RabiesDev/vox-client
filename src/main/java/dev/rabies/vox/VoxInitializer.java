package dev.rabies.vox;

import dev.rabies.vox.cheats.AutoClickerCheat;
import dev.rabies.vox.cheats.AutoSprintCheat;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.DebugCheat;
import dev.rabies.vox.commands.Command;
import dev.rabies.vox.commands.HelpCommand;
import dev.rabies.vox.render.UIHook;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.ArrayList;

public class VoxInitializer implements Initializer {

    @Getter
    private final ArrayList<Cheat> cheats = new ArrayList<>();
    @Getter
    private final ArrayList<Command> commands = new ArrayList<>();

    @Override
    public void initialize(FMLInitializationEvent event) {
        registerCheats();
        registerCommands();
    }

    @Override
    public void postInitialize(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new UIHook());
    }

    private void registerCheats() {
        cheats.add(new DebugCheat());
        cheats.add(new AutoSprintCheat());
        cheats.add(new AutoClickerCheat());
    }

    private void registerCommands() {
        commands.add(new HelpCommand());
    }

    public Cheat getCheatByName(String name) {
        return cheats.stream().filter(it -> it.getName().equals(name))
                .findFirst().orElse(null);
    }
}
