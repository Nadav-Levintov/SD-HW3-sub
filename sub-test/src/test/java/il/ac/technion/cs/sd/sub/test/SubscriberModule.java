package il.ac.technion.cs.sd.sub.test;

import com.google.inject.AbstractModule;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializer;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializerImpl;
import il.ac.technion.cs.sd.sub.app.SubscriberReader;
import il.ac.technion.cs.sd.sub.app.SubscriberReaderImpl;

// This module is in the testing project, so that it could easily bind all dependencies from all levels.
public class SubscriberModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(SubscriberReader.class).to(SubscriberReaderImpl.class);
    bind(SubscriberInitializer.class).to(SubscriberInitializerImpl.class);
  }
}
