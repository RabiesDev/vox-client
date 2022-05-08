package dev.rabies.vox.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.lwjgl.opengl.GL20;

import lombok.Getter;

public class ShaderUtil {
	
	@Getter
	private final int programId;
	
	public ShaderUtil(String frag) {
		int programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, compile(this.getClass().getResourceAsStream(String.format("/assets/minecraft/vox/%s", frag))));
		GL20.glAttachShader(programId, compile(this.getClass().getResourceAsStream("/assets/minecraft/vox/vertex.vsh")));
		GL20.glLinkProgram(programId);
		if (GL20.glGetShaderi(programId, GL20.GL_LINK_STATUS) == 0) {
			throw new RuntimeException("shader error 1");
		}
		this.programId = programId;
	}
	
	private int compile(InputStream inputStream) {
		int shader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
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
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null)
            sb.append(line).append('\n');
        return sb.toString();
    }
}
