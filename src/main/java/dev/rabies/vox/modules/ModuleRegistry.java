package dev.rabies.vox.modules;

import dev.rabies.vox.modules.modules.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleRegistry {
    private static final Map<Class<? extends Module>, Module> instanceMap = new HashMap<>();

    public static void initialize() {
        instanceMap.put(AimAssistModule.class, new AimAssistModule());
        instanceMap.put(AntiForgeBypassModule.class, new AntiForgeBypassModule());
        instanceMap.put(AutoClickerModule.class, new AutoClickerModule());
        instanceMap.put(AutoRespawnModule.class, new AutoRespawnModule());
        instanceMap.put(AutoSprintModule.class, new AutoSprintModule());
        instanceMap.put(AutoToolModule.class, new AutoToolModule());
        instanceMap.put(BrightnessModule.class, new BrightnessModule());
        instanceMap.put(ChamsModule.class, new ChamsModule());
        instanceMap.put(EspModule.class, new EspModule());
        instanceMap.put(FakeFpsModule.class, new FakeFpsModule());
        instanceMap.put(FlightModule.class, new FlightModule());
        instanceMap.put(FreecamModule.class, new FreecamModule());
        instanceMap.put(HitBoxModule.class, new HitBoxModule());
        instanceMap.put(HudModule.class, new HudModule());
        instanceMap.put(InventoryPlusModule.class, new InventoryPlusModule());
        instanceMap.put(KeepSprintModule.class, new KeepSprintModule());
        instanceMap.put(OutlineEspModule.class, new OutlineEspModule());
        instanceMap.put(PackSpooferModule.class, new PackSpooferModule());
        instanceMap.put(PingSpooferModule.class, new PingSpooferModule());
        instanceMap.put(ReachModule.class, new ReachModule());
        instanceMap.put(SneakModule.class, new SneakModule());
        instanceMap.put(StreamerModule.class, new StreamerModule());
        instanceMap.put(TabGuiModule.class, new TabGuiModule());
        instanceMap.put(VelocityModule.class, new VelocityModule());
        instanceMap.put(WTapModule.class, new WTapModule());
    }

    public static Collection<Module> getModules() {
        return instanceMap.values();
    }

    public static Collection<Module> getModules(Category category) {
        return getModules().stream().filter(module -> module.getCategory().equals(category)).collect(Collectors.toList());
    }

    public static Module fromString(String name) {
        return getModules().stream().filter(module ->
                module.name.replaceAll(" ", "").equalsIgnoreCase(name)
        ).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Module> T fromInstance(Class<? extends Module> clazz) {
        return (T) instanceMap.get(clazz);
    }
}
