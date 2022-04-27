package dev.rabies.vox;

import dev.rabies.vox.cheats.AutoClickerCheat;
import dev.rabies.vox.cheats.AutoSprintCheat;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.DebugCheat;
import dev.rabies.vox.commands.Command;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.ArrayList;

public class VoxInitializer implements Initializer {

    @Getter
    private final ArrayList<Cheat> cheats = new ArrayList<>();
    @Getter
    private final ArrayList<Command> commands = new ArrayList<>();

    @Override
    public void initialize(FMLInitializationEvent event) {
        registerCheats();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    private void registerCheats() {
        cheats.add(new DebugCheat());
        cheats.add(new AutoSprintCheat());
        cheats.add(new AutoClickerCheat());
    }

    private void registerCommands() {
    }

    public Cheat getCheatByName(String name) {
        return cheats.stream().filter(it -> it.getName().equals(name))
                .findFirst().orElse(null);
    }
}
