import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*Hi teachers you need to know this: Before running the program, set the environment variable OpenAIToken
with your OpenAI API key.
The code compiles correctly, but a valid API key with available quota is required for real translations.
Like this in the carpet where you run the program: $env:OpenAIToken="your_api_key_here"
I am going to trust in my coide because i created my API key, but i did not put money in it, so if I run 
the program, it will show an error about insufficient quota.
I am foreigner, i don't have money :)
*/

public class FileTranslator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Read the API token from the environment variable
            String apiToken = System.getenv("OpenAIToken");

            if (apiToken == null || apiToken.isBlank()) {
                System.out.println("Error: OpenAIToken was not found.");
                return;
            }

            // Ask for the txt file name or path
            System.out.print("Enter the .txt file name or path: ");
            String fileName = scanner.nextLine();

            // Create the path and read the file content
            Path inputPath = Paths.get(fileName);
            String originalText = Files.readString(inputPath, StandardCharsets.UTF_8);

            // Ask for the target language
            System.out.print("Enter the target language: ");
            String targetLanguage = scanner.nextLine();

            // Build the prompt
            String prompt = "Translate the following text into " + targetLanguage +
                    ". Return only the translated text:\n\n" + originalText;

            // Build the JSON request
            String jsonBody = "{\n" +
                    "  \"model\": \"gpt-4.1-mini\",\n" +
                    "  \"input\": " + toJsonString(prompt) + "\n" +
                    "}";

            // Save the JSON request in a temporary file
            Path requestFile = Paths.get("request_temp.json");
            Files.writeString(requestFile, jsonBody, StandardCharsets.UTF_8);

            // Execute curl with ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(
                    "curl.exe",
                    "https://api.openai.com/v1/responses",
                    "-H", "Content-Type: application/json",
                    "-H", "Authorization: Bearer " + apiToken,
                    "--data-binary", "@" + requestFile.toAbsolutePath()
            );

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Read the API response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            process.waitFor();

            String apiResponse = response.toString();

            // If there is no quota, show the response and stop
            if (apiResponse.contains("insufficient_quota")) {
                System.out.println("Error: insufficient quota.");
                System.out.println(apiResponse);
                return;
            }

            // Save the raw response into a new txt file
            Path outputPath = Paths.get("translated_" + inputPath.getFileName().toString());
            Files.writeString(outputPath, apiResponse, StandardCharsets.UTF_8);

            System.out.println("Done.");
            System.out.println("Output file: " + outputPath.toAbsolutePath());

        } catch (Exception e) {
            System.out.println("Error:");
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Convert a normal String into a valid JSON string
    public static String toJsonString(String text) {
        return "\"" + text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t") + "\"";
    }
}