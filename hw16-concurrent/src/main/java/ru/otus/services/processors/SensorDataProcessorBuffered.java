package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;


public class SensorDataProcessorBuffered implements SensorDataProcessor {
private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final PriorityBlockingQueue<SensorData> dataBuffer;
    private final ReentrantLock lock;
    private final ArrayList<SensorData> list;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
        this.lock = new ReentrantLock();
        this.list = new ArrayList<>();
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.add(data);
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        if (lock.tryLock() && dataBuffer.size() > 0) {
            try {
                Thread.sleep(3);
                dataBuffer.drainTo(list, bufferSize);
                writer.writeBufferedData(list);
                dataBuffer.clear();
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
