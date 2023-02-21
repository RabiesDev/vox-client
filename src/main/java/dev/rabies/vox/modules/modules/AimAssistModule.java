package dev.rabies.vox.modules.modules;

import dev.rabies.vox.modules.Category;
import dev.rabies.vox.modules.Module;
import dev.rabies.vox.settings.BooleanSetting;
import dev.rabies.vox.settings.NumberSetting;
import dev.rabies.vox.events.game.UpdateEvent;
import dev.rabies.vox.utils.ServerHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimAssistModule extends Module {
    private final NumberSetting rangeSetting = registerSetting(NumberSetting.builder()
            .name("Range")
            .value(3.8)
            .min(3.0)
            .max(8.0)
            .interval(0.1)
            .build()
    );

    private final NumberSetting aimSpeedSetting = registerSetting(NumberSetting.builder()
            .name("Aim Speed")
            .value(0.45)
            .min(0.1)
            .max(10.0)
            .interval(0.1)
            .build()
    );

    private final BooleanSetting holdOnlySetting = registerSetting(BooleanSetting.builder()
            .name("Hold Only")
            .value(false)
            .build()
    );

    private final BooleanSetting ignoreFriendsSetting = registerSetting(BooleanSetting.builder()
            .name("Ignore Friends")
            .value(true)
            .build()
    );

    private final BooleanSetting ignoreTeamsSetting = registerSetting(BooleanSetting.builder()
            .name("Ignore Teammate")
            .value(true)
            .build()
    );

    private final BooleanSetting itemInUseSetting = registerSetting(BooleanSetting.builder()
            .name("Item in use")
            .value(false)
            .build()
    );

    private final BooleanSetting targetPlayersSetting = registerSetting(BooleanSetting.builder()
            .name("Target Players")
            .value(true)
            .build()
    );

    private final BooleanSetting targetMonstersSetting = registerSetting(BooleanSetting.builder()
            .name("Target Monsters")
            .value(true)
            .build()
    );

    private final BooleanSetting targetAnimalsSetting = registerSetting(BooleanSetting.builder()
            .name("Target Animals")
            .value(false)
            .build()
    );

    private final List<EntityLivingBase> validated = new ArrayList<>();
    private EntityLivingBase primary;
    private int breakTick;

    public AimAssistModule() {
        super("Aim Assist", Category.LEGIT);
    }

    @Override
    public void onDisable() {
        validated.clear();
        primary = null;
        breakTick = 0;
    }

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        primary = findTarget();
        if (event.isPost() || primary == null || !canAssist()) {
            return;
        }

        float diff = calculateYawChangeToDst(primary);
        float aimSpeed = aimSpeedSetting.getValue().floatValue();
        aimSpeed = (float) MathHelper.clamp(RandomUtils.nextFloat(aimSpeed - 0.2f, aimSpeed + 1.8f), aimSpeedSetting.getMin(), aimSpeedSetting.getMax());
        aimSpeed -= aimSpeed % getSensitivity();

        if (diff < -6) {
            aimSpeed -= diff / 12f;
            mc.player.rotationYaw -= aimSpeed;
        } else if (diff > 6) {
            aimSpeed += diff / 12f;
            mc.player.rotationYaw += aimSpeed;
        }
    }

    public double getSensitivity() {
        double sensitivity = mc.gameSettings.mouseSensitivity * 0.6 + 0.2;
        return sensitivity * sensitivity * sensitivity * RandomUtils.nextFloat(3.0f, 4.0f);
    }

    private boolean canAssist() {
        if (mc.isGamePaused() || !mc.inGameHasFocus || mc.currentScreen != null) {
            return false;
        }

        if (holdOnlySetting.getValue() && !mc.gameSettings.keyBindAttack.isKeyDown()) {
            return false;
        }

        if (mc.player.getItemInUseCount() > 0 && !itemInUseSetting.getValue()) {
            return false;
        }

        if (mc.objectMouseOver != null) {
            RayTraceResult result = mc.objectMouseOver;
            if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockPos = result.getBlockPos();
                Block block = mc.world.getBlockState(blockPos).getBlock();
                if (block instanceof BlockAir || block instanceof BlockLiquid) {
                    return true;
                }

                if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                    if (breakTick > 2) {
                        return false;
                    }
                    breakTick++;
                } else {
                    breakTick = 0;
                }
            }
        }
        return true;
    }

    private EntityLivingBase findTarget() {
        validated.clear();

        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase && entity != mc.player) {
                if (entity.isDead || !entity.isEntityAlive() || entity.ticksExisted < 10) {
                    continue;
                }

                double focusRange = mc.player.canEntityBeSeen(entity) ? rangeSetting.getValue() : 3.5;
                if (mc.player.getDistance(entity) > focusRange) continue;
                if (entity instanceof EntityPlayer && targetPlayersSetting.getValue()) {
                    if (ignoreFriendsSetting.getValue() && ServerHelper.isFriend((EntityPlayer) entity)) {
                        continue;
                    }

                    if (ignoreTeamsSetting.getValue() && ServerHelper.isTeammate((EntityPlayer) entity)) {
                        continue;
                    }

                    validated.add((EntityLivingBase) entity);
                } else if (entity instanceof EntityAnimal && targetAnimalsSetting.getValue()) {
                    validated.add((EntityLivingBase) entity);
                } else if (entity instanceof EntityMob && targetMonstersSetting.getValue()) {
                    validated.add((EntityLivingBase) entity);
                }
            }
        }

        if (validated.isEmpty()) return null;
        validated.sort(Comparator.comparingDouble(this::calculateYawChangeToDst));
        return validated.get(0);
    }

    public float calculateYawChangeToDst(Entity entity) {
        double diffX = entity.posX - mc.player.posX;
        double diffZ = entity.posZ - mc.player.posZ;
        double deg = Math.toDegrees(Math.atan(diffZ / diffX));
        if (diffZ < 0.0 && diffX < 0.0) {
            return (float) MathHelper.wrapDegrees(-(mc.player.rotationYaw - (90 + deg)));
        } else if (diffZ < 0.0 && diffX > 0.0) {
            return (float) MathHelper.wrapDegrees(-(mc.player.rotationYaw - (-90 + deg)));
        } else {
            return (float) MathHelper.wrapDegrees(-(mc.player.rotationYaw - Math.toDegrees(-Math.atan(diffX / diffZ))));
        }
    }
}
