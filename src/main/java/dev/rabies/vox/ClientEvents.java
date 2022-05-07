package dev.rabies.vox;

import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.events.render.Render3DEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class ClientEvents {

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		Render3DEvent render3DEvent = new Render3DEvent(event.getPartialTicks());
		MinecraftForge.EVENT_BUS.post(render3DEvent);
	}

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
    	if (Minecraft.getMinecraft().world == null) return;
    	if (!Keyboard.isCreated()) return;

        boolean state = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        if (key == 0) return;
        for (Cheat cheat : VoxMod.get().getCheats()) {
        	if (key != cheat.getBind().getKeyCode()) continue;
        	switch (cheat.getBind().getType()) {
        		case TOGGLE:
        			if (!state) return;
        			cheat.toggle();
        			break;
        		
        		case HOLD:
					// ~^-^~
					if (state && !cheat.isEnabled()) {
						cheat.toggle();
					} else if (!state && cheat.isEnabled()) {
						cheat.toggle();
					}
        			break;
        	}
        }
    }
}
