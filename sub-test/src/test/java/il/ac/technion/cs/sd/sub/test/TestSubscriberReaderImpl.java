package il.ac.technion.cs.sd.sub.test;

import il.ac.technion.cs.sd.sub.app.SubscriberReaderImpl;
import library.Dict;
import library.DoubleKeyDict;

/**
 * Created by Nadav on 19-Jun-17.
 */
public class TestSubscriberReaderImpl extends SubscriberReaderImpl {
    protected TestSubscriberReaderImpl(Dict users_dict, Dict journal_dict, DoubleKeyDict history) {
    this.users_dict = users_dict;
    this.user_journal_history_dict = history;
    this.journal_price_dict = journal_dict;
    }
}
