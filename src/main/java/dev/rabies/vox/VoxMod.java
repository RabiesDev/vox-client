package dev.rabies.vox;

import dev.rabies.vox.utils.misc.ModFile;
import lombok.Getter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class VoxMod {

    private static final Initializer initializer = new VoxInitializer();
    @Getter
    private static final ModFile modFolder = new ModFile(new File(Constants.MOD_NAME), true);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        initializer.preInitialize(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        initializer.initialize(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        initializer.postInitialize(event);
    }

    public static VoxInitializer get() {
        return (VoxInitializer) initializer;
    }
}
