package filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemUtils {
    public static File getLastCreatedFile(File directory) throws IOException {

        if(directory.isFile()) {
            return directory;
        }

        File selectedFile = null;
        long selectedCreationTime = Long.MIN_VALUE;

        for (var curFile: directory.listFiles()) {
            curFile = getLastCreatedFile(curFile);
            var creationTime = getCreationTimeFromFile(curFile);

            if(creationTime > selectedCreationTime) {
                selectedCreationTime = creationTime;
                selectedFile = curFile;
            }
        }

        if(selectedFile == null)
            throw new IOException("no file selected");

        return selectedFile;
    }
    public static long getCreationTimeFromFile(File file) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        return attrs.creationTime().toMillis();
    }
}
