package dev.rabies.vox.commands.commands;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.commands.Command;

public class RefreshCommand extends Command {

	public RefreshCommand() {
		super("refresh", "r");
	}

	@Override
	public void execute(String[] args) {
		VoxMod.get().getConfigManager().reloadConfigs();
		VoxMod.get().getConfigManager().loadConfig(VoxMod.get().getLastLoadConfig());
	}
}
