package dev.rabies.vox.cheats.cheats;

import dev.rabies.vox.cheats.Category;
import dev.rabies.vox.cheats.CheatWrapper;
import dev.rabies.vox.events.game.PacketEvent;
import dev.rabies.vox.events.render.Render3DEvent;
import dev.rabies.vox.utils.DrawUtils;
import lombok.Data;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class DeathChamsCheat extends CheatWrapper {

    private final List<DeadPlayerData> playerDataList = new ArrayList<>();

    public DeathChamsCheat() {
        super("Death Chams", Category.OTHER);
    }

    @SubscribeEvent
    public void onRender3d(Render3DEvent event) {
        if (playerDataList.isEmpty()) return;
        for (int i = 0; i < playerDataList.size(); i++) {
        	playerDataList.get(i).render(i * 5);
        }
        playerDataList.removeIf(it -> System.currentTimeMillis() - it.getTime() > 2000);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent event) {
    	if (mc.world == null) return;
        if (event.isOut()) return;
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus entityStatus = (SPacketEntityStatus) event.getPacket();
            Entity entity = entityStatus.getEntity(mc.world);
            if (entityStatus.getOpCode() != 3) return;
            if (!(entity instanceof EntityPlayer)) return;
            playerDataList.add(new DeadPlayerData(
                    (EntityPlayer) entity, System.currentTimeMillis(),
                    entity.rotationYaw, entity.rotationPitch,
                    entity.posX, entity.posY, entity.posZ
            ));
        }
    }

    @Data
    static class DeadPlayerData {

        private final EntityPlayer player;
        private final long time;

        private final float yaw, pitch;
        private final double x, y, z;

        public void render(int num) {
            double viewX = x - mc.getRenderManager().viewerPosX;
            double viewY = y - mc.getRenderManager().viewerPosY;
            double viewZ = z - mc.getRenderManager().viewerPosZ;

            int boxAlpha = (int) MathHelper.clamp(255 - ((System.currentTimeMillis() - time) * 60.0 / 255), 0, 150);
            int boxHeadColor = reAlpha(getChamsColor(45 + num).getRGB(), boxAlpha);
            int boxChestColor = reAlpha(getChamsColor(30 + num).getRGB(), boxAlpha);
            int boxArmColor = reAlpha(getChamsColor(15 + num).getRGB(), boxAlpha);
            int boxLegColor = reAlpha(getChamsColor(num).getRGB(), boxAlpha);

            int outlineAlpha = (int) MathHelper.clamp(255 - ((System.currentTimeMillis() - time) * 60.0 / 255), 0, 255);
            int outlineHeadColor = reAlpha(getChamsColor(45 + num).getRGB(), outlineAlpha);
            int outlineChestColor = reAlpha(getChamsColor(30 + num).getRGB(), outlineAlpha);
            int outlineArmColor = reAlpha(getChamsColor(15 + num).getRGB(), outlineAlpha);
            int outlineLegColor = reAlpha(getChamsColor(num).getRGB(), outlineAlpha);

            AxisAlignedBB head = new AxisAlignedBB(viewX - 0.2D, viewY + 1.42D, viewZ - 0.25D, viewX + 0.2D, viewY + 1.92D, viewZ + 0.25D);
            AxisAlignedBB chest = new AxisAlignedBB(viewX - 0.15D, viewY + 0.72D, viewZ - 0.25D, viewX + 0.15D, viewY + 1.42D, viewZ + 0.25D);
            AxisAlignedBB arm1 = new AxisAlignedBB(viewX - 0.15D, viewY + 0.72D, viewZ + 0.25D, viewX + 0.15D, viewY + 1.42D, viewZ + 0.5D);
            AxisAlignedBB arm2 = new AxisAlignedBB(viewX - 0.15D, viewY + 0.72D, viewZ - 0.25D, viewX + 0.15D, viewY + 1.42D, viewZ - 0.5D);
            AxisAlignedBB leg1 = new AxisAlignedBB(viewX - 0.15D, viewY + 0.0D, viewZ + 0.0D, viewX + 0.15D, viewY + 0.72D, viewZ + 0.25D);
            AxisAlignedBB leg2 = new AxisAlignedBB(viewX - 0.15D, viewY + 0.0D, viewZ + 0.0D, viewX + 0.15D, viewY + 0.72D, viewZ - 0.25D);

            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GlStateManager.glLineWidth(2f);
            GlStateManager.translate(viewX, viewY, viewZ);
            GlStateManager.rotate(180.0F - (yaw + 90.0F), 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-viewX, -viewY, -viewZ);
            DrawUtils.drawBB(head, boxHeadColor);
            DrawUtils.drawBB(chest, boxChestColor);
            DrawUtils.drawBB(arm1, boxArmColor);
            DrawUtils.drawBB(arm2, boxArmColor);
            DrawUtils.drawBB(leg1, boxLegColor);
            DrawUtils.drawBB(leg2, boxLegColor);
            DrawUtils.drawOutlinedBB(head, outlineHeadColor);
            DrawUtils.drawOutlinedBB(chest, outlineChestColor);
            DrawUtils.drawOutlinedBB(arm1, outlineArmColor);
            DrawUtils.drawOutlinedBB(arm2, outlineArmColor);
            DrawUtils.drawOutlinedBB(leg1, outlineLegColor);
            DrawUtils.drawOutlinedBB(leg2, outlineLegColor);
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.popMatrix();
        }
        
        public static int reAlpha(int color, int alpha) {
            Color c = new Color(color);
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha).getRGB();
        }
        
        private Color getChamsColor(double offset) {
            float[] fractions = {0.0F, 0.3F, 0.7F, 1.0F};
            Color[] colors = {
                    new Color(55, 33, 255),
                    new Color(255, 70, 90),
                    new Color(50, 150, 255),
                    new Color(55, 33, 255)
            };
            double progress = Math.ceil((time + System.currentTimeMillis() + (offset * 2) * 2) / 3);
            progress %= 270.0D;
            return DrawUtils.blendColors(fractions, colors, (float) (progress / 270)).brighter();
        }
    }
}
