package org.zalando.tracer;

import org.apiguardian.api.API;

import java.util.concurrent.ThreadLocalRandom;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

/**
 * Generates a random 64-bit hexadecimal integer.
 */
@API(status = EXPERIMENTAL)
public final class Random64Generator implements Generator {

    @Override
    public String generate() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        // set most significant bit to produce fixed length string
        return Long.toHexString(random.nextLong() | Long.MIN_VALUE);
    }

}
