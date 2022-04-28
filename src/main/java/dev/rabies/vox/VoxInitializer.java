package dev.rabies.vox;

import dev.rabies.vox.cheats.*;
import dev.rabies.vox.cheats.cheats.AutoClickerCheat;
import dev.rabies.vox.cheats.cheats.AutoSprintCheat;
import dev.rabies.vox.cheats.cheats.DebugCheat;
import dev.rabies.vox.cheats.cheats.HudCheat;
import dev.rabies.vox.commands.BindCommand;
import dev.rabies.vox.commands.Command;
import dev.rabies.vox.commands.HelpCommand;
import dev.rabies.vox.commands.ToggleCommand;
import dev.rabies.vox.config.ConfigManager;
import dev.rabies.vox.render.RenderHook;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VoxInitializer implements Initializer {

    @Getter
    private final ArrayList<Cheat> cheats = new ArrayList<>();
    @Getter
    private final ArrayList<Command> commands = new ArrayList<>();
    @Getter
    private ConfigManager configManager;

    @Override
    public void initialize(FMLInitializationEvent event) {
        registerCheats();
        registerCommands();
    }

    @Override
    public void postInitialize(FMLPostInitializationEvent event) {
        configManager = new ConfigManager();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        MinecraftForge.EVENT_BUS.register(new RenderHook());
    }

    private void registerCheats() {
        Collections.addAll(cheats,
                new DebugCheat(),
                new HudCheat(),
                new AutoSprintCheat(),
                new AutoClickerCheat()
        );
    }

    private void registerCommands() {
        Collections.addAll(commands,
                new HelpCommand(),
                new ToggleCommand(),
                new BindCommand()
        );
    }

    public Cheat getCheatByName(String name) {
        return cheats.stream().filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public List<Cheat> getCheatsByCategory(Category category) {
        return cheats.stream().filter(it -> it.getCategory() == category)
                .collect(Collectors.toList());
    }
}
