package il.ac.technion.cs.sd.sub.test;

import il.ac.technion.cs.sd.sub.ext.FutureLineStorage;
import il.ac.technion.cs.sd.sub.ext.FutureLineStorageFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).thenApply(v -> Optional.of(store.get(name)));
	}

}