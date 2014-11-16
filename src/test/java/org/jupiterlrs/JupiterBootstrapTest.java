package org.jupiterlrs;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jupiterlrs.bootstrap.ElasticsearchBootstrap;
import org.jupiterlrs.client.JupiterClient;
import org.jupiterlrs.commands.CreateStatementRequest;
import org.jupiterlrs.commands.CreateStatementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rx.Observer;
import rx.functions.Action1;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/services.xml")
public class JupiterBootstrapTest {

    @Autowired
    private ElasticsearchBootstrap es;

    private JupiterClient client;

    @Before
    public void setup() {
        final Jupiter jupiter = new Jupiter(es.getClient());
        assertNotNull(jupiter);
        assertNotNull(jupiter.getClient());
        client = jupiter.getClient();
    }

    @Test
    public void testAsyncObservableIndex() {
        final CountDownLatch latch = new CountDownLatch(1);

        final CreateStatementRequest request = new CreateStatementRequest();
        final CreateStatementResponse[] response = new CreateStatementResponse[1];

        client.createStatement(request).subscribe(new Action1<CreateStatementResponse>() {
            @Override
            public void call(CreateStatementResponse createStatementResponse) {
                response[0] = createStatementResponse;
                latch.countDown();
            }
        });

        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }

        assertNotNull(response[0]);
        assertNotNull(response[0].getId());
    }

    @Test
    public void testSyncObservableIndex() {
        final CreateStatementRequest request = new CreateStatementRequest();
        final CreateStatementResponse[] response = new CreateStatementResponse[1];

        client.createStatement(request).subscribe(new Observer<CreateStatementResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CreateStatementResponse createStatementResponse) {
                response[0] = createStatementResponse;
            }
        });

        assertNotNull(response[0]);
        assertNotNull(response[0].getId());
    }

    @Test
    public void testBlockingIndex() {
        final CreateStatementRequest request = new CreateStatementRequest();
        final CreateStatementResponse response = client.createStatement(request).toBlocking().single();

        assertNotNull(response);
        assertNotNull(response.getId());
    }
}
