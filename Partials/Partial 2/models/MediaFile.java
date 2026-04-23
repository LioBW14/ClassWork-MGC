package models;

import java.time.LocalDateTime;

// Represents a verified media file within the system.
// Implements Comparable to allow automatic chronological sorting of files
// based on their creation date before video rendering begins.
public class MediaFile implements Comparable<MediaFile> {
    private final String filePath;
    private final GPSData gpsData;
    private final LocalDateTime creationDate;
    private final String mimeType;
    private final int orientation;

    public MediaFile(String filePath, GPSData gpsData, LocalDateTime creationDate, String mimeType, int orientation) {
        this.filePath = filePath;
        this.gpsData = gpsData;
        this.creationDate = creationDate;
        this.mimeType = mimeType;
        this.orientation = orientation;
    }

    public String getFilePath() { return filePath; }
    public GPSData getGpsData() { return gpsData; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public String getMimeType() { return mimeType; }
    public int getOrientation() { return orientation; }

    // Compares this file's date with another to ensure chronological sorting (oldest to newest).
    @Override
    public int compareTo(MediaFile other) {
        if (this.creationDate == null || other.creationDate == null) return 0;
        return this.creationDate.compareTo(other.creationDate);
    }
}