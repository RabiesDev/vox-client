package dev.rabies.vox.mixins;

import dev.rabies.vox.utils.misc.SessionUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method = "createButtons", at = @At("RETURN"))
    public void createButtons(CallbackInfo ci) {
        GuiButton clipLogin = new GuiButton(10, this.width / 2 + 4 + 76 * 2, this.height - 28, 75, 20, "Clip Login");
        this.buttonList.add(clipLogin);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    protected void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.enabled && button.id == 10) {
            String clipboard = getClipboardString();
            String[] split = clipboard.contains(":") ? clipboard.split(":") : new String[]{clipboard};
            if (split.length != (clipboard.contains("@alt.com") ? 1 : 2)) return;
            boolean result;
            if (clipboard.contains("@alt.com")) {
                result = SessionUtils.newSession().theAltening(split[0].trim()).login();
            } else {
                result = SessionUtils.newSession().username(split[0].trim()).password(split[1].trim()).login();
            }
            System.out.println(result);
        }
    }
}
