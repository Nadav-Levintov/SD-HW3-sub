import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializer;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializerImpl;
import il.ac.technion.cs.sd.sub.app.SubscriberReader;
import il.ac.technion.cs.sd.sub.app.SubscriberReaderImpl;
import il.ac.technion.cs.sd.sub.ext.FutureLineStorageFactory;
import library.LibraryModule;

public class FakeSubscriberModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new LibraryModule());
        bind(SubscriberInitializer.class).to(SubscriberInitializerImpl.class);
        bind(SubscriberReader.class).to(SubscriberReaderImpl.class);
        bind(FutureLineStorageFactory.class).toProvider(FakeFactoryProvider.class);
    }
}

@Singleton
class FakeFactoryProvider implements Provider<FutureLineStorageFactoryFake> {
    static FutureLineStorageFactoryFake ret = new FutureLineStorageFactoryFake();

    @Override
    public FutureLineStorageFactoryFake get() {
        return ret;
    }
}
