package il.ac.technion.cs.sd.sub.test;

import com.google.inject.AbstractModule;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializer;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializerImpl;
import il.ac.technion.cs.sd.sub.app.SubscriberReader;
import il.ac.technion.cs.sd.sub.app.SubscriberReaderImpl;
import library.LibraryModule;
import org.junit.Test;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
public class TestSubscriberModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new LibraryModule());
    bind(SubscriberReader.class).to(TestSubscriberReaderImpl.class);
    bind(TestSubscriberInitializerImpl.class).to(TestSubscriberInitializerImpl.class);
  }
}
