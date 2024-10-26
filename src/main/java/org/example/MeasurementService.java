package org.example;

import java.io.IOException;
import java.nio.file.Path;

public class MeasurementService {

    private final MeasurementProvider provider;
    private final MeasurementRepository repository;

    public MeasurementService(MeasurementProvider provider, MeasurementRepository repository) {
        this.provider = provider;
        this.repository = repository;
    }

    public void populateMeasurements(Path filePath) throws IOException {
        try (var measurementStream = provider.getMeasurements(filePath)) {
            measurementStream.forEach(measurement -> {
                try {
                    repository.addMeasurement(measurement);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException) {
                throw (IOException) e.getCause();
            } else {
                throw e;
            }
        }
    }

}
