package il.ac.technion.cs.sd.sub.app;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberInitializerImpl implements SubscriberInitializer {
    @Override
    public CompletableFuture<Void> setupCsv(String csvData) {
        return null;
    }

    @Override
    public CompletableFuture<Void> setupJson(String jsonData) {
        return null;
    }
}
