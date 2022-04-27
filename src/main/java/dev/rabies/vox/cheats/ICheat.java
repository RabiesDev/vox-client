package dev.rabies.vox.cheats;

public interface ICheat {

    default void onEnable() { }

    default void onDisable() { }

    void toggle();
}
