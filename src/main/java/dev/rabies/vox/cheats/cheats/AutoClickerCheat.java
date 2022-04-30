package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.KeyBind;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.Render2DEvent;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.utils.PlayerUtils;
import dev.rabies.vox.utils.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

public class AutoClickerCheat extends Cheat {

    private final BoolSetting leftClickSetting = registerBoolSetting("Left click", true);
    private final NumberSetting leftCpsSetting = registerNumberSetting("Left Cps", 7, 1, 30, 1,
            leftClickSetting::getValue);

    private final BoolSetting rightClickSetting = registerBoolSetting("Right click", true);
    private final NumberSetting rightCpsSetting = registerNumberSetting("Right Cps", 7, 1, 30, 1,
            rightClickSetting::getValue);

    private final BoolSetting renderSetting = registerBoolSetting("Show info", true,
            leftClickSetting::getValue);

    private final TimerUtil leftTimerUtil = new TimerUtil();
    private final TimerUtil rightTimerUtil = new TimerUtil();
    private float leftClickNextDelay;
    private float rightClickNextDelay;

    private boolean attackable;
    private boolean attacked;
    private int breakTick;

    public AutoClickerCheat() {
        super("Auto Clicker", Category.LEGIT, KeyBind.fromKey(Keyboard.KEY_R));
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            doLeftClick(event);
        } else {
            leftClickNextDelay = 350;
            leftTimerUtil.reset();
        }

        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            doRightClick(event);
        } else {
            rightClickNextDelay /= 2;
            rightTimerUtil.reset();
        }
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (!(renderSetting.getValue() && renderSetting.isAvailable())) return;
        ScaledResolution resolution = event.getResolution();
        FontRenderer font = mc.fontRenderer;
        String label = attackable ? "\247aAttackable" : "\247cNonAttackable";
        int x = (resolution.getScaledWidth() / 2) - font.getStringWidth(label) / 2;
        int y = resolution.getScaledHeight() / 2 + font.FONT_HEIGHT;
        font.drawStringWithShadow(label, x, y, -1);
    }

    private void doLeftClick(UpdateEvent event) {
        attackable = canClick(true);
        if (!attackable) return;
        if (event.isPost() && attacked) {
            if (mc.player.ticksExisted % RandomUtils.nextInt(2, 3) != 0) return;
            PlayerUtils.holdState(0, false);
            attacked = false;
            return;
        }

        if (!leftTimerUtil.delay(leftClickNextDelay)) return;
        leftClickNextDelay = getNextDelay(leftCpsSetting.getValue().floatValue());
        PlayerUtils.holdState(0, true);
        PlayerUtils.legitAttack();
        attacked = true;
    }

    private void doRightClick(UpdateEvent event) {
        if (!canClick(false)) return;
        if (event.isPost() && attacked) {
            if (mc.player.ticksExisted % RandomUtils.nextInt(2, 3) != 0) return;
            PlayerUtils.holdState(1, false);
        }

        if (rightTimerUtil.delay(rightClickNextDelay)) return;
        rightClickNextDelay = getNextDelay(rightCpsSetting.getValue().floatValue());
        PlayerUtils.holdState(1, true);
        mc.rightClickMouse();
    }
    
    private float getNextDelay(float middleCps) {
        float minCps = middleCps - 2;
        float maxCps = middleCps + 2;
        float cps = RandomUtils.nextFloat(minCps, maxCps);
        setSuffix((int) cps);
        return cps;
    }

    public boolean canClick(boolean left) {
        if (mc.isGamePaused()) return false;
        if (!mc.inGameHasFocus) return false;
        if (mc.objectMouseOver != null && left) {
            RayTraceResult result = mc.objectMouseOver;
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = result.getBlockPos();
                Block block = mc.world.getBlockState(blockPos).getBlock();
                if (block instanceof BlockAir) return true;
                if (block instanceof BlockLiquid) return true;
                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                    if (breakTick > 1) return false;
                    breakTick++;
                } else {
                    breakTick = 0;
                }
            } else {
                breakTick = 0;
            }
        }
        return true;
    }
}
