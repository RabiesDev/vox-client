package dev.rabies.vox.cheats.cheats;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.KeyBind;
import dev.rabies.vox.cheats.setting.ModeSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.Render2DEvent;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.utils.PlayerUtils;
import dev.rabies.vox.utils.ServerUtil;
import dev.rabies.vox.utils.misc.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoClickerCheat extends Cheat {

    enum Mode {
        Normal, Wave
    }

    private final ModeSetting<Mode> modeSetting = registerModeSetting("Mode", Mode.Normal);

    private final BoolSetting teamCheckSetting = registerBoolSetting("Team check", true);
    private final BoolSetting itemInUseSetting = registerBoolSetting("Item in use", false);

    private final BoolSetting leftClickSetting = registerBoolSetting("Left click", true);
    private final NumberSetting leftCpsSetting = registerNumberSetting("Left Cps", 7, 1, 30, 1,
            leftClickSetting::getValue);

    private final BoolSetting rightClickSetting = registerBoolSetting("Right click", true);
    private final NumberSetting rightCpsSetting = registerNumberSetting("Right Cps", 18, 1, 30, 1,
            rightClickSetting::getValue);

    private final BoolSetting renderSetting = registerBoolSetting("Show info", true,
            leftClickSetting::getValue);

    private final SystemFontRenderer infoFont = VoxMod.get().newSystemFont("Mukta-Bold", 18);
    private final TimerUtil leftTimerUtil = new TimerUtil();
    private final TimerUtil rightTimerUtil = new TimerUtil();
    private float leftClickNextDelay;
    private float rightClickNextDelay;

    private boolean attackable;
    private boolean attacked;
    private boolean clicked;
    private int breakTick;

    public AutoClickerCheat() {
        super("Auto Clicker", Category.LEGIT, KeyBind.fromKey(Keyboard.KEY_R));
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (leftClickSetting.getValue()) {
            attackable = canClick(true);
            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                doLeftClick();
            } else {
                leftClickNextDelay = 320;
                leftTimerUtil.reset();
            }
        }

        if (rightClickSetting.getValue()) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                doRightClick();
            } else {
                rightClickNextDelay = RandomUtils.nextFloat(70, 150);
                rightTimerUtil.reset();
            }
        }
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (!(renderSetting.getValue() && renderSetting.isAvailable())) return;
        if (mc.gameSettings.thirdPersonView != 0) return;
        ScaledResolution resolution = event.getResolution();
        String label = attackable ? "\247aAttackable" : "\247cNonAttackable";
        double y = (resolution.getScaledHeight() / 2.0) + infoFont.getHeight() + 2.0;
        infoFont.drawCenteredStringWithShadow(label, resolution.getScaledWidth() / 2.0, y, -1);
    }

    private void doLeftClick() {
        if (!attackable) return;
        float cps = leftCpsSetting.getValue().floatValue();

        switch (modeSetting.getValue()) {
            case Normal:
                if (attacked) {
                    if (mc.player.ticksExisted % RandomUtils.nextInt(1, 3) != 0) return;
                    PlayerUtils.holdState(0, false);
                    attacked = false;
                    return;
                }

                if (!leftTimerUtil.delay(leftClickNextDelay)) return;
                leftClickNextDelay = getNextDelay(cps);
                leftTimerUtil.reset();
                break;

            case Wave:
                if (attacked) {
                    PlayerUtils.holdState(0, false);
                    if (mc.player.ticksExisted % RandomUtils.nextInt(14, 16) == 0) {
                    	leftClickNextDelay = getNextDelay(cps) * 3.2f;
                        leftTimerUtil.reset();
                    } else {
	                    leftClickNextDelay = getNextDelay(cps) / 1.24f;
	                    leftTimerUtil.reset();
                    }
                    attacked = false;
                }

                if (!leftTimerUtil.delay(leftClickNextDelay)) return;
        }

        PlayerUtils.holdState(0, true);
        PlayerUtils.legitAttack();
        attacked = true;
    }

    private void doRightClick() {
        if (!canClick(false)) return;
        float cps = rightCpsSetting.getValue().floatValue();
        
        switch (modeSetting.getValue()) {
            case Normal:
                if (clicked) {
                    if (mc.player.ticksExisted % RandomUtils.nextInt(2, 4) != 0) return;
                    PlayerUtils.holdState(1, false);
                    clicked = false;
                    return;
                }

                if (!rightTimerUtil.delay(rightClickNextDelay)) return;
                rightClickNextDelay = getNextDelay(cps);
                rightTimerUtil.reset();
                break;

            case Wave:
                if (clicked) {
                    PlayerUtils.holdState(1, false);
                    if (mc.player.ticksExisted % RandomUtils.nextInt(11, 12) == 0) {
                    	rightClickNextDelay = getNextDelay(cps) * 1.6f;
                        rightTimerUtil.reset();
                    } else {
	                    rightClickNextDelay = getNextDelay(cps) / 1.1f;
	                    rightTimerUtil.reset();
                    }
                    clicked = false;
                }

                if (!rightTimerUtil.delay(rightClickNextDelay)) return;
        }

        PlayerUtils.holdState(1, true);
        PlayerUtils.legitClick();
        clicked = true;
    }
    
    private float getNextDelay(float middleCps) {
        float minCps = middleCps - 2;
        float maxCps = middleCps + 2;
        float cps = RandomUtils.nextFloat(minCps, maxCps);
        if (cps < 1) cps = 1;
        setSuffix((int) cps);
        return 820.0F / cps;
    }

    public boolean canClick(boolean left) {
        if (mc.isGamePaused()) return false;
        if (!mc.inGameHasFocus) return false;
        if (mc.player.getItemInUseCount() > 0 &&
                !itemInUseSetting.getValue()) return false;
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

                if (result.typeOfHit == RayTraceResult.Type.ENTITY && teamCheckSetting.getValue()) {
                    Entity entity = result.entityHit;
                    if (entity.isDead) return false;
                    if (!(entity instanceof EntityPlayer)) return true;
                    if (entity == mc.player) return false;
                    return !ServerUtil.isTeams((EntityPlayer) entity);
                }
            }
        }
        return true;
    }
}
