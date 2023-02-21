package dev.rabies.vox.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;

public class DrawHelper {

    private static final ShaderUtil roundShader = new ShaderUtil("rounded_rect.frag");

    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int[] range = new int[2];
        int startPoint = 0;
        while (startPoint < fractions.length && fractions[startPoint] <= progress) {
            startPoint++;
        }

        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }

        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }

    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = (float) 1.0 - r;

        float rgb1[] = new float[3];
        float rgb2[] = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);

        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;

        if (red < 0) {
            red = 0;
        } else if (red > 255) {
            red = 255;
        }
        if (green < 0) {
            green = 0;
        } else if (green > 255) {
            green = 255;
        }
        if (blue < 0) {
            blue = 0;
        } else if (blue > 255) {
            blue = 255;
        }

        Color color = null;
        try {
            color = new Color(red, green, blue);
        } catch (IllegalArgumentException exp) {
            NumberFormat nf = NumberFormat.getNumberInstance();
            System.out.println(nf.format(red) + "; " + nf.format(green) + "; " + nf.format(blue));
            exp.printStackTrace();
        }
        return color;
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        Color color = null;
        if (fractions.length == colors.length) {
            int[] indicies = getFractionIndicies(fractions, progress);
            if (indicies[0] < 0 || indicies[0] >= fractions.length || indicies[1] < 0 || indicies[1] >= fractions.length) {
                return colors[0];
            }

            float[] range = new float[]{fractions[indicies[0]], fractions[indicies[1]]};
            Color[] colorRange = new Color[]{colors[indicies[0]], colors[indicies[1]]};
            float max = range[1] - range[0];
            float value = progress - range[0];
            float weight = value / max;
            color = blend(colorRange[0], colorRange[1], 1f - weight);
        }
        return color;
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, vector)) {
            return new Vector3d((vector.get(0) / scaleFactor), ((Display.getHeight() - vector.get(1)) / scaleFactor), vector.get(2));
        }
        return null;
    }
    
    public static void drawGradientH(double x, double y, double x2, double y2, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.resetColor();
    }
    
    public static void drawGradientV(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.resetColor();
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        drawRect(GL11.GL_QUADS, left, top, right, bottom, color);
    }

    public static void drawRect(int flag, double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
        		GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(red, green, blue, alpha);
        bufferBuilder.begin(flag, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(left, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, bottom, 0.0D).endVertex();
        bufferBuilder.pos(right, top, 0.0D).endVertex();
        bufferBuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();
    }

    public static void drawRoundRect(float x, float y, float width, float height, float radius, boolean blur, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL20.glUseProgram(roundShader.getProgramId());
        GL20.glUniform2f(roundShader.getUniformByName("location"), x * resolution.getScaleFactor(),
                mc.displayHeight - (height * resolution.getScaleFactor()) - (y * resolution.getScaleFactor()));
        GL20.glUniform2f(roundShader.getUniformByName("rectSize"),  width * resolution.getScaleFactor(),
                height * resolution.getScaleFactor());
        GL20.glUniform1f(roundShader.getUniformByName("radius"), radius * resolution.getScaleFactor());
        GL20.glUniform1i(roundShader.getUniformByName("blur"), blur ? 1 : 0);
        GL20.glUniform4f(roundShader.getUniformByName("color"), red, green, blue, alpha);
        roundShader.renderShader(x - 1, y - 1, width + 2, height + 2);
    }

    public static void drawBB(AxisAlignedBB aabb, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        bufferBuilder.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawOutlinedBB(AxisAlignedBB aabb, int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GlStateManager.color(red, green, blue, alpha);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex();
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex();
        tessellator.draw();
        GlStateManager.resetColor();
    }
}
