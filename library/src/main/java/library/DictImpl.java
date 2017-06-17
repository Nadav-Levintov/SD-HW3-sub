package library;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import il.ac.technion.cs.sd.sub.ext.FutureLineStorage;
import il.ac.technion.cs.sd.sub.ext.FutureLineStorageFactory;
import javafx.util.Pair;

/**
 * The provided implementation of {@link Dict}, using {@link FutureLineStorage}
 * 
 * @see {@link DictFactory} and {@link LibraryModule} for more info on how to
 *      create an instance
 */

public class DictImpl implements Dict {
	private final CompletableFuture<FutureLineStorage> storer;
	private final Map<String, String> pairs = new HashMap<>();
	private final FutureLineStorageFactory factory;
	private final String name;
	private CompletableFuture<?> storingStatus;

	private FutureLineStorage get_storer()
	{
		CompletableFuture<Optional<FutureLineStorage>> open_result = factory.open(name);
		Optional<FutureLineStorage> temp = Optional.empty();

		open_result = open_result.whenComplete((optional,v) ->
		{
			if(!optional.isPresent())
				return
		})

		return temp.get();
	}


	@Inject
	DictImpl(FutureLineStorageFactory factory, //
			@Assisted String name) {
		this.factory=factory;
		this.name=name;
		storingStatus = storer = CompletableFuture.completedFuture(get_storer());
	}

	@Override
	public CompletableFuture<Void> store() {
		return null;
	}

	@Override
	public void add(String key, String value) {

	}

	@Override
	public void addAll(Map<String, String> m) {

	}

	@Override
	public CompletableFuture<Optional<String>> find(String key) {
		return null;
	}
	/*
	public CompletableFuture<Void> store() {
		return (storingStatus = storeToStorage(pairs, storer, storer)).thenAccept(s -> {
		});
	}

	static CompletableFuture<?> storeToStorage(Map<String, String> map, CompletableFuture<Optional<FutureLineStorage>> store,
											   CompletableFuture<?> current) {
		for (String key : map.keySet().stream().sorted().collect(Collectors.toList())) {
			current = current.thenCompose(v -> store.thenCompose(s -> {
				FutureLineStorage optionalLineStorage = s.get().appendLine(key)
			} ));
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
				.thenCompose(v -> BinarySearch.valueOf(storer, key, 0, storer.thenCompose(s -> {
					CompletableFuture<OptionalInt> val = s.numberOfLines();

				})));
	}
	*/
}
