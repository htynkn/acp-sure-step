package com.huangyunkun.acpsure.web.model;

/**
 * Represents a single entry (file or directory) returned by the file-browser API.
 */
public class FileEntry {

    private final String name;
    private final String path;
    private final boolean directory;

    public FileEntry(String name, String path, boolean directory) {
        this.name = name;
        this.path = path;
        this.directory = directory;
    }

    public String getName() { return name; }
    public String getPath() { return path; }
    public boolean isDirectory() { return directory; }
}
