package com.example.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public class FileUtils {

    private FileUtils() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Finds the latest video file in the specified directory.
     *
     * @param directory The directory to search in.
     * @return An optional File object pointing to the latest video recording.
     */
    public static Optional<File> findLatestVideoFile(Path directory) {
        File dir = directory.toFile();
        if (!dir.exists() || !dir.isDirectory()) {
            return Optional.empty();
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".mp4"));
        if (files == null || files.length == 0) {
            return Optional.empty();
        }

        File latestFile = null;
        for (File file : files) {
            if (latestFile == null || file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }

        return Optional.ofNullable(latestFile);
    }
}
