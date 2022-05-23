package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.cheats.setting.ModeSetting;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.RenderNameEvent;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.utils.render.ColorUtil;
import dev.rabies.vox.utils.render.DrawUtils;
import dev.rabies.vox.utils.ServerUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import java.awt.*;

public class EspCheat extends CheatWrapper {

    enum Mode {
        Normal, SuperCool
    }

    private final ModeSetting<Mode> modeSetting = registerModeSetting("Mode", Mode.SuperCool);

    private final BoolSetting ignoreSelfSetting = registerBoolSetting("Ignore self", false);
    private final BoolSetting invisSetting = registerBoolSetting("Invisible entity", true);

    private final SystemFontRenderer tagFont = VoxMod.get().newSystemFont("NotoSansJP-Bold", 13);
    private final SystemFontRenderer hpFont = VoxMod.get().newSystemFont("NotoSansJP-Regular", 11);
    private final Frustum frustum = new Frustum();

    public EspCheat() {
        super("ESP", Category.OTHER);
    }

    @SubscribeEvent
    public void onRenderName(RenderNameEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRender2d(Render2DEvent event) {
        setSuffix("2D");

        EntityRenderer entityRenderer = mc.entityRenderer;
        RenderManager renderManager = mc.getRenderManager();
        ScaledResolution resolution = event.getResolution();
        int factor = resolution.getScaleFactor();
        double scaling = factor / Math.pow(factor, 2.0D);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scaling, scaling, scaling);
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                if (!isValidEntity(entity)) continue;
                EntityLivingBase livingBase = (EntityLivingBase) entity;
                double x = DrawUtils.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                double y = DrawUtils.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                double z = DrawUtils.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                double width = entity.width / 1.8;
                double height = entity.height + (entity.isSneaking() ? -0.15 : 0.1);

                AxisAlignedBB aabb = new AxisAlignedBB(
                        x - width, y, z - width,
                        x + width, y + height, z + width);
                Vector3d[] vectors = {
                        new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ),
                        new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ),
                        new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)
                };

                entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                Vector4d position = null;

                for (Vector3d vector : vectors) {
                    vector = DrawUtils.project2D(factor, vector.x - renderManager.viewerPosX, vector.y - renderManager.viewerPosY, vector.z - renderManager.viewerPosZ);
                    if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position == null) continue;
                entityRenderer.setupOverlayRendering();
                double posX = position.x;
                double posY = position.y;
                double endPosX = position.z;
                double endPosY = position.w;

                switch (modeSetting.getValue()) {
                    case Normal:
                        renderNormalBox(livingBase, posX, posY, endPosX, endPosY);
                        break;

                    case SuperCool:
                        renderSuperCoolBox(livingBase, posX, posY, endPosX, endPosY);
                }

                int backgroundColor = new Color(0, 0, 0, 200).getRGB();
                double armorPercentage = livingBase.getTotalArmorValue() / 20.0D;
                double armorBarHeight = (endPosY - posY) * armorPercentage;
                DrawUtils.drawRect(endPosX + 1.0D, posY - 0.5D, endPosX + 3.0D, endPosY + 0.5D, backgroundColor);
                if (armorBarHeight > 0) {
                    DrawUtils.drawRect(endPosX + 1.5D, endPosY, endPosX + 2.5D, endPosY - armorBarHeight, new Color(60, 100, 255).getRGB());
                }

                float health = livingBase.getHealth();
                float maxHealth = livingBase.getMaxHealth();
                if (health > maxHealth) health = maxHealth;

                int healthColor = getHealthColor(health, maxHealth).getRGB();
                double hpPercentage = (health / maxHealth);
                double hpHeight = (endPosY - posY) * hpPercentage;
                DrawUtils.drawRect(posX - 3.5D, posY - 0.5D, posX - 1.5D, endPosY + 0.5D, backgroundColor);
                if (health > 0) {
                    DrawUtils.drawRect(posX - 3.0D, endPosY, posX - 2.0D, endPosY - hpHeight, healthColor);
                }

                String tagString = livingBase.getName();
                if (livingBase instanceof EntityPlayer) {
                    if (ServerUtil.isFriend((EntityPlayer) livingBase)) {
                        tagString += " \2477[\247aFriend\2477]";
                    }
                }

                String hpFormat = String.format("%.1f", health / 2 * 10);
                hpFont.drawCenteredStringWithShadow(
                        "\2477HP: ",
                        (posX + endPosX) / 2 - 6,
                        posY - (hpFont.getHeight() + 2),
                        -1);
                hpFont.drawCenteredStringWithShadow(
                        hpFormat.replace(".0", "") + "%",
                        (posX + endPosX) / 2 + 6,
                        posY - (hpFont.getHeight() + 2),
                        healthColor);
                tagFont.drawCenteredStringWithShadow(
                        tagString,
                        (posX + endPosX) / 2,
                        posY - ((hpFont.getHeight() + 3) + tagFont.getHeight()),
                        ServerUtil.getTeamColor(livingBase).getRGB());
            }
        }

        entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }

    private void renderSuperCoolBox(EntityLivingBase livingBase, double posX, double posY, double endPosX, double endPosY) {
        int backgroundColor = new Color(0, 0, 0, 200).getRGB();
        Color first = ColorUtil.getSuperCoolColor(1, 0, 270);
        Color second = ColorUtil.getSuperCoolColor(1, 90, 270);
        Color third = ColorUtil.getSuperCoolColor(1, 180, 270);
        Color fourth = ColorUtil.getSuperCoolColor(1, 270, 270);
        if (livingBase.hurtTime > 0) {
            first = new Color(255, 80, 80);
            second = new Color(255, 80, 80);
            third = new Color(255, 80, 80);
            fourth = new Color(255, 80, 80);
        }

        DrawUtils.drawRect(posX - 1.0D, posY, posX + 1.0D, endPosY + 0.5D, backgroundColor);
        DrawUtils.drawRect(posX - 1.0D, endPosY - 1.5D, endPosX + 0.5D, endPosY + 0.5D, backgroundColor);
        DrawUtils.drawRect(endPosX - 1.5D, posY, endPosX + 0.5D, endPosY + 0.5D, backgroundColor);
        DrawUtils.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 1.5D, backgroundColor);

        DrawUtils.drawGradientH(posX - 0.5D, posY, posX + 0.5D, endPosY, first.getRGB(), second.getRGB());
        DrawUtils.drawGradientV(posX, endPosY - 1.0D, endPosX, endPosY, second.getRGB(), third.getRGB());
        DrawUtils.drawGradientH(endPosX - 1.0D, posY, endPosX, endPosY, fourth.getRGB(), third.getRGB());
        DrawUtils.drawGradientV(posX - 0.5D, posY, endPosX, posY + 1.0D, first.getRGB(), fourth.getRGB());
    }

    private void renderNormalBox(EntityLivingBase livingBase, double posX, double posY, double endPosX, double endPosY) {
        int backgroundColor = new Color(0, 0, 0, 200).getRGB();
        int mainColor = new Color(40, 255, 60).getRGB();
        if (livingBase.hurtTime > 0) {
            mainColor = new Color(255, 80, 80).getRGB();
        }

        DrawUtils.drawRect(posX + 0.5D, posY, posX - 1.0D, posY + (endPosY - posY) / 7.0D + 0.5D, backgroundColor);
        DrawUtils.drawRect(posX - 1.0D, endPosY, posX + 0.5D, endPosY - (endPosY - posY) / 7.0D - 0.5D, backgroundColor);
        DrawUtils.drawRect(posX - 1.0D, posY - 0.5D, posX + (endPosX - posX) / 4.0D + 0.5D, posY + 1.0D, backgroundColor);
        DrawUtils.drawRect(endPosX - (endPosX - posX) / 4.0D - 0.5D, posY - 0.5D, endPosX, posY + 1.0D, backgroundColor);
        DrawUtils.drawRect(endPosX - 1.0D, posY, endPosX + 0.5D, posY + (endPosY - posY) / 7.0D + 0.5D, backgroundColor);
        DrawUtils.drawRect(endPosX - 1.0D, endPosY, endPosX + 0.5D, endPosY - (endPosY - posY) / 7.0D - 0.5D, backgroundColor);
        DrawUtils.drawRect(posX - 1.0D, endPosY - 1.0D, posX + (endPosX - posX) / 4.0D + 0.5D, endPosY + 0.5D, backgroundColor);
        DrawUtils.drawRect(endPosX - (endPosX - posX) / 4.0D - 0.5D, endPosY - 1.0D, endPosX + 0.5D, endPosY + 0.5D, backgroundColor);

        DrawUtils.drawRect(posX, posY, posX - 0.5D, posY + (endPosY - posY) / 7.0D, mainColor);
        DrawUtils.drawRect(posX, endPosY, posX - 0.5D, endPosY - (endPosY - posY) / 7.0D, mainColor);
        DrawUtils.drawRect(posX - 0.5D, posY, posX + (endPosX - posX) / 4.0D, posY + 0.5D, mainColor);
        DrawUtils.drawRect(endPosX - (endPosX - posX) / 4.0D, posY, endPosX, posY + 0.5D, mainColor);
        DrawUtils.drawRect(endPosX - 0.5D, posY, endPosX, posY + (endPosY - posY) / 7.0D, mainColor);
        DrawUtils.drawRect(endPosX - 0.5D, endPosY, endPosX, endPosY - (endPosY - posY) / 7.0D, mainColor);
        DrawUtils.drawRect(posX, endPosY - 0.5D, posX + (endPosX - posX) / 4.0D, endPosY, mainColor);
        DrawUtils.drawRect(endPosX - (endPosX - posX) / 4.0D, endPosY - 0.5D, endPosX - 0.5D, endPosY, mainColor);
    }

    public static Color getHealthColor(float health, float maxHealth) {
        float[] fractions = {0.0F, 0.5F, 1.0F};
        Color[] colors = {
                new Color(255, 50, 50),
                new Color(255, 180, 50),
                new Color(50, 255, 50)
        };
        float progress = health / maxHealth;
        return DrawUtils.blendColors(fractions, colors, progress).brighter();
    }

    private boolean isValidEntity(Entity entity) {
        if (entity.isDead) return false;
        if (!entity.isEntityAlive()) return false;
        if (!invisSetting.getValue() && entity.isInvisible()) return false;
        if (ignoreSelfSetting.getValue() && entity == mc.player) return false;
        if (entity == mc.player && mc.gameSettings.thirdPersonView == 0) return false;
        if (!(entity instanceof EntityPlayer)) return false;
        return isInViewFrustum(entity.getEntityBoundingBox());
    }

    private boolean isInViewFrustum(AxisAlignedBB bb) {
        frustum.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
        return frustum.isBoundingBoxInFrustum(bb);
    }
}
