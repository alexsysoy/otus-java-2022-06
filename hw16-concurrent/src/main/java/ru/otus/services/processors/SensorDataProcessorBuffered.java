package ru.otus.services.processors;

import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;


public class SensorDataProcessorBuffered implements SensorDataProcessor {

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final ArrayBlockingQueue<SensorData> dataBuffer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.add(data);
        if (dataBuffer.size() == bufferSize) {
            flush();
        }
    }

    public void flush() {
        if (!dataBuffer.isEmpty()) {
            var list = new ArrayList<SensorData>();
            dataBuffer.drainTo(list, bufferSize);
            list.sort(Comparator.comparing(SensorData::getMeasurementTime));
            if (!list.isEmpty()) {
                writer.writeBufferedData(list);
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
