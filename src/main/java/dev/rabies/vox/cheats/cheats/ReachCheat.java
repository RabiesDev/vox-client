package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.NumberSetting;

public class ReachCheat extends Cheat {
	
	public final NumberSetting reachSetting = registerNumberSetting("Reach", 3.8, 3.0, 10.0, 0.1);

	public ReachCheat() {
		super("Reach", Category.LEGIT);
	}	
}
