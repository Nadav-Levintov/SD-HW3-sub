package library;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import il.ac.technion.cs.sd.buy.ext.FutureLineStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The provided implementation of {@link Dict}, using {@link FutureLineStorage}
 * 
 * @see {@link DictFactory} and {@link LibraryModule} for more info on how to
 *      create an instance
 */
public class DictImpl implements Dict {
	private final CompletableFuture<FutureLineStorageWrapper> storer;
	private final Map<String, String> pairs = new HashMap<>();
	private CompletableFuture<?> storingStatus;

	@Inject
	DictImpl(FutureLineStorageWrapperFactory factory, //
			@Assisted String name) {
		storingStatus = storer = factory.open(name);
	}

	public CompletableFuture<Void> store() {
		return (storingStatus = storeToStorage(pairs, storer, storer)).thenAccept(s -> {
		});
	}

	static CompletableFuture<?> storeToStorage(Map<String, String> map, CompletableFuture<FutureLineStorageWrapper> store,
			CompletableFuture<?> current) {
		for (String key : map.keySet().stream().sorted().collect(Collectors.toList())) {
			current = current.thenCompose(v -> store.thenCompose(s -> s.appendLine(key)));
			current = current.thenCompose(v -> store.thenCompose(s -> s.appendLine(map.get(key))));
		}
		return current;
	}

	@Override
	public void add(String key, String value) {
		pairs.put(key, value);
	}

	@Override
	public void addAll(Map<String, String> ps) {
		pairs.putAll(ps);
	}

	@Override
	public CompletableFuture<Optional<String>> find(String key) {
		return storingStatus
				.thenCompose(v -> BinarySearch.valueOf(storer, key, 0, storer.thenCompose(FutureLineStorageWrapper::numberOfLines)));
	}
}
