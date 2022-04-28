package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.KeyBind;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.utils.PlayerUtils;
import dev.rabies.vox.utils.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

public class AutoClickerCheat extends Cheat {

    private final TimerUtil timerUtil = new TimerUtil();
    private boolean attacked;
    private float nextDelay;
    private int breakTick;

    public AutoClickerCheat() {
        super("AutoClicker", Category.LEGIT, KeyBind.fromKey(Keyboard.KEY_R));
    }
    
    @Override
    public void onEnable() {
    	updateDelay();
    	attacked = false;
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (!canClick()) return;
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) {
        	if (nextDelay != -1F) {
        		nextDelay = 320;
            	timerUtil.reset();
        	}
        	
        	nextDelay = -1F;
        	return;
        }
        
        if (event.isPost() && attacked) {
        	if (mc.player.ticksExisted % 3 != 0) return;
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
    
    private void updateDelay() {
        float middleCps = 9; // TODO
        float minCps = middleCps - 2;
        float maxCps = middleCps + 2;
    	nextDelay = 1000.0F / RandomUtils.nextFloat(minCps, maxCps);
    	timerUtil.reset();
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
                    if (breakTick > 2) return false;
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
