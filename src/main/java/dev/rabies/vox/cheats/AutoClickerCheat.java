package dev.rabies.vox.cheats;

import dev.rabies.vox.cheats.setting.KeyBind;
import dev.rabies.vox.events.UpdateEvent;
import dev.rabies.vox.utils.TimerUtil;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class AutoClickerCheat extends Cheat {

    private final TimerUtil timerUtil = new TimerUtil();
    private float nextDelay;

    public AutoClickerCheat() {
        super("AutoClicker", KeyBind.fromKey(Keyboard.KEY_R));
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        // TODO: 色々出来るようにします
        if (mc.isGamePaused()) return;
        if (!mc.inGameHasFocus) return;
        if (!mc.gameSettings.keyBindAttack.isKeyDown()) return;
        
        // TODO: TimingCheck
        // pre attack
        if (event.isPost()) return;
        updateDelay();
        
        if (!timerUtil.delay(nextDelay)) return;
        legitAttack();
        timerUtil.reset();
    }

    private void legitAttack() {
        int attackKey = mc.gameSettings.keyBindAttack.getKeyCode();
        Mouse.poll(); // ?
        KeyBinding.setKeyBindState(attackKey, true);
        KeyBinding.onTick(attackKey);
        KeyBinding.setKeyBindState(attackKey, false);
    }
    
    private void updateDelay() {
        float middleCps = 6; // TODO
        float minCps = middleCps - 2;
        float maxCps = middleCps + 2;
    	nextDelay = 1000.0F / RandomUtils.nextFloat(minCps, maxCps);
    }
}
