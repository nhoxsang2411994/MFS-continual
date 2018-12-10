package com.dxc.minifilesharing.file.common;

public enum AccountLevelType {
    BRONZE(5_120, 20_480, 51_200),
    SILVER(10_240, 51_200, 71_680),
    GOLD(20_480, 102_400, Integer.MAX_VALUE);

    private final int MAX_UPLOAD_SIZE;    // maximum upload size, in kilobytes
    private final int REQUIRED_UPLOAD_SIZE;   // required upload size to upgrade account level, in kilobytes
    private final int MAX_DOWNLOAD_SIZE;  // maximum download size, in kilobytes

    AccountLevelType(int maxUploadSize, int requiredUploadSize, int maxDownloadSize) {
        MAX_UPLOAD_SIZE = maxUploadSize;
        REQUIRED_UPLOAD_SIZE = requiredUploadSize;
        MAX_DOWNLOAD_SIZE = maxDownloadSize;
    }

    public int getMAX_UPLOAD_SIZE() {
        return MAX_UPLOAD_SIZE;
    }

    public int getREQUIRED_UPLOAD_SIZE() {
        return REQUIRED_UPLOAD_SIZE;
    }

    public int getMAX_DOWNLOAD_SIZE() {
        return MAX_DOWNLOAD_SIZE;
    }
}
