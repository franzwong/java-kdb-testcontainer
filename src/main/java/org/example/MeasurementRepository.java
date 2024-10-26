package org.example;

import com.kx.c;

import java.io.IOException;

public class MeasurementRepository {

    private static final String[] HEADERS = new String[] { "sym", "temperature" };

    private final c tickerPlantConn;

    public MeasurementRepository(c tickerPlantConn) {
        this.tickerPlantConn = tickerPlantConn;
    }

    public void addMeasurement(Measurement measurement) throws IOException {
        String[] stations = new String[1];
        float[] temperatures = new float[1];

        stations[0] = measurement.station();
        temperatures[0] = measurement.temperature();

        Object[] data = new Object[2];
        data[0] = stations;
        data[1] = temperatures;

        c.Flip flip = new c.Flip(HEADERS, data);
        tickerPlantConn.ks("upd", "Measurements", flip);
    }

}
