package il.ac.technion.cs.sd.sub.app;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import il.ac.technion.cs.sd.sub.ext.LineStorageModule;
import library.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberReaderImpl implements SubscriberReader {
    protected Dict journal_price_dict;
    protected Dict users_dict;
    protected DoubleKeyDict user_journal_history_dict;

    @Inject
    public SubscriberReaderImpl(DictFactory dictFactory, DoubleKeyDictFactory doubleKeyDictFactory) {
        String journal_dict_name = "Journal_Price";
        journal_price_dict = dictFactory.create(journal_dict_name);
        String user_dict_name = "Users";
        users_dict = dictFactory.create(user_dict_name);
        String double_dict_name = "User_journal_history";
        user_journal_history_dict = doubleKeyDictFactory.create(double_dict_name);
    }
    protected SubscriberReaderImpl()
    {
        user_journal_history_dict=null;
        users_dict = null;
        journal_price_dict = null;
    }


    @Override
    public CompletableFuture<Optional<Boolean>> isSubscribed(String userId, String journalId) {
        CompletableFuture<Optional<String>> user = users_dict.find("userId");

        return user.thenCompose(optional_user ->
        {
            if(!optional_user.isPresent())
            {
                return CompletableFuture.completedFuture(Optional.empty());
            }

            CompletableFuture<Optional<String>> history = user_journal_history_dict.findByKeys(userId, journalId);

            return history.thenApply(optional_history -> {
                if(!optional_history.isPresent())
                {
                    return Optional.of(Boolean.FALSE);
                }

                String hist = optional_history.get();
                if(hist.charAt(hist.length()-1) == '1')
                {
                    return Optional.of(Boolean.TRUE);
                }
                else
                {
                    return Optional.of(Boolean.FALSE);
                }
            });
        });
    }

    @Override
    public CompletableFuture<Optional<Boolean>> wasSubscribed(String userId, String journalId) {
        return userHistoryContains(userId, journalId,"1");


    }

    @Override
    public CompletableFuture<Optional<Boolean>> isCanceled(String userId, String journalId) {
        return isSubscribed(userId, journalId).thenApply(optional_bool ->
                optional_bool.map(aBoolean -> !aBoolean));
    }

    @Override
    public CompletableFuture<Optional<Boolean>> wasCanceled(String userId, String journalId) {
        return userHistoryContains(userId, journalId,"0");
    }

    @Override
    public CompletableFuture<List<String>> getSubscribedJournals(String userId) {
        CompletableFuture<Map<String, String>> user_journal_history = user_journal_history_dict.findByMainKey(userId);
        return user_journal_history.thenApply(map ->
        {
            List<String> res_list = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                String journal = entry.getKey();
                String history = entry.getValue();
                if(history.charAt(history.length()-1) == '1')
                {
                    res_list.add(journal);
                }
            }
            return res_list.stream().sorted().collect(Collectors.toList());
        });

    }

    @Override
    public CompletableFuture<Map<String, List<Boolean>>> getAllSubscriptions(String userId) {
        CompletableFuture<Map<String, String>> user_journal_history = user_journal_history_dict.findByMainKey(userId);
        return user_journal_history.thenApply(map ->
        {
            Map<String, List<Boolean>> res_map = new TreeMap<>();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                String journal = entry.getKey();
                String history = entry.getValue();
                res_map.put(journal,convert_history_from_string_to_bool_list(history));
            }
            return res_map;
        });
    }

    @Override
    public CompletableFuture<OptionalInt> getMonthlyBudget(String userId) {
        CompletableFuture<Optional<String>> user = users_dict.find(userId);

        return user.thenCompose(optional_user ->
        {
            if(!optional_user.isPresent())
            {
                return CompletableFuture.completedFuture(OptionalInt.empty());
            }

            CompletableFuture<Map<String, String>> user_journal_history = user_journal_history_dict.findByMainKey(userId);
            CompletableFuture<Integer> res_integer = user_journal_history.thenCompose(map ->
            {
                CompletableFuture<Integer> res = CompletableFuture.completedFuture(0);
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                    String journal = entry.getKey();
                    String history = entry.getValue();
                    if(history.charAt(history.length()-1) == '1')
                    {
                        CompletableFuture<Optional<String>> journal_price = journal_price_dict.find(journal);
                        res.thenCombine(journal_price, (res_val,price)-> res_val+=Integer.parseInt(price.get())).thenApply(i->i);
                    }
                }
                return res;
            });

            return res_integer.thenApply(OptionalInt::of);
        });
    }

    @Override
    public CompletableFuture<List<String>> getSubscribedUsers(String journalId) {
        CompletableFuture<Map<String, String>> user_journal_history = user_journal_history_dict.findBySecondaryKey(journalId);
        return user_journal_history.thenApply(map ->
        {
            List<String> res_list = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                String user = entry.getKey();
                String history = entry.getValue();
                if(history.charAt(history.length()-1) == '1')
                {
                    res_list.add(user);
                }
            }
            return res_list.stream().sorted().collect(Collectors.toList());
        });
    }

    @Override
    public CompletableFuture<OptionalInt> getMonthlyIncome(String journalId) {
        CompletableFuture<Optional<String>> journal_price = journal_price_dict.find(journalId);


        return journal_price.thenCompose(optional_journal ->
        {
            if(!optional_journal.isPresent())
            {
                return CompletableFuture.completedFuture(OptionalInt.empty());
            }
            final Integer price = Integer.parseInt(optional_journal.get());
            CompletableFuture<Map<String, String>> user_journal_history = user_journal_history_dict.findByMainKey(journalId);
            CompletableFuture<Integer> res_integer = user_journal_history.thenApply(map ->
            {
                Integer sum=0;
                for (Map.Entry<String, String> entry : map.entrySet())
                {
                    String journal = entry.getKey();
                    String history = entry.getValue();
                    if(history.charAt(history.length()-1) == '1')
                    {
                        sum+=price;
                    }
                }
                return sum;
            });

            return res_integer.thenApply(OptionalInt::of);
        });
    }

    @Override
    public CompletableFuture<Map<String, List<Boolean>>> getSubscribers(String journalId) {
        CompletableFuture<Map<String, String>> user_journal_history = user_journal_history_dict.findBySecondaryKey(journalId);
        return user_journal_history.thenApply(map ->
        {
            Map<String, List<Boolean>> res_map = new TreeMap<>();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                String user = entry.getKey();
                String history = entry.getValue();
                res_map.put(user,convert_history_from_string_to_bool_list(history));
            }
            return res_map;
        });
    }


    /*******PRIVATE*******/
    private List<Boolean> convert_history_from_string_to_bool_list(String history)
    {
        List<Boolean> res_list = new ArrayList<>();
        for(int i=0;i<history.length();i++)
        {
            if(history.charAt(i) == '1')
                res_list.add(Boolean.TRUE);
            else
                res_list.add(Boolean.FALSE);
        }

        return res_list;
    }

    private CompletableFuture<Optional<Boolean>> userHistoryContains(String userId, String journalId,String val) {
        CompletableFuture<Optional<String>> user = users_dict.find("userId");

        return user.thenCompose(optional_user ->
        {
            if(!optional_user.isPresent())
            {
                return CompletableFuture.completedFuture(Optional.empty());
            }

            CompletableFuture<Optional<String>> history = user_journal_history_dict.findByKeys(userId, journalId);

            return history.thenApply(optional_history -> {
                if(!optional_history.isPresent())
                {
                    return Optional.of(Boolean.FALSE);
                }
                else{
                    String hist_val = optional_history.get();
                    return Optional.of(hist_val.contains(val));
                }
            });
        });
    }

}
