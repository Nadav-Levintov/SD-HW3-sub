package library;

import java.util.concurrent.CompletableFuture;

/**
 * Created by benny on 20/06/2017.
 */
public class FutureLineStorageWrapperImpl implements FutureLineStorageWrapper {
    public CompletableFuture<Void> appendLine(String s)
    {
        return null;
    }

    public CompletableFuture<String> read(int lineNumber)
    {
        return null;
    }

    public CompletableFuture<Integer> numberOfLines()
    {
        return null;
    }
}
