package com.baker.Utils;

public class SnowflakeIdGenerator {

    // 下面两个参数分别表示起始时间戳（2021-01-01）和数据中心ID位数
    private static final long START_TIME = 1609430400000L;
    private static final long DATA_CENTER_ID_BITS = 5L;

    private final long dataCenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id.");
        }
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << 12) - 1);
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - START_TIME) << (64 - 41))
                | (dataCenterId << (64 - 41 - DATA_CENTER_ID_BITS))
                | (sequence << (64 - 41 - DATA_CENTER_ID_BITS - 12));
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
