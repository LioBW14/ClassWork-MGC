package models;

// Stores the geographical coordinates extracted from media files.
// This data structure separates coordinate logic from general file data,
// making it easier to pass specific location parameters to the map API.
public class GPSData {
    private final double latitude;
    private final double longitude;

    public GPSData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}