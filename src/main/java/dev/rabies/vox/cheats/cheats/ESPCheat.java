package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.VoxMod;
import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.Cheat;
import dev.rabies.vox.cheats.setting.BoolSetting;
import dev.rabies.vox.events.render.Render2DEvent;
import dev.rabies.vox.events.render.RenderNameEvent;
import dev.rabies.vox.render.font.SystemFontRenderer;
import dev.rabies.vox.utils.DrawUtils;
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

public class ESPCheat extends Cheat {

    private final BoolSetting ignoreSelfSetting = registerBoolSetting("Ignore self", false);
    private final BoolSetting invisSetting = registerBoolSetting("Invisible entity", true);

    private final SystemFontRenderer tagFont = VoxMod.get().newSystemFont("Mukta-Bold", 13);
    private final SystemFontRenderer hpFont = VoxMod.get().newSystemFont("Mukta-SemiBold", 11);
    private final Frustum frustum = new Frustum();

    public ESPCheat() {
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

                int black = new Color(0, 0, 0, 200).getRGB();
                Color first = getEspColor(0);
                Color second = getEspColor(90);
                Color third = getEspColor(180);
                Color fourth = getEspColor(270);
                if (livingBase.hurtTime > 0) {
                	first = new Color(255, 80, 80);
                	second = new Color(255, 80, 80);
                	third = new Color(255, 80, 80);
                	fourth = new Color(255, 80, 80);
                }

                DrawUtils.drawRect(posX - 1.0D, posY, posX + 1.0D, endPosY + 0.5D, black);
                DrawUtils.drawRect(posX - 1.0D, endPosY - 1.5D, endPosX + 0.5D, endPosY + 0.5D, black);
                DrawUtils.drawRect(endPosX - 1.5D, posY, endPosX + 0.5D, endPosY + 0.5D, black);
                DrawUtils.drawRect(posX - 1.0D, posY - 0.5D, endPosX + 0.5D, posY + 1.5D, black);

                DrawUtils.drawGradientH(posX - 0.5D, posY, posX + 0.5D, endPosY, first.getRGB(), second.getRGB());
                DrawUtils.drawGradientV(posX, endPosY - 1.0D, endPosX, endPosY, second.getRGB(), third.getRGB());
                DrawUtils.drawGradientH(endPosX - 1.0D, posY, endPosX, endPosY, fourth.getRGB(), third.getRGB());
                DrawUtils.drawGradientV(posX - 0.5D, posY, endPosX, posY + 1.0D, first.getRGB(), fourth.getRGB());

                double armorPercentage = livingBase.getTotalArmorValue() / 20.0D;
                double armorBarHeight = (endPosY - posY) * armorPercentage;
                DrawUtils.drawRect(endPosX + 1.0D, posY - 0.5D, endPosX + 3.0D, endPosY + 0.5D, black);
                if (armorBarHeight > 0) {
                    DrawUtils.drawRect(endPosX + 1.5D, endPosY, endPosX + 2.5D, endPosY - armorBarHeight, new Color(60, 100, 255).getRGB());
                }

                float health = livingBase.getHealth();
                float maxHealth = livingBase.getMaxHealth();
                if (health > maxHealth) health = maxHealth;

                int healthColor = getHealthColor(health, maxHealth).getRGB();
                double hpPercentage = (health / maxHealth);
                double hpHeight = (endPosY - posY) * hpPercentage;
                DrawUtils.drawRect(posX - 3.5D, posY - 0.5D, posX - 1.5D, endPosY + 0.5D, black);
                if (health > 0) {
                    DrawUtils.drawRect(posX - 3.0D, endPosY, posX - 2.0D, endPosY - hpHeight, healthColor);
                }

                String hpFormat = String.format("%.1f", health / 2 * 10);
                hpFont.drawCenteredStringWithShadow(
                        hpFormat.replace(".0", "") + "%",
                        (posX + endPosX) / 2,
                        posY - (hpFont.getHeight() + 2),
                        healthColor);
                tagFont.drawCenteredStringWithShadow(
                        livingBase.getName(),
                        (posX + endPosX) / 2,
                        posY - ((hpFont.getHeight() + 3) + tagFont.getHeight()),
                        ServerUtil.getTeamColor(livingBase).getRGB());
            }
        }

        entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
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
    
    public Color getEspColor(double offset) {
        float[] fractions = {0.0F, 0.3F, 0.7F, 1.0F};
        Color[] colors = {
                new Color(55, 33, 255),
                new Color(255, 70, 90),
                new Color(50, 150, 255),
                new Color(55, 33, 255)
        };
        double progress = Math.ceil((System.currentTimeMillis() + (offset * 2) * 2) / 5);
        progress %= 270.0D;
        return DrawUtils.blendColors(fractions, colors, (float) (progress / 270)).brighter();
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
