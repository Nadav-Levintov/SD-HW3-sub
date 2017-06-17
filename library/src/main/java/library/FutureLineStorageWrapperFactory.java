package library;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Nadav on 17-Jun-17.
 */
public interface FutureLineStorageWrapperFactory {

    public CompletableFuture<FutureLineStorageWrapper> open(String s);
}
