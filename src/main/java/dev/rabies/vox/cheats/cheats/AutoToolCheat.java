package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.utils.ChatUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoToolCheat extends Cheat {

    public AutoToolCheat() {
        super("Auto Tool", Category.RAGE);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen != null) return;
        if (mc.objectMouseOver == null) return;
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) return;
        if (mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) return;
        IBlockState block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
        float strength = 1.0F;
        int bestToolSlot = -1;
        for (int i = 36; i < 45; i++) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getDestroySpeed(block) > strength) {
                strength = itemStack.getDestroySpeed(block);
                bestToolSlot = i - 36;
            }
        }

        if (bestToolSlot != -1) {
            mc.player.inventory.currentItem = bestToolSlot;
            mc.playerController.updateController();
            if (VoxMod.get().isDebugMode()) {
                ChatUtils.info("Switched to " + mc.player.inventory.getCurrentItem().getDisplayName());
            }
        }
    }
}
