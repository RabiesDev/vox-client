package dev.rabies.vox.modules.modules;

import org.lwjgl.input.Mouse;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.misc.ChatHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoToolModule extends Module {
    private final BooleanSetting switchBackSetting = registerSetting(BooleanSetting.builder()
            .name("Switch back")
            .value(true)
            .build()
    );

    private int lastSlot = -1;

    public AutoToolModule() {
        super("Auto Tool", Category.RAGE);
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (mc.currentScreen == null && mc.objectMouseOver != null && Mouse.isButtonDown(0)) {
            if (mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) return;
            IBlockState block = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
            float strength = 1;
            int bestToolSlot = -1;
            for (int i = 36; i < 45; i++) {
                ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
                if (itemStack.getDestroySpeed(block) > strength) {
                    strength = itemStack.getDestroySpeed(block);
                    bestToolSlot = i - 36;
                }
            }

            if (bestToolSlot != -1) {
                if (bestToolSlot != lastSlot) {
                    lastSlot = mc.player.inventory.currentItem;
                }

                mc.player.inventory.currentItem = bestToolSlot;
                mc.playerController.updateController();
                if (VoxMod.isDebugMode()) {
                    ChatHelper.info("Switched to " + mc.player.inventory.getCurrentItem().getDisplayName());
                }
            }
        } else if (switchBackSetting.getValue() && lastSlot != -1) {
            mc.player.inventory.currentItem = lastSlot;
            lastSlot = -1;
        }
    }
}
