package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReachModule extends Module {
	public final BooleanSetting likeLegitSetting = registerSetting(BooleanSetting.builder()
			.name("Like Legit")
			.value(true)
			.build()
	);

	public final NumberSetting reachSetting = registerSetting(NumberSetting.builder()
			.name("Reach")
			.value(3.8)
			.min(3.0)
			.max(10.0)
			.interval(0.1)
			.build()
	);

	public ReachModule() {
		super("Reach", Category.LEGIT);
	}

	@SubscribeEvent
	public void onUpdate(UpdateEvent event) {
		suffix = reachSetting.getValue();
	}
}
