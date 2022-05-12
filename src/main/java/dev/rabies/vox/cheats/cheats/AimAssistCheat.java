package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.ServerUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimAssistCheat extends CheatWrapper {

    private final NumberSetting rangeSetting = registerNumberSetting("Range", 3.8, 3.0, 10.0, 0.1);
    private final NumberSetting speedSetting = registerNumberSetting("Speed", 0.4f, 0.1f, 10.0f, 0.1f);
    private final NumberSetting tickExistSetting = registerNumberSetting("Tick Exist", 15, 0, 50, 1);
    private final BoolSetting clickOnlySetting = registerBoolSetting("On click only", false);
    private final BoolSetting teamCheckSetting = registerBoolSetting("Team check", true);
    private final BoolSetting itemInUseSetting = registerBoolSetting("Item in use", false);

    private final List<EntityLivingBase> targetEntities = new ArrayList<>();
    private EntityLivingBase targetEntity;

    public AimAssistCheat() {
        super("Aim Assist", Category.LEGIT);
    }

    @Override
    public void onDisable() {
        targetEntities.clear();
        targetEntity = null;
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (event.isPost()) return;
        targetEntity = findTarget();
        if (targetEntity == null) return;
        if (clickOnlySetting.getValue() && !mc.gameSettings.keyBindAttack.isKeyDown()) return;
        if (mc.player.getItemInUseCount() > 0 && !itemInUseSetting.getValue()) return;
        float yawChange = getYawChangeToEntity(targetEntity);
        float speed = speedSetting.getValue().floatValue();
        float improvedSpeed = (float) MathHelper.clamp(RandomUtils.nextFloat(speed - 0.2f, speed + 1.4f),
                speedSetting.getMinValue(), speedSetting.getMaxValue());
        improvedSpeed -= improvedSpeed % getGcd();
        if (yawChange < -3.5) {
        	improvedSpeed -= yawChange / 12f;
            mc.player.rotationYaw -= improvedSpeed;
        } else if (yawChange > 3.5) {
        	improvedSpeed += yawChange / 12f;
            mc.player.rotationYaw += improvedSpeed;
        }
    }

    public float getGcd() {
        float sensitivity = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return sensitivity * sensitivity * sensitivity * RandomUtils.nextFloat(2.0f, 5.0f);
    }

    private EntityLivingBase findTarget() {
    	targetEntities.clear();
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!(entity instanceof EntityLivingBase)) continue;
            if (entity.isDead || !entity.isEntityAlive()) continue;
            if (entity.ticksExisted < tickExistSetting.getValue()) continue;
            double focusRange = mc.player.canEntityBeSeen(entity) ? rangeSetting.getValue() : 3.5f;
            if (mc.player.getDistance(entity) > focusRange) continue;
            if (entity instanceof EntityPlayer) {
                if (entity == mc.player) continue;
                if (teamCheckSetting.getValue() && ServerUtil.isTeams((EntityPlayer) entity)) continue;
                targetEntities.add((EntityLivingBase) entity);
            }
        }

        if (targetEntities.isEmpty()) return null;
        targetEntities.sort(Comparator.comparingDouble(this::getYawChangeToEntity));
        return targetEntities.get(0);
    }

    public float getYawChangeToEntity(Entity entity) {
        double deltaX = entity.posX - mc.player.posX;
        double deltaZ = entity.posZ - mc.player.posZ;
        double yawToEntity;
        if (deltaZ < 0.0d && deltaX < 0.0d) {
            yawToEntity = 90.0d + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            if (deltaZ < 0.0d && deltaX > 0.0d) {
                yawToEntity = -90.0d + Math.toDegrees(Math.atan(deltaZ / deltaX));
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
        return MathHelper.wrapDegrees(-(mc.player.rotationYaw - (float) yawToEntity));
    }
}
