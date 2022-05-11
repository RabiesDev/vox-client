package dev.rabies.vox.utils;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
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
	private boolean binded;

	public ShaderUtil(String frag) {
		GL20.glAttachShader(programId, compile(
				this.getClass().getResourceAsStream("/assets/minecraft/vox/vertex.vsh"),
				GL20.GL_VERTEX_SHADER));
		GL20.glAttachShader(programId, compile(
				this.getClass().getResourceAsStream(String.format("/assets/minecraft/vox/%s", frag)),
				GL20.GL_FRAGMENT_SHADER));
		GL20.glLinkProgram(programId);
		if (GL20.glGetShaderi(programId, GL20.GL_LINK_STATUS) == 0)
			throw new RuntimeException("shader error 1");
	}

	public void renderShader(ScaledResolution resolution) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0.0, 1.0);
		GL11.glVertex2d(0.0, 0.0);
		GL11.glTexCoord2d(0.0, 0.0);
		GL11.glVertex2d(0.0, resolution.getScaledHeight());
		GL11.glTexCoord2d(1.0, 0.0);
		GL11.glVertex2d(resolution.getScaledWidth(), resolution.getScaledHeight());
		GL11.glTexCoord2d(1.0, 1.0);
		GL11.glVertex2d(resolution.getScaledWidth(), 0.0);
		GL11.glEnd();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.disableBlend();
		GL20.glUseProgram(0);
		binded = false;
	}

	public int getUniformByName(String name) {
		return GL20.glGetUniformLocation(programId, name);
	}

	private int compile(InputStream inputStream, int type) {
		int shader = GL20.glCreateShader(type);
		try {
			GL20.glShaderSource(shader, readInputStream(inputStream));
			GL20.glValidateProgram(programId);
			GL20.glCompileShader(shader);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (GL20.glGetShaderi(shader, GL20.GL_LINK_STATUS) == 0)
			throw new RuntimeException("shader error 2");
		return shader;
	}
	
	public String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null)
            sb.append(line).append('\n');
        return sb.toString();
    }
}
