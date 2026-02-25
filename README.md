Description

  This project consists of a basic image editor developed in Java that runs entirely from the console. The program automatically loads an image named image.png, located in the root folder of the project, and allows different transformations to be applied before saving the final result.
  
  All modifications are performed directly on the image stored in memory, which means operations are cumulative: each change affects the current state of the image.

Features

  The system supports the following operations:
  
  Crop: Crops the image using two coordinates (x1, y1) and (x2, y2), forming a rectangle or square between those points.
    
  Invert: Inverts the colors of the entire image.
    
  Rotate: Rotates a selected region by 90°, 180°, or 270°.
    
  Auto Save: When selecting the exit option, the program automatically saves the modified image as editedimage.png.
  
  Input validation ensures that the coordinates provided by the user do not exceed the image boundaries.

Execution

  Make sure image.png is inside the project folder.
  
  Compile the files
  
  Run the program main
  
  Follow the instructions displayed in the console.
  
  When exiting the program, the file editedimage.png will be generated automatically.

Project Structure

  Main.java: Handles user interaction and program flow.
  
  ImageEditor.java: Contains the image processing and manipulation logic.
  
  image.png: Original input image.
  
  editedimage.png: Generated image after applying modifications.

Technical Notes

  The program uses the BufferedImage class to directly manipulate image pixels.
  Color operations are implemented using bit shifting to extract and reconstruct the RGB components of each pixel.
  
  The alpha channel is preserved to prevent transparency issues.
  Additionally, a helper method was implemented to correctly compute rectangle boundaries from two coordinates, avoiding duplicated logic across operations.
