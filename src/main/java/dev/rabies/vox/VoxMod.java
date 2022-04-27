package dev.rabies.vox;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.VERSION)
public class VoxMod {

    @Mod.Instance(Constants.MOD_ID)
    public static VoxMod INSTANCE;

    public static Initializer initializer = new VoxInitializer();

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
