package org.jupiterlrs.bootstrap;

import java.util.concurrent.CountDownLatch;

import org.jupiterlrs.elasticsearch.ESBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {

    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);

    private static Bootstrap bootstrap;
    private static ESBootstrap esBootstrap;

    private static volatile CountDownLatch keepAliveLatch;

    public static void main(String[] args) {
        esBootstrap = new ESBootstrap();

        bootstrap = new Bootstrap();
        bootstrap.start();
    }

    private void setup() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                bootstrap.destroy();
            }
        });

    }

    private void start() {
        bootstrap.setup();

        esBootstrap.start();

        // Start the Jupiter server keepalive thread
        try {
            keepAliveLatch = new CountDownLatch(1);

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
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "jupiter[keepalive]");

            mainThread.setDaemon(false);
            mainThread.start();

            LOG.info("Jupiter started successfully");
        } catch (Exception e) {
            LOG.error("Leaving Jupiter abnormally", e);
            System.exit(-1);
        }
    }

    public static void close(String[] args) {
        bootstrap.destroy();
        keepAliveLatch.countDown();
    }

    private void destroy() {
        esBootstrap.stop();
    }
}
