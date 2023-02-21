package dev.rabies.vox;

import dev.rabies.vox.modules.Module;
import dev.rabies.vox.events.render.RenderWorldEvent;
import dev.rabies.vox.modules.ModuleRegistry;
import dev.rabies.vox.settings.KeyBinding;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class ClientEvents {
	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		RenderWorldEvent renderWorldEvent = new RenderWorldEvent(event.getPartialTicks());
		MinecraftForge.EVENT_BUS.post(renderWorldEvent);
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		boolean active = Keyboard.getEventKeyState();
		int keyCode = Keyboard.getEventKey();
		if (keyCode == Keyboard.KEY_NONE) return;
		for (Module module : ModuleRegistry.getModules()) {
			if (keyCode == module.getBind().getKeyCode()) {
				if (active && module.getBind().getType() == KeyBinding.BindType.TOGGLE) {
					module.toggle();
				} else if (module.getBind().getType() == KeyBinding.BindType.HOLD) {
					if (active && !module.isToggled()) {
						module.toggle();
					} else if (!active && module.isToggled()) {
						module.toggle();
					}
				}
			}
		}
	}
}
