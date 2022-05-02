package dev.rabies.vox.utils.misc;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

public class ModFile {

    @Getter @Setter
    private File file;
    @Getter
    private final boolean directory;

    public ModFile(File file, boolean dir) {
        this.file = file;
        this.directory = dir;
        check();
    }

    public void check() {
        if (exists()) return;
        try {
            if (directory) file.mkdir();
            else file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        return file.exists();
    }
}
