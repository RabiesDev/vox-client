package dev.rabies.vox.utils.render;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShaderUtil {
	@Getter
	private final int programId = GL20.glCreateProgram();
	@Getter @Setter
	private boolean bounded;

	public ShaderUtil(String frag) {
		GL20.glValidateProgram(programId);
		GL20.glAttachShader(programId, compile(this.getClass().getResourceAsStream("/assets/minecraft/vox/shader/default.vert"), GL20.GL_VERTEX_SHADER));
		GL20.glAttachShader(programId, compile(this.getClass().getResourceAsStream(String.format("/assets/minecraft/vox/shader/%s", frag)), GL20.GL_FRAGMENT_SHADER));
		GL20.glLinkProgram(programId);

		if (GL20.glGetShaderi(programId, GL20.GL_LINK_STATUS) == 0) {
			throw new RuntimeException("shader error 1");
		}
	}

	public void renderShader(ScaledResolution resolution) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex2f(0.0f, 0.0f);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2f(0.0f, resolution.getScaledHeight());
		GL11.glTexCoord2f(1.0f, 0.0f);
		GL11.glVertex2f(resolution.getScaledWidth(), resolution.getScaledHeight());
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex2f(resolution.getScaledWidth(), 0.0f);
		GL11.glEnd();
		GL20.glUseProgram(0);
		GlStateManager.disableBlend();
		GlStateManager.resetColor();
		bounded = false;
	}

	public void renderShader(float x, float y, float width, float height) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x, y + height);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f(x + width, y + height);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(x + width, y);
		GL11.glEnd();
		GL20.glUseProgram(0);
		GlStateManager.disableBlend();
		GlStateManager.resetColor();
		bounded = false;
	}

	public int getUniformByName(String name) {
		return GL20.glGetUniformLocation(programId, name);
	}

	private int compile(InputStream inputStream, int type) {
		int shader = GL20.glCreateShader(type);
		try {
			GL20.glShaderSource(shader, readInputStream(inputStream));
			GL20.glCompileShader(shader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (GL20.glGetShaderi(shader, GL20.GL_LINK_STATUS) == 0) {
			throw new RuntimeException("shader error 2");
		}
		return shader;
	}

	public String readInputStream(InputStream inputStream) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line).append('\n');
		}
		return builder.toString();
	}

	public static Framebuffer createFramebuffer(Framebuffer framebuffer) {
		if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getMinecraft().displayWidth || framebuffer.framebufferHeight != Minecraft.getMinecraft().displayHeight) {
			if (framebuffer != null) {
				framebuffer.deleteFramebuffer();
			}
			return new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
		}
		return framebuffer;
	}
}
