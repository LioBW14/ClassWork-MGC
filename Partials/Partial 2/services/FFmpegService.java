package services;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

// Handles audio generation via Google TTS API and video rendering via native FFmpeg
public class FFmpegService implements AudioVideoService {
    private final String geoapifyKey;

    public FFmpegService(String geoapifyKey) {
        this.geoapifyKey = geoapifyKey;
    }

    @Override
    public String generateAudio(String text) {
        // Google TTS has a 200 character limit, so we ensure we cut at a whole word and add a period for a natural stop.
        String safeText = text;
        if (safeText.length() > 190) {
            safeText = safeText.substring(0, 190);
            int lastSpace = safeText.lastIndexOf(" ");
            if (lastSpace > 0) {
                // Cuts at the last whole word and adds a period for a clean audio halt
                safeText = safeText.substring(0, lastSpace) + "."; 
            }
        }

        String outputPath = "process/temp_audio_" + System.currentTimeMillis() + ".mp3";
        try {
            String encodedText = URLEncoder.encode(safeText, "UTF-8");
            int textLength = safeText.length();

            String urlString = String.format(
                "https://translate.google.com/translate_tts?ie=UTF-8&q=%s&tl=en-US&client=gtx&textlen=%d&ttsspeed=1",
                encodedText, textLength
            );

            URLConnection connection = URI.create(urlString).toURL().openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            InputStream in = connection.getInputStream();
            Files.copy(in, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);

            return outputPath;
        } catch (Exception e) {
            System.out.println("Google TTS API Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String createClip(String mediaPath, String audioPath) {
        String outputPath = "process/clip_" + System.currentTimeMillis() + ".mp4";
        try {
            boolean isVideo = mediaPath.toLowerCase().endsWith(".mp4");
            List<String> command = new ArrayList<>();
            
            command.add("ffmpeg");
            command.add("-y");
            
            if (!isVideo) {
                command.add("-loop"); command.add("1");
                command.add("-framerate"); command.add("30");
            }
            
            command.add("-i"); command.add(mediaPath);
            command.add("-i"); command.add(audioPath);
            
            command.add("-map"); command.add("0:v:0");
            command.add("-map"); command.add("1:a:0");
            
            command.add("-c:v"); command.add("libx264");
            command.add("-c:a"); command.add("aac");
            command.add("-b:a"); command.add("192k");
            
            command.add("-ar"); command.add("44100");
            command.add("-ac"); command.add("2");
            
            command.add("-vf");
            command.add("scale=1080:1920:force_original_aspect_ratio=increase,crop=1080:1920");
            
            command.add("-af");
            command.add("loudnorm=I=-14:TP=-1:LRA=7");
            
            command.add("-shortest");
            command.add("-pix_fmt"); command.add("yuv420p");
            command.add(outputPath);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            
            pb.start().waitFor();
            return outputPath;
        } catch (Exception e) {
            System.out.println("FFmpeg Video Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String generateMapImage(double startLat, double startLon, double endLat, double endLon) {
        String url = String.format(
            "https://maps.geoapify.com/v1/staticmap?style=osm-carto&width=1080&height=1920" +
            "&marker=lonlat:%f,%f;type:material;color:red;size:x-large" +
            "%%7Clonlat:%f,%f;type:material;color:blue;size:x-large" +
            "&apiKey=%s",
            startLon, startLat, endLon, endLat, geoapifyKey
        );
        
        String outputPath = "process/final_map.jpg";
        try {
            URLConnection connection = URI.create(url).toURL().openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
            InputStream in = connection.getInputStream();
            Files.copy(in, Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);
            return outputPath;
        } catch (Exception e) {
            System.out.println("Map Generation Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String compileFinalVideo(List<String> videoClips) {
        String listFilePath = "process/concat_list.txt";
        String finalOutput = "FINAL_PROJECT_VIDEO.mp4";
        
        try {
            FileWriter writer = new FileWriter(listFilePath);
            for (String clip : videoClips) {
                writer.write("file '" + new File(clip).getAbsolutePath() + "'\n");
            }
            writer.close();

            ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg", "-y", "-f", "concat", "-safe", "0", 
                "-i", listFilePath, "-c", "copy", finalOutput
            );
            
            pb.redirectErrorStream(true);
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            
            pb.start().waitFor();
            
            return finalOutput;
        } catch (Exception e) {
            System.out.println("Final Compilation Error: " + e.getMessage());
            return null;
        }
    }
}