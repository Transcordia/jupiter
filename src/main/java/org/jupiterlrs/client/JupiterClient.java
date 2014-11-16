package org.jupiterlrs.client;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.jupiterlrs.commands.CreateStatementRequest;
import org.jupiterlrs.commands.CreateStatementResponse;
import rx.Observable;
import rx.functions.Func1;

public class JupiterClient {

    private org.elasticsearch.client.Client client;

    public JupiterClient(org.elasticsearch.client.Client client) {
        this.client = client;
    }


    public Observable<CreateStatementResponse> createStatement(CreateStatementRequest request) {
        final IndexRequest indexRequest = new IndexRequest();
        indexRequest
            .index("jupiter")
            .type("test")
            .source("{ hello: \"World\" }");
        final ActionFuture<IndexResponse> response = client.index(indexRequest);
        return Observable.from(response).map(new Func1<IndexResponse, CreateStatementResponse>() {
            @Override
            public CreateStatementResponse call(IndexResponse indexResponse) {
                CreateStatementResponse response = new CreateStatementResponse();
                response.setId(indexResponse.getId());
                return response;
            }
        });
    }
}


