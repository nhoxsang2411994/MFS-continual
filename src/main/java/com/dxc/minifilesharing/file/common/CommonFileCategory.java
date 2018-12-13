package com.dxc.minifilesharing.file.common;

public enum CommonFileCategory {
    audio("mp3", "ogg", "flac", "wav"),
    image("png", "jpeg", "gif", "jpg"),
    text("txt", "doc", "docx", "json", "pdf"),
    video("mp4", "wmv", "mov", "avi", "flv"),
    application("pdf", "json", "zip"),
    unclassified;

    private final String[] extensions;

    CommonFileCategory(String... extensions) {
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
