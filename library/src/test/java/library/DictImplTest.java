package library;

import com.google.inject.Guice;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.sub.ext.LineStorageModule;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class DictImplTest {

    public Dict SetupAndBuildDataBase()
    {

        Injector injector= Guice.createInjector(new TestLineStorageModule(),new LibraryModule());
        DictFactory dictFactory= injector.getInstance(DictFactory.class);

        Dict dict =dictFactory.create("test");

        return dict;
    }
    @Test
    public void store() throws Exception {
        Injector injector= Guice.createInjector(new TestLineStorageModule(),new LibraryModule());
        DictFactory dictFactory= injector.getInstance(DictFactory.class);

        Dict dict =dictFactory.create("test");

        assertTrue(Boolean.TRUE);
    }

    @Test
    public void add() throws Exception {
    }

    @Test
    public void addAll() throws Exception {
    }

    @Test
    public void find() throws Exception {
    }

}