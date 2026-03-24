package com.huangyunkun.acpsure.web.model;

import java.util.List;

/**
 * Response payload for the file-browser API endpoint.
 */
public class FileListing {

    private final String currentPath;
    private final String parentPath;
    private final List<FileEntry> entries;

    public FileListing(String currentPath, String parentPath, List<FileEntry> entries) {
        this.currentPath = currentPath;
        this.parentPath = parentPath;
        this.entries = entries;
    }

    public String getCurrentPath() { return currentPath; }
    public String getParentPath() { return parentPath; }
    public List<FileEntry> getEntries() { return entries; }
}
