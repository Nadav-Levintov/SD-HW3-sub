package library;



import il.ac.technion.cs.sd.sub.ext.FutureLineStorage;
import java.util.concurrent.CompletableFuture;

class FutureLineStorageWrapperImpl implements FutureLineStorageWrapper {

    private FutureLineStorage ls;

    FutureLineStorageWrapperImpl(FutureLineStorage ls) {
        this.ls = ls;
    }

    @Override
    public CompletableFuture<Void> appendLine(String s) {
        return this.ls.appendLine(s).thenAccept(r->{
            if(r.equals(false)){
                this.appendLine(s);
            }
        });
    }

    @Override
    public CompletableFuture<String> read(int lineNumber){
        return this.ls.read(lineNumber).thenCompose(r->
                r.map(CompletableFuture::completedFuture).orElseGet(() -> this.read(lineNumber))
        );
    }

    @Override
    public CompletableFuture<Integer> numberOfLines(){
        return this.ls.numberOfLines().thenCompose(r->
                r.isPresent()? CompletableFuture.completedFuture(r.getAsInt()): this.numberOfLines());
    }
}
