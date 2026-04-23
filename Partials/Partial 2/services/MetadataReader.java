package services;

import models.MediaFile;
import java.io.File;

// Defines the contract for extracting metadata from physical files.
// Using an interface allows switching between different extraction tools without breaking the core pipeline.
public interface MetadataReader {
    MediaFile extractMetadata(File file) throws Exception;
}