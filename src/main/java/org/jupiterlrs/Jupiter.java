package org.jupiterlrs;

import java.util.concurrent.CountDownLatch;

import org.elasticsearch.client.Client;
import org.jupiterlrs.client.JupiterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jupiter {

    private static final Logger LOG = LoggerFactory.getLogger(Jupiter.class);

    private final CountDownLatch keepAliveLatch = new CountDownLatch(1);

    private JupiterClient jupiterClient;

    public Jupiter(Client esClient) {
        jupiterClient = new JupiterClient(esClient);
        keepAlive();
    }

    public JupiterClient getClient() {
        return jupiterClient;
    }

    private void keepAlive() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                keepAliveLatch.countDown();
            }
        });

        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    keepAliveLatch.await();
                    LOG.info("Jupiter shut down");
                } catch (InterruptedException ignored) {
                    LOG.info("Jupiter shut down abnormally");
                }
            }
        }, "jupiter[keepalive]");

        mainThread.setDaemon(false);
        mainThread.start();

        LOG.info("Jupiter started");
    }


    public void close() {
        keepAliveLatch.countDown();
    }
}
