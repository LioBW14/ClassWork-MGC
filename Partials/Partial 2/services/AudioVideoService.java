package services;

import java.util.List;

// Defines the contract for all physical media rendering operations.
public interface AudioVideoService {
    String generateAudio(String text);
    String createClip(String mediaPath, String audioPath);
    String generateMapImage(double startLat, double startLon, double endLat, double endLon);
    String compileFinalVideo(List<String> videoClips);
}