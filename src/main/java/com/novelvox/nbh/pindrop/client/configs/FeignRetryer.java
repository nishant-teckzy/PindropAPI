package com.novelvox.nbh.pindrop.client.configs;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class FeignRetryer implements Retryer {
    private final int maxAttempts;
    private int attempt = 1;
    private final long cooloff;

    public FeignRetryer() {
        this(1000, 2);
    }

    public FeignRetryer(long period, int maxAttempts) {
        this.cooloff = period;
        this.maxAttempts = maxAttempts;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            throw e;
        }
        try {
            Thread.sleep(cooloff);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new FeignRetryer(cooloff, maxAttempts);
    }
}

