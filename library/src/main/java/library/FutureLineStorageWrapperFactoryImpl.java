package library;

import com.google.inject.Inject;
import il.ac.technion.cs.sd.sub.ext.FutureLineStorageFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class FutureLineStorageWrapperFactoryImpl implements FutureLineStorageWrapperFactory {
    private final FutureLineStorageFactory lineStorageFactory;

    @Inject
    public FutureLineStorageWrapperFactoryImpl(FutureLineStorageFactory lineStorageFactory) {
        this.lineStorageFactory = lineStorageFactory;
    }

    @Override
    public CompletableFuture<FutureLineStorageWrapper> open(String s) {
        return this.lineStorageFactory.open(s).thenCompose(r->
                r.isPresent()? CompletableFuture.completedFuture(new FutureLineStorageWrapperImpl(r.get())): this.open(s));
    }
}
