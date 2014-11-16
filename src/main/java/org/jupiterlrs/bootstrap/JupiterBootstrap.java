package org.jupiterlrs.bootstrap;

import org.jupiterlrs.Jupiter;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JupiterBootstrap {

    private static Jupiter jupiter;

    public static void close() {
        jupiter.close();
    }

    public static void main(String[] args) {
        final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{
            "services.xml"
        });

        ElasticsearchBootstrap es = ctx.getBean(ElasticsearchBootstrap.class);
        jupiter = new Jupiter(es.getClient());

        ctx.registerShutdownHook();
    }
}
