package dev.rabies.vox.modules.modules;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.settings.KeyBinding;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.PlayerHelper;
import dev.rabies.vox.utils.ServerHelper;
import dev.rabies.vox.utils.misc.StopWatch;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoClickerModule extends Module {
    private final BooleanSetting ignoreFriendsSetting = registerSetting(BooleanSetting.builder()
            .name("Ignore Friends")
            .value(true)
            .build()
    );

    private final BooleanSetting ignoreTeamsSetting = registerSetting(BooleanSetting.builder()
            .name("Ignore Teammate")
            .value(true)
            .build()
    );

    private final BooleanSetting itemInUseSetting = registerSetting(BooleanSetting.builder()
            .name("Item in use")
            .value(false)
            .build()
    );

    private final BooleanSetting leftClickSetting = registerSetting(BooleanSetting.builder()
            .name("Left Click")
            .value(true)
            .build()
    );

    private final NumberSetting leftCpsSetting = registerSetting(NumberSetting.builder()
            .name("Left CPS")
            .value(7)
            .min(1)
            .max(20)
            .interval(1)
            .dependency(leftClickSetting::getValue)
            .build()
    );

    private final BooleanSetting rightClickSetting = registerSetting(BooleanSetting.builder()
            .name("Right Click")
            .value(true)
            .build()
    );

    private final NumberSetting rightCpsSetting = registerSetting(NumberSetting.builder()
            .name("Right CPS")
            .value(7)
            .min(1)
            .max(20)
            .interval(1)
            .dependency(rightClickSetting::getValue)
            .build()
    );

    private final StopWatch leftStopWatch = new StopWatch();
    private final StopWatch rightStopWatch = new StopWatch();

    private boolean attacked;
    private boolean clicked;
    private int breakTick;

    public AutoClickerModule() {
        super("Auto Clicker", Category.LEGIT, KeyBinding.fromKey(Keyboard.KEY_R));
    }

    @Override
    public void onDisable() {
        attacked = false;
        clicked = false;
        breakTick = 0;
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (leftClickSetting.getValue() && mc.gameSettings.keyBindAttack.isKeyDown() && shouldClick(true)) {
            doLeftClick();
        }

        if (rightClickSetting.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown() && shouldClick(false)) {
            doRightClick();
        }
    }

    private void doLeftClick() {
        int cps = leftCpsSetting.getValue().intValue();
        if (attacked && mc.player.ticksExisted % RandomUtils.nextInt(1, 3) == 0) {
            PlayerHelper.holdState(0, false);
            attacked = false;
            return;
        }

        if (!leftStopWatch.finished(calculateTime(cps), true)) {
            return;
        }

        PlayerHelper.holdState(0, true);
        PlayerHelper.legitAttack();
        attacked = true;
    }

    private void doRightClick() {
        int cps = rightCpsSetting.getValue().intValue();
        if (clicked && mc.player.ticksExisted % RandomUtils.nextInt(1, 3) == 0) {
            PlayerHelper.holdState(1, false);
            clicked = false;
            return;
        }

        if (!rightStopWatch.finished(calculateTime(cps), true)) {
            return;
        }

        PlayerHelper.holdState(1, true);
        PlayerHelper.legitClick();
        clicked = true;
    }

    private long calculateTime(int cps) {
        return (long) ((Math.random() * (1000 / (cps - 2) - 1000 / cps + 1)) + 1000 / cps);
    }

    public boolean shouldClick(boolean left) {
        if (mc.isGamePaused() || !mc.inGameHasFocus) {
            return false;
        }

        if (mc.player.getItemInUseCount() > 0 && !itemInUseSetting.getValue()) {
            return false;
        }

        if (mc.objectMouseOver != null && left) {
            RayTraceResult result = mc.objectMouseOver;
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = result.getBlockPos();
                Block block = mc.world.getBlockState(blockPos).getBlock();
                if (block instanceof BlockAir || block instanceof BlockLiquid) {
                    return true;
                }

                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                    if (breakTick > 1) {
                        return false;
                    }
                    breakTick++;
                } else {
                    breakTick = 0;
                }
            } else {
                breakTick = 0;

                if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
                    Entity entity = result.entityHit;
                    if (entity instanceof EntityPlayer && !entity.isDead) {
                        return (!ignoreTeamsSetting.getValue() || !ServerHelper.isTeammate((EntityPlayer) entity))
                                || (!ignoreFriendsSetting.getValue() || !ServerHelper.isFriend((EntityPlayer) entity));
                    }
                }
            }
        }
        return true;
    }
}
