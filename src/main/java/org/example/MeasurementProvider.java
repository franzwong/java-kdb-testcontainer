package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class MeasurementProvider {
    public Stream<Measurement> getMeasurements(Path filePath) throws IOException {
        return Files.lines(filePath).map(line -> {
            String[] parts = line.split(";");
            return new Measurement(parts[0], Float.parseFloat(parts[1]));
        });
    }
}
