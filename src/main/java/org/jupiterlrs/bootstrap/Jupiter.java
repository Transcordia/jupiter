package org.jupiterlrs.bootstrap;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Jupiter {

    private static final Logger LOG = LoggerFactory.getLogger(Jupiter.class);

    private final CountDownLatch keepAliveLatch = new CountDownLatch(1);

    private Elasticsearch es;

    public Jupiter() {
        final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{
                "services.xml"
        });
        ctx.registerShutdownHook();

        keepAlive();
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


    public void close(String[] args) {
        keepAliveLatch.countDown();
    }

    public static void main(String[] args) {
        new Jupiter();
    }
}
