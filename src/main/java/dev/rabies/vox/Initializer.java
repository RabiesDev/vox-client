package dev.rabies.vox;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface Initializer {

    default void preInitialize(FMLPreInitializationEvent event) {}

    default void postInitialize(FMLPostInitializationEvent event) {}

    void initialize(FMLInitializationEvent event);
}
