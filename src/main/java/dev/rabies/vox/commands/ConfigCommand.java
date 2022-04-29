package dev.rabies.vox.commands;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.config.Config;
import dev.rabies.vox.config.ConfigManager;
import dev.rabies.vox.utils.ChatUtils;

public class ConfigCommand extends Command {

    public ConfigCommand() {
        super("config", "c");
    }

    @Override
    public void execute(String[] args) {
        if (args == null || args.length <= 0) {
            ChatUtils.info("\2477 - Usage");
            ChatUtils.info(" :config \2477<\247eLoad/Save/List\2477> \2477<\247eConfigName\2477>");
            return;
        }

        if (args.length >= 2) {
            String first = args[0];
            String configName = args[1];
            ConfigManager cfm = VoxMod.get().getConfigManager();

            switch (first.toLowerCase().trim()) {
                case "load":
                    cfm.loadConfig(configName);
                    break;

                case "save":
                    cfm.saveConfig(configName, mc.getSession().getUsername());
                    break;

                default:
                    printConfigList();
                    break;
            }
            return;
        }

        printConfigList();
    }

    private void printConfigList() {
        ChatUtils.info(" - Configs");
        for (Config config : VoxMod.get().getConfigManager().getConfigs()) {
            ChatUtils.info(String.format(" \2479%s \2477- \2471%s",
                    config.getName(), config.getAuthor()));
        }
    }
}
