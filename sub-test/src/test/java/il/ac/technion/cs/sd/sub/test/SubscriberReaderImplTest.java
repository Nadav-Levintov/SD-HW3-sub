package il.ac.technion.cs.sd.sub.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.sub.app.SubscriberReader;
import library.LibraryModule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberReaderImplTest {
    @Test
    public void isSubscribed() throws Exception {
        Injector injector = Guice.createInjector(new SubscriberModule(),new TestLineStorageModule());
        SubscriberReader subscriberReader = injector.getInstance(SubscriberReader.class);

        assertTrue(Boolean.TRUE);
    }

    @Test
    public void wasSubscribed() throws Exception {
    }

    @Test
    public void isCanceled() throws Exception {
    }

    @Test
    public void wasCanceled() throws Exception {
    }

    @Test
    public void getSubscribedJournals() throws Exception {
    }

    @Test
    public void getAllSubscriptions() throws Exception {
    }

    @Test
    public void getMonthlyBudget() throws Exception {
    }

    @Test
    public void getSubscribedUsers() throws Exception {
    }

    @Test
    public void getMonthlyIncome() throws Exception {
    }

    @Test
    public void getSubscribers() throws Exception {
    }

}