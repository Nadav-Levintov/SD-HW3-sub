package il.ac.technion.cs.sd.sub.app;

import com.google.inject.Inject;
import library.Dict;
import library.DictFactory;
import library.DoubleKeyDict;
import library.DoubleKeyDictFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberInitializerImpl implements SubscriberInitializer {
    private final Dict journal_price_dict;
    private final Dict users_dict;
    private final DoubleKeyDict user_journal_history_dict;

    @Inject
    public SubscriberInitializerImpl(DictFactory dictFactory, DoubleKeyDictFactory doubleKeyDictFactory) {
        String journal_dict_name = "Journal_Price";
        journal_price_dict = dictFactory.create(journal_dict_name);
        String user_dict_name = "Users";
        users_dict = dictFactory.create(user_dict_name);
        String double_dict_name = "User_journal_history";
        user_journal_history_dict = doubleKeyDictFactory.create(double_dict_name);
    }

    @Override
    public CompletableFuture<Void> setupCsv(String csvData) {
        CompletableFuture<Void> status = CompletableFuture.completedFuture(null);
        List<String> users_list = new ArrayList<>();
        Map<String,String> journal_map = new TreeMap<>();
        Map<String,Map<String,String>> user_journal_history_map = new TreeMap<>();

        final String lines[] = csvData.split("\\r\\n|\\n|\\r");

        for (String line: lines ) {
            String line_values[] = line.split(",");
            if(line_values[0].equals("journal"))
            {
                journal_map.put(line_values[1],line_values[2]);
            }else
            {
                if(!users_list.contains(line_values[1]))
                {
                    users_list.add(line_values[1]);
                    Map<String,String> history_map = new TreeMap<>();
                    history_map.put(line_values[2],"");
                    user_journal_history_map.put(line_values[1],history_map);
                }

                String history = user_journal_history_map.get(line_values[1]).get(line_values[2]);
                String new_history;
                if(line_values[0].equals("subscriber"))
                {

                    new_history=history.concat("1");
                }else
                {
                    new_history=history.concat("0");
                }
                Map<String, String> history_map_update = user_journal_history_map.get(line_values[1]);
                history_map_update.put(line_values[2],new_history);
                user_journal_history_map.put(line_values[1], history_map_update);
            }
        }

        for (String user :
                users_list) {
            users_dict.add(user,"1");
        }

        for (Map.Entry<String,String> journal :
                journal_map.entrySet()) {
            journal_price_dict.add(journal.getKey(),journal.getValue());
        }
        for (Map.Entry<String,Map<String,String>> user_map :
                user_journal_history_map.entrySet()) {

            for (Map.Entry<String,String> journal_history :
                    user_map.getValue().entrySet()) {
                user_journal_history_dict.add(user_map.getKey(),journal_history.getKey(),journal_history.getValue());
            }
        }

        users_dict.store();
        journal_price_dict.store();
        user_journal_history_dict.store();

        return status;
    }

    @Override
    public CompletableFuture<Void> setupJson(String jsonData) {
        return null;
    }
}
