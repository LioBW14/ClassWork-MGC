import pipeline.ProjectPipeline;
import services.ExifToolService;
import services.GeminiImplementation;
import services.FFmpegService;

// The entry point of the application. Responsible for initializing the environment
// and injecting dependencies into the core pipeline.
public class Main {
    public static void main(String[] args) {
        // System variables required for external service authentication.
        // Must be populated before execution.
        String geminiApiKey = "GEMINI_API_KEY_HERE"; 
        String geoapifyMapKey = "GEOAPIFY_MAP_KEY_HERE"; 

        // Dependency Injection Setup: Instantiates the specific tools that will be used.
        ProjectPipeline pipeline = new ProjectPipeline(
            new ExifToolService(),
            new GeminiImplementation(geminiApiKey),
            new FFmpegService(geoapifyMapKey)
        );

        // Initiates the process targeting the designated source folder.
        pipeline.run("./Files_MGC");
    }
}