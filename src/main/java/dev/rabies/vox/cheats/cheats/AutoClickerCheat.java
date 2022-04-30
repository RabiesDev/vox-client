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

    private final NumberSetting cpsSetting = registerNumberSetting("Cps", 7, 1, 30, 1);
    private final BoolSetting renderSetting = registerBoolSetting("Show info", true);

    private final TimerUtil timerUtil = new TimerUtil();
    private boolean attackable;
    private boolean attacked;
    private float nextDelay;
    private int breakTick;

    public AutoClickerCheat() {
        super("Auto Clicker", Category.LEGIT, KeyBind.fromKey(Keyboard.KEY_R));
    }
    
    @Override
    public void onEnable() {
    	updateDelay();
    	attacked = false;
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        attackable = canClick();
        if (!attackable) return;
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
        	nextDelay = 350;
        	timerUtil.reset();
        	return;
        }
        
        if (event.isPost() && attacked) {
        	if (mc.player.ticksExisted % RandomUtils.nextInt(2, 3) != 0) return;
        	PlayerUtils.holdState(false);
        	attacked = false;
        	return;
        }

    	if (!timerUtil.delay(nextDelay)) return;
        PlayerUtils.holdState(true);
        PlayerUtils.legitAttack();
        attacked = true;
        updateDelay();
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        if (!renderSetting.getValue()) return;
        ScaledResolution resolution = event.getResolution();
        FontRenderer font = mc.fontRenderer;

        String label1 = attackable ? "\247aAttackable" : "\247cNonAttackable";
        int x = (resolution.getScaledWidth() / 2) - font.getStringWidth(label1) / 2;
        int y = resolution.getScaledHeight() / 2 + font.FONT_HEIGHT;
        font.drawStringWithShadow(label1, x, y, -1);
    }
    
    private void updateDelay() {
        float middleCps = cpsSetting.getValue().floatValue();
        float minCps = middleCps - 2;
        float maxCps = middleCps + 2;
        float cps = RandomUtils.nextFloat(minCps, maxCps);
    	nextDelay = 800.0F / cps;
    	timerUtil.reset();
        setSuffix((int) cps);
    }

    public boolean canClick() {
        if (mc.isGamePaused()) return false;
        if (!mc.inGameHasFocus) return false;
        if (mc.objectMouseOver != null) {
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
