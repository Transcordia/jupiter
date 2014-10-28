package org.jupiterlrs.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

public class ESBootstrap {


    private final Node node;
    private Client client;

    public ESBootstrap() {
        final Settings settings = ImmutableSettings.settingsBuilder().build();
        node = nodeBuilder().client(true).settings(settings).build();
    }

    public Client getClient() {
        return client;
    }

    public void start() {
        node.start();
        client = node.client();
    }

    public void stop() {
        if (client != null) client.close();
        if (node != null && !node.isClosed()) node.close();
    }
}
