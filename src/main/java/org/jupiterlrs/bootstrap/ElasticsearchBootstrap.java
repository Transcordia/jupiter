package org.jupiterlrs.bootstrap;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class ElasticsearchBootstrap implements InitializingBean, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchBootstrap.class);

    private Node node;
    private Client client;

    public ElasticsearchBootstrap() {
    }

    public Client getClient() {
        return client;
    }

    public void start() {
        LOG.info("Elasticsearch starting");
        node = nodeBuilder().node();
        node.start();
        client = node.client();
        LOG.info("Elasticsearch started");
    }

    public void stop() {
        LOG.info("Elasticsearch shutting down");
        if (client != null) client.close();
        if (isRunning()) node.close();
        LOG.info("Elasticsearch shut down");
    }

    public boolean isRunning() {
        return node != null && !node.isClosed();
    }

    @Override
    public void destroy() throws Exception {
        stop();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}
