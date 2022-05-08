package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.render.RenderModelEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DebugCheat extends Cheat {

    public DebugCheat() {
        super("Debug", Category.OTHER);
    }

    @Override
    public void onEnable() {
        VoxMod.get().setDebugMode(true);
    }

    @Override
    public void onDisable() {
        VoxMod.get().setDebugMode(false);
    }

    @SubscribeEvent
    public void onRenderEntity(RenderModelEvent event) {
        if (!(event.getLivingBase() instanceof EntityPlayer)) return;
        if (event.isPost()) {
        }
    }
}
