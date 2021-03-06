package library;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import il.ac.technion.cs.sd.sub.ext.FutureLineStorage;
import il.ac.technion.cs.sd.sub.ext.FutureLineStorageFactory;

public class TestStorerFactory implements FutureLineStorageFactory {

	Map<String, TestStorer> store = new HashMap<>();

	@Override
	public CompletableFuture<Optional<FutureLineStorage>> open(String name) {
		if (!store.containsKey(name))
			store.put(name, new TestStorer());

		return CompletableFuture.runAsync(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(store.size()*100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).thenApply(v -> Optional.of(store.get(name)));
	}

}
