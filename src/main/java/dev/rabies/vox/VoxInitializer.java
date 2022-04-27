package dev.rabies.vox;

import dev.rabies.vox.cheats.AutoClicker;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.DebugCheat;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.ArrayList;

public class VoxInitializer implements Initializer {

    @Getter
    private final ArrayList<Cheat> cheats = new ArrayList<>();

    @Override
    public void initialize(FMLInitializationEvent event) {
        registerCheats();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    private void registerCheats() {
        cheats.add(new DebugCheat());
        cheats.add(new AutoClicker());
    }
}
