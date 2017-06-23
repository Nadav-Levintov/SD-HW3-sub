package il.ac.technion.cs.sd.sub.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.sub.app.SubscriberReader;
import library.LibraryModule;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberReaderImplTest {

    private static TestSubscriberInitializerImpl setupAndGetInitializer(String fileName) throws Exception {
        String fileContents =
                new Scanner(new File(ExampleTest.class.getResource(fileName).getFile())).useDelimiter("\\Z").next();
        Injector injector = Guice.createInjector(new SubscriberModule(), new TestLineStorageModule());
        TestSubscriberInitializerImpl si = injector.getInstance(TestSubscriberInitializerImpl.class);
        CompletableFuture<Void> setup =
                fileName.endsWith("csv") ? si.setupCsv(fileContents) : si.setupJson(fileContents);
        setup.get();
        return si;
    }

    @Test
    public void isSubscribed_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.isSubscribed("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.isSubscribed("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.isSubscribed("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.isSubscribed("nadav","bla");


        assertTrue(val1.get().get());
        assertFalse(val2.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());

    }

    @Test
    public void wasSubscribed_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.wasSubscribed("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.wasSubscribed("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.wasSubscribed("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.wasSubscribed("nadav","bla");


        assertTrue(val2.get().get());
        assertTrue(val1.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());
    }

    @Test
    public void isCanceled_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.isCanceled("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.isCanceled("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.isCanceled("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.isCanceled("nadav","bla");


        assertTrue(val2.get().get());
        assertFalse(val1.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());
    }

    @Test
    public void wasCanceled_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.wasCanceled("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.wasCanceled("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.wasCanceled("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.wasCanceled("nadav","bla");


        assertTrue(val2.get().get());
        assertFalse(val1.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());
    }

    @Test
    public void getSubscribedJournals_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<List<String>> val1 = reader.getSubscribedJournals("nadav");
        CompletableFuture<List<String>> val2 = reader.getSubscribedJournals("benny");
        CompletableFuture<List<String>> val3 = reader.getSubscribedJournals("otherdude");

        List<String> res_list = new ArrayList<>();
        res_list.add("aaa");
        res_list.add("newsletter");


        assertTrue(val2.get().isEmpty());
        assertEquals(res_list,val1.get());
        assertTrue(val3.get().isEmpty());
    }

    @Test
    public void getAllSubscriptions_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Map<String,List<Boolean>>> val1 = reader.getAllSubscriptions("nadav");
        CompletableFuture<Map<String,List<Boolean>>> val2= reader.getAllSubscriptions("benny");
        CompletableFuture<Map<String,List<Boolean>>> val3 = reader.getAllSubscriptions("otherdude");

        Map<String,List<Boolean>> res_map1 = new TreeMap<>();
        List<Boolean> hist1 = new ArrayList<>();
        hist1.add(Boolean.TRUE);
        List<Boolean> hist2 = new ArrayList<>();
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.FALSE);
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.TRUE);
        res_map1.put("newsletter",hist1);
        res_map1.put("aaa",hist2);
        Map<String,List<Boolean>> res_map2 = new TreeMap<>();

        List<Boolean> hist3 = new ArrayList<>();
        hist3.add(Boolean.TRUE);
        hist3.add(Boolean.FALSE);

        res_map2.put("newsletter",hist3);
        res_map2.put("aaa",hist3);





        assertEquals(res_map1,val1.get());
        assertEquals(res_map2,val2.get());
        assertTrue(val3.get().isEmpty());
    }

    @Test
    public void getMonthlyBudget_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<OptionalInt> val1 = reader.getMonthlyBudget("nadav");
        CompletableFuture<OptionalInt> val2 = reader.getMonthlyBudget("benny");
        CompletableFuture<OptionalInt> val3 = reader.getMonthlyBudget("otherdude");



        assertEquals(350,val1.get().getAsInt());
        assertEquals(0,val2.get().getAsInt());
        assertFalse(val3.get().isPresent());
    }

    @Test
    public void getSubscribedUsers_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<List<String>> val1 = reader.getSubscribedUsers("aaa");
        CompletableFuture<List<String>> val2 = reader.getSubscribedUsers("newsletter");
        CompletableFuture<List<String>> val3 = reader.getSubscribedUsers("otherdude");

        List<String> res_list = new ArrayList<>();
        res_list.add("nadav");

        assertEquals(res_list,val1.get());
        assertEquals(res_list,val2.get());
        assertTrue(val3.get().isEmpty());


    }

    @Test
    public void getMonthlyIncome_csv() throws Exception {

        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<OptionalInt> val1 = reader.getMonthlyIncome("aaa");
        CompletableFuture<OptionalInt> val2 = reader.getMonthlyIncome("newsletter");
        CompletableFuture<OptionalInt> val3 = reader.getMonthlyIncome("otherjournal");



        assertEquals(250,val1.get().getAsInt());
        assertEquals(100,val2.get().getAsInt());
        assertFalse(val3.get().isPresent());
    }

    @Test
    public void getSubscribers_csv() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.csv");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Map<String,List<Boolean>>> val1 = reader.getSubscribers("aaa");
        CompletableFuture<Map<String,List<Boolean>>> val2= reader.getSubscribers("newsletter");
        CompletableFuture<Map<String,List<Boolean>>> val3 = reader.getSubscribers("otherjournal");

        Map<String,List<Boolean>> res_map1 = new TreeMap<>();
        List<Boolean> hist1 = new ArrayList<>();
        hist1.add(Boolean.TRUE);
        hist1.add(Boolean.FALSE);
        List<Boolean> hist2 = new ArrayList<>();
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.FALSE);
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.TRUE);
        res_map1.put("benny",hist1);
        res_map1.put("nadav",hist2);
        Map<String,List<Boolean>> res_map2 = new TreeMap<>();

        List<Boolean> hist3 = new ArrayList<>();
        hist3.add(Boolean.TRUE);
        hist3.add(Boolean.FALSE);
        List<Boolean> hist4 = new ArrayList<>();
        hist4.add(Boolean.TRUE);


        res_map2.put("benny",hist3);
        res_map2.put("nadav",hist4);



        assertEquals(res_map1,val1.get());
        assertEquals(res_map2,val2.get());
        assertTrue(val3.get().isEmpty());
    }

    @Test
    public void isSubscribed_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.isSubscribed("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.isSubscribed("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.isSubscribed("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.isSubscribed("nadav","bla");


        assertTrue(val1.get().get());
        assertFalse(val2.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());

    }

    @Test
    public void wasSubscribed_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.wasSubscribed("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.wasSubscribed("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.wasSubscribed("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.wasSubscribed("nadav","bla");


        assertTrue(val2.get().get());
        assertTrue(val1.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());
    }

    @Test
    public void isCanceled_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.isCanceled("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.isCanceled("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.isCanceled("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.isCanceled("nadav","bla");


        assertTrue(val2.get().get());
        assertFalse(val1.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());
    }

    @Test
    public void wasCanceled_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Optional<Boolean>> val1 = reader.wasCanceled("nadav","newsletter");
        CompletableFuture<Optional<Boolean>> val2 = reader.wasCanceled("benny","newsletter");
        CompletableFuture<Optional<Boolean>> val3 = reader.wasCanceled("otherdude","newsletter");
        CompletableFuture<Optional<Boolean>> val4 = reader.wasCanceled("nadav","bla");


        assertTrue(val2.get().get());
        assertFalse(val1.get().get());
        assertFalse(val3.get().isPresent());
        assertFalse(val4.get().get());
    }

    @Test
    public void getSubscribedJournals_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<List<String>> val1 = reader.getSubscribedJournals("nadav");
        CompletableFuture<List<String>> val2 = reader.getSubscribedJournals("benny");
        CompletableFuture<List<String>> val3 = reader.getSubscribedJournals("otherdude");

        List<String> res_list = new ArrayList<>();
        res_list.add("aaa");
        res_list.add("newsletter");


        assertTrue(val2.get().isEmpty());
        assertEquals(res_list,val1.get());
        assertTrue(val3.get().isEmpty());
    }

    @Test
    public void getAllSubscriptions_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Map<String,List<Boolean>>> val1 = reader.getAllSubscriptions("nadav");
        CompletableFuture<Map<String,List<Boolean>>> val2= reader.getAllSubscriptions("benny");
        CompletableFuture<Map<String,List<Boolean>>> val3 = reader.getAllSubscriptions("otherdude");

        Map<String,List<Boolean>> res_map1 = new TreeMap<>();
        List<Boolean> hist1 = new ArrayList<>();
        hist1.add(Boolean.TRUE);
        List<Boolean> hist2 = new ArrayList<>();
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.FALSE);
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.TRUE);
        res_map1.put("newsletter",hist1);
        res_map1.put("aaa",hist2);
        Map<String,List<Boolean>> res_map2 = new TreeMap<>();

        List<Boolean> hist3 = new ArrayList<>();
        hist3.add(Boolean.TRUE);
        hist3.add(Boolean.FALSE);

        res_map2.put("newsletter",hist3);
        res_map2.put("aaa",hist3);





        assertEquals(res_map1,val1.get());
        assertEquals(res_map2,val2.get());
        assertTrue(val3.get().isEmpty());
    }

    @Test
    public void getMonthlyBudget_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<OptionalInt> val1 = reader.getMonthlyBudget("nadav");
        CompletableFuture<OptionalInt> val2 = reader.getMonthlyBudget("benny");
        CompletableFuture<OptionalInt> val3 = reader.getMonthlyBudget("otherdude");



        assertEquals(350,val1.get().getAsInt());
        assertEquals(0,val2.get().getAsInt());
        assertFalse(val3.get().isPresent());
    }

    @Test
    public void getSubscribedUsers_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<List<String>> val1 = reader.getSubscribedUsers("aaa");
        CompletableFuture<List<String>> val2 = reader.getSubscribedUsers("newsletter");
        CompletableFuture<List<String>> val3 = reader.getSubscribedUsers("otherdude");

        List<String> res_list = new ArrayList<>();
        res_list.add("nadav");

        assertEquals(res_list,val1.get());
        assertEquals(res_list,val2.get());
        assertTrue(val3.get().isEmpty());


    }

    @Test
    public void getMonthlyIncome_json() throws Exception {

        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<OptionalInt> val1 = reader.getMonthlyIncome("aaa");
        CompletableFuture<OptionalInt> val2 = reader.getMonthlyIncome("newsletter");
        CompletableFuture<OptionalInt> val3 = reader.getMonthlyIncome("otherjournal");



        assertEquals(250,val1.get().getAsInt());
        assertEquals(100,val2.get().getAsInt());
        assertFalse(val3.get().isPresent());
    }

    @Test
    public void getSubscribers_json() throws Exception {
        TestSubscriberInitializerImpl initializer =  setupAndGetInitializer("basic.json");
        SubscriberReader reader = new TestSubscriberReaderImpl(initializer.getUsers_dict(),initializer.getJournal_price_dict(),initializer.getUser_journal_history_dict());

        CompletableFuture<Map<String,List<Boolean>>> val1 = reader.getSubscribers("aaa");
        CompletableFuture<Map<String,List<Boolean>>> val2= reader.getSubscribers("newsletter");
        CompletableFuture<Map<String,List<Boolean>>> val3 = reader.getSubscribers("otherjournal");

        Map<String,List<Boolean>> res_map1 = new TreeMap<>();
        List<Boolean> hist1 = new ArrayList<>();
        hist1.add(Boolean.TRUE);
        hist1.add(Boolean.FALSE);
        List<Boolean> hist2 = new ArrayList<>();
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.FALSE);
        hist2.add(Boolean.TRUE);
        hist2.add(Boolean.TRUE);
        res_map1.put("benny",hist1);
        res_map1.put("nadav",hist2);
        Map<String,List<Boolean>> res_map2 = new TreeMap<>();

        List<Boolean> hist3 = new ArrayList<>();
        hist3.add(Boolean.TRUE);
        hist3.add(Boolean.FALSE);
        List<Boolean> hist4 = new ArrayList<>();
        hist4.add(Boolean.TRUE);


        res_map2.put("benny",hist3);
        res_map2.put("nadav",hist4);



        assertEquals(res_map1,val1.get());
        assertEquals(res_map2,val2.get());
        assertTrue(val3.get().isEmpty());
    }

}