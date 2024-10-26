package org.example;

import com.kx.c;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Testcontainers
public class MeasurementIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementIT.class);

    private static final Slf4jLogConsumer LOG_CONSUMER = new Slf4jLogConsumer(LOGGER);

    @Container
    private static final GenericContainer<?> tickerPlantContainer = new GenericContainer<>("local/kdb:1.0.0")
            .withLogConsumer(LOG_CONSUMER)
            .withExposedPorts(10000)
            .withCopyFileToContainer(MountableFile.forClasspathResource("/org/example/measurements_ticker_plant.q"), "/app/main.q")
            .withCommand("/app/main.q")
            .withCreateContainerCmdModifier(cmd -> cmd.withName("kdb-" + MeasurementIT.class.getName()));

    @BeforeAll
    public static void startContainers() {
        LOGGER.info("Starting containers...");
        tickerPlantContainer.start();
    }

    @AfterAll
    public static void stopContainers() {
        LOGGER.info("Stopping containers...");
        tickerPlantContainer.stop();
    }

    @Test
    public void saveMeasurementsInKdb() throws c.KException, IOException, URISyntaxException {
        var mesaurementFilePath = Paths.get(getClass().getResource("measurements.txt").toURI());

        c tickerPlantConn = null;

        try {
            tickerPlantConn = new c("localhost", tickerPlantContainer.getFirstMappedPort());
            var provider = new MeasurementProvider();
            var repository = new MeasurementRepository(tickerPlantConn);
            var service = new MeasurementService(provider, repository);
            service.populateMeasurements(mesaurementFilePath);

            var result = tickerPlantConn.k("Measurements");
            if (result instanceof c.Flip measurements) {
                var stations = (String[]) measurements.y[0];
                var temperatures = (float[]) measurements.y[1];
                assertEquals(2, stations.length);
                assertEquals("Tokyo", stations[0]);
                assertEquals("Hong Kong", stations[1]);
                assertEquals(9.6, temperatures[0], 0.0001f);
                assertEquals(23.1, temperatures[1], 0.0001f);
            }
        } finally {
            if (tickerPlantConn != null) {
                tickerPlantConn.close();
            }
        }
    }
}
