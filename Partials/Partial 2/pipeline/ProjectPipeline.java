package pipeline;

import models.MediaFile;
import models.GPSData;
import services.AIService;
import services.AudioVideoService;
import services.MetadataReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Main orchestrator for the video generation process
public class ProjectPipeline {
    private final MetadataReader metadataReader;
    private final AIService aiService;
    private final AudioVideoService audioVideoService;

    public ProjectPipeline(MetadataReader reader, AIService ai, AudioVideoService av) {
        this.metadataReader = reader;
        this.aiService = ai;
        this.audioVideoService = av;
    }

    public void run(String folderPath) {
        System.out.println("Starting compilation pipeline...");
        
        File processDir = new File("process");
        if (!processDir.exists()) {
            processDir.mkdir();
        }

        File folder = new File(folderPath);
        List<MediaFile> validFiles = new ArrayList<>();

        // 1. Read and filter files
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) continue;
                try {
                    MediaFile media = metadataReader.extractMetadata(file);
                    if (media != null && media.getGpsData() != null && media.getCreationDate() != null) {
                        validFiles.add(media);
                        System.out.println("Accepted file: " + file.getName());
                    } else {
                        System.out.println("Rejected file (No GPS/Date data): " + file.getName());
                    }
                } catch (Exception e) {
                    System.out.println("Error reading file " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        if (validFiles.isEmpty()) {
            System.out.println("ERROR: No valid GPS files found. Aborting process.");
            return;
        }

        //Sort chronologically
        System.out.println("Sorting files by date...");
        Collections.sort(validFiles);
        List<String> videoClips = new ArrayList<>();

        //Generate Intro
        System.out.println("Generating AI Intro Image...");
        String introImg = aiService.generateIntroImage("forest mountains");
        if (introImg != null) {
            System.out.println("Generating Intro Audio...");
            String introText = "Welcome to our journey. The image you are currently seeing is an artificial intelligence generated representation, inspired by the locations we are about to explore. Let's begin the tour.";
            String introAudio = audioVideoService.generateAudio(introText);
            
            System.out.println("Rendering Intro Clip...");
            videoClips.add(audioVideoService.createClip(introImg, introAudio));
        }

        //Process real media
        int totalFiles = validFiles.size();
        for (int i = 0; i < totalFiles; i++) {
            MediaFile media = validFiles.get(i);
            System.out.println("Generating precise location description for: " + media.getFilePath());
            String desc = aiService.generateDescription(media, i + 1, totalFiles);
            
            System.out.println("Synthesizing audio narrative...");
            String audio = audioVideoService.generateAudio(desc);
            
            System.out.println("Rendering video clip...");
            videoClips.add(audioVideoService.createClip(media.getFilePath(), audio));
        }

        //Generate Outro Map
        System.out.println("Fetching Route Map Image...");
        GPSData firstPoint = validFiles.get(0).getGpsData();
        GPSData lastPoint = validFiles.get(validFiles.size() - 1).getGpsData();
        
        String mapImg = audioVideoService.generateMapImage(
            firstPoint.getLatitude(), firstPoint.getLongitude(),
            lastPoint.getLatitude(), lastPoint.getLongitude()
        );

        if (mapImg != null) {
            System.out.println("Generating motivational trajectory summary...");
            String quote = aiService.generateInspirationalText(validFiles);
            
            System.out.println("Synthesizing Outro Audio...");
            String mapAudio = audioVideoService.generateAudio(quote);
            
            System.out.println("Rendering Final Map Clip...");
            videoClips.add(audioVideoService.createClip(mapImg, mapAudio));
        }

        //Compile Final Video
        System.out.println("Compiling all segments into final video output...");
        String finalVideo = audioVideoService.compileFinalVideo(videoClips);
        
        if (finalVideo != null) {
            System.out.println("SUCCESS: Architecture execution completed. Final video generated at " + finalVideo);
            // 7. Housekeeping: Destroys the temporary folder after successful compilation
            cleanupProcessFolder(processDir);
        } else {
            System.out.println("ERROR: Architecture failed during final segment concatenation.");
        }
    }

    //Helper method to recursively delete the temporary process directory and its contents
    private void cleanupProcessFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
            folder.delete();
            System.out.println("Housekeeping complete: Temporary processing files removed from disk.");
        }
    }
}