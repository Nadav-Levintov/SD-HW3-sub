package il.ac.technion.cs.sd.sub.app;


import com.google.inject.Inject;
import library.Dict;
import library.DictFactory;
import library.DoubleKeyDict;
import library.DoubleKeyDictFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberInitializerImpl implements SubscriberInitializer {
    protected final Dict journal_price_dict;
    protected final Dict users_dict;
    protected final DoubleKeyDict user_journal_history_dict;

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
        Set<String> users_set = new HashSet<>();
        Map<String,String> journal_map = new TreeMap<>();
        Map<String,Map<String,List<String>>> user_journal_history_map = new TreeMap<>();

        final String lines[] = csvData.split("\\r\\n|\\n|\\r");

        for (String line: lines ) {
            String line_values[] = line.split(",");
            if(line_values[0].equals("journal"))
            {
                journal_map.put(line_values[1],line_values[2]);
            }else
            {
                if(!users_set.contains(line_values[1]))     //case first time see user
                {
                    users_set.add(line_values[1]);
                    Map<String,List<String>> history_map = new TreeMap<>();
                    user_journal_history_map.put(line_values[1],history_map);
                }
                if(!user_journal_history_map.get(line_values[1]).containsKey(line_values[2]))  //case first time see journal
                {
                    List<String> journal_history = new ArrayList<>();
                    user_journal_history_map.get(line_values[1]).put(line_values[2],journal_history);
                }

                List<String> journal_history = user_journal_history_map.get(line_values[1]).get(line_values[2]);
                if (line_values[0].equals("subscriber"))
                {
                    journal_history.add("1");
                } else
                    {
                    if (!(!journal_history.isEmpty() && journal_history.get(journal_history.size() - 1).equals("0")))
                    {  //what happens when string "" (length 0?)
                        journal_history.add("0");
                    }
                }
                user_journal_history_map.get(line_values[1]).put(line_values[2],journal_history); //is this necessary?
            }
        }

        return filter_data_init_dcits_and_store(users_set, journal_map, user_journal_history_map);
    }

    private CompletableFuture<Void> filter_data_init_dcits_and_store(Set<String> users_set, Map<String, String> journal_map, Map<String, Map<String,List<String>>> user_journal_history_map) {
        for (String user :
                users_set) {
            users_dict.add(user,"1");
        }

        for (Map.Entry<String,String> journal :
                journal_map.entrySet()) {
            journal_price_dict.add(journal.getKey(),journal.getValue());
        }
        for (Map.Entry<String,Map<String,List<String>>> user_map : user_journal_history_map.entrySet())
        {
            for (Map.Entry<String,List<String>> journal_history : user_map.getValue().entrySet()) {
                if(journal_map.containsKey(journal_history.getKey()))
                {
                    user_journal_history_dict.add(user_map.getKey(),journal_history.getKey(),journal_history.getValue().toArray().toString());
                }
            }
        }

        users_dict.store();
        journal_price_dict.store();
        user_journal_history_dict.store();

        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> setupJson(String jsonData)
    {
       // CompletableFuture<Void> status = CompletableFuture.completedFuture(null);
        Set<String> users_set = new HashSet<>();
        Map<String,String> journal_map = new TreeMap<>();
        Map<String,Map<String,List<String>>> user_journal_history_map = new TreeMap<>(); //key1 - user key2-journal

        try
        {
            JSONArray arr = new JSONArray(jsonData);
            JSONObject obj;
            int arraySize = arr.length();
            for (int i = 0; i < arraySize; i++)
            {
                obj = arr.getJSONObject(i);
                String type = obj.getString("type");
                String value = "0";
                switch (type) {
                    case "journal":
                        journal_map.put(obj.getString("journal-id"), obj.getString("priced"));
                        break;
                    case "subscription":
                         value = "1";
                    case "cancel":
                        String user_id = obj.getString("user-id");
                        String journal_id = obj.getString("journal-id");
                        if(!users_set.contains(user_id))
                        {
                            users_set.add(user_id);
                            Map<String,List<String>> user_history_map = new TreeMap<>();
                            List<String> journal_history = new ArrayList<>();
                            user_history_map.put(journal_id,journal_history);
                            user_journal_history_map.put(user_id,user_history_map);
                        }
                        List<String> j_history = user_journal_history_map.get(user_id).get(journal_id);
                        if(!(value.equals("0") && !j_history.isEmpty() && j_history.get(j_history.size()-1).equals("0")))  // case no cancel after cancel
                            user_journal_history_map.get(user_id).get(journal_id).add(value);
                        break;
                    default:
                        System.out.println("JSON file is not legal");
                }
            }
        } catch (JSONException e)
        {
            System.out.println("catch JSONException");
        }

        return filter_data_init_dcits_and_store(users_set, journal_map, user_journal_history_map);

    }


}
