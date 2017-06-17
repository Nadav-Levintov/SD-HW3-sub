package library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * The provided implementation of {@link Dict}, using {@link FutureLineStorageWrapper}
 * 
 * @see {@link DictFactory} and {@link LibraryModule} for more info on how to
 *      create an instance
 */
public class DictImpl implements Dict {
	private final CompletableFuture<FutureLineStorageWrapper> storer;
	private final ListMultimap<String, String> pairs = ArrayListMultimap.create();
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

	static CompletableFuture<?> storeToStorage(ListMultimap<String, String> map, CompletableFuture<FutureLineStorageWrapper> store,
			CompletableFuture<?> current) {
		for (String key : map.keySet().stream().sorted().collect(Collectors.toList())) {
			List<String> curr_key_val_list = map.get(key);
			for (String val:curr_key_val_list) {
				current = current.thenCompose(v -> store.thenCompose(s -> s.appendLine(key)));
				current = current.thenCompose(v -> store.thenCompose(s -> s.appendLine(val)));
			}
		}
		return current;
	}

	@Override
	public void add(String key, String value) {
		pairs.put(key, value);
	}

	@Override
	public void addAll(Multimap<String, String> ps) {
		pairs.putAll(ps);
	}

	@Override
	public CompletableFuture<Optional<String>> find(String key) {
		return storingStatus
				.thenCompose(v -> BinarySearch.valueOf(storer, key, 0, storer.thenCompose(s -> s.numberOfLines())));
	}
}
