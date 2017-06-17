package il.ac.technion.cs.sd.sub.app;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Nadav on 17-Jun-17.
 */
public class SubscriberReaderImpl implements SubscriberReader {
    @Override
    public CompletableFuture<Optional<Boolean>> isSubscribed(String userId, String journalId) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Boolean>> wasSubscribed(String userId, String journalId) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Boolean>> isCanceled(String userId, String journalId) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<Boolean>> wasCanceled(String userId, String journalId) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> getSubscribedJournals(String userId) {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, List<Boolean>>> getAllSubscriptions(String userId) {
        return null;
    }

    @Override
    public CompletableFuture<OptionalInt> getMonthlyBudget(String userId) {
        return null;
    }

    @Override
    public CompletableFuture<List<String>> getSubscribedUsers(String journalId) {
        return null;
    }

    @Override
    public CompletableFuture<OptionalInt> getMonthlyIncome(String journalId) {
        return null;
    }

    @Override
    public CompletableFuture<Map<String, List<Boolean>>> getSubscribers(String journalId) {
        return null;
    }
}
