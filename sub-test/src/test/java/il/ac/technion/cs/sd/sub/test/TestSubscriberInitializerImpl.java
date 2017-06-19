package il.ac.technion.cs.sd.sub.test;

import com.google.inject.Inject;
import il.ac.technion.cs.sd.sub.app.SubscriberInitializerImpl;
import library.Dict;
import library.DictFactory;
import library.DoubleKeyDict;
import library.DoubleKeyDictFactory;

/**
 * Created by Nadav on 19-Jun-17.
 */
public class TestSubscriberInitializerImpl extends SubscriberInitializerImpl {

    @Inject
    public TestSubscriberInitializerImpl(DictFactory dictFactory, DoubleKeyDictFactory doubleKeyDictFactory) {
        super(dictFactory, doubleKeyDictFactory);
    }

    public Dict getJournal_price_dict() {
        return journal_price_dict;
    }

    public Dict getUsers_dict() {
        return users_dict;
    }

    public DoubleKeyDict getUser_journal_history_dict() {
        return user_journal_history_dict;
    }
}
