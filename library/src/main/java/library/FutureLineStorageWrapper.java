package library;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Nadav on 17-Jun-17.
 */
public interface FutureLineStorageWrapper {

    public CompletableFuture<Void> appendLine(String s);

    public CompletableFuture<String> read(int lineNumber);

    public CompletableFuture<Integer> numberOfLines();
}
