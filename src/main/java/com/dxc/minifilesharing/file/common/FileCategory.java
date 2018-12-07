package com.dxc.minifilesharing.file.common;

public enum FileCategory {
    ARCHIVE("zip", "rar", "7z"),
    AUDIO("mp3", "ogg", "flac", "wav"),
    IMAGE("png", "jpeg", "gif", "jpg"),
    TEXT("txt", "doc", "docx", "json", "pdf"),
    VIDEO("mp4", "wmv", "mov", "avi", "flv"),
    UNCLASSIFIED("unclassified");

    private final String[] extensions;

    FileCategory(String... extensions) {
        this.extensions = new String[extensions.length];
        for (int i = 0, l = extensions.length; i < l; i++) {
            this.extensions[i] = extensions[i];
        }
    }

    /**
     * Get extensions of this category of file
     * <p>
     * @return file extensions
     */
    public String[] getExtensions() {
        return extensions;
    }
}
