package services;

import models.MediaFile;
import java.util.List;

// Defines the contract for all Artificial Intelligence operations.
public interface AIService {
    String generateIntroImage(String theme);
    
    // Updated to include current index and total count for sequential narrative
    String generateDescription(MediaFile file, int current, int total);
    
    // Updated to process the entire trajectory rather than just start/end points
    String generateInspirationalText(List<MediaFile> files);
}