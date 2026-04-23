package services;

import models.MediaFile;
import models.GPSData;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Implements MetadataReader using the external ExifTool binary.
// This service avoids third-party Java JSON libraries by executing the OS command
// and parsing the resulting output directly using Regular Expressions.
public class ExifToolService implements MetadataReader {
    
    @Override
    public MediaFile extractMetadata(File file) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
            "exiftool", "-n", "-json", "-GPSLatitude", "-GPSLongitude", 
            "-CreateDate", "-Orientation", "-MIMEType", file.getAbsolutePath()
        );
        Process process = pb.start();
        
        StringBuilder jsonOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) jsonOutput.append(line);
        }
        process.waitFor();

        String json = jsonOutput.toString();
        
        // Manual regex extraction for zero external dependencies.
        double lat = extractDouble(json, "\"GPSLatitude\":\\s*([\\d\\.-]+)");
        double lon = extractDouble(json, "\"GPSLongitude\":\\s*([\\d\\.-]+)");
        String dateStr = extractString(json, "\"CreateDate\":\\s*\"([^\"]+)\"");
        String mime = extractString(json, "\"MIMEType\":\\s*\"([^\"]+)\"");
        int orientation = (int) extractDouble(json, "\"Orientation\":\\s*(\\d+)");

        // Discards the file immediately if no GPS coordinates are found.
        if (lat == 0.0 && lon == 0.0) return null; 

        GPSData gps = new GPSData(lat, lon);
        LocalDateTime date = null;
        if (dateStr != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
            // Uses substring(0, 19) to strip milliseconds and prevent parsing exceptions.
            date = LocalDateTime.parse(dateStr.substring(0, 19), formatter);
        }

        // Defaults orientation to 1 (normal) if the metadata tag is missing.
        return new MediaFile(file.getAbsolutePath(), gps, date, mime, orientation == 0 ? 1 : orientation);
    }

    private double extractDouble(String json, String regex) {
        Matcher m = Pattern.compile(regex).matcher(json);
        return m.find() ? Double.parseDouble(m.group(1)) : 0.0;
    }

    private String extractString(String json, String regex) {
        Matcher m = Pattern.compile(regex).matcher(json);
        return m.find() ? m.group(1) : null;
    }
}