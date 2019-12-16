package de.uni.ds.rx;

import java.util.function.Consumer;

import org.reactivestreams.Subscriber;

import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;

public class App {

    public final GUI gui;

    private Subscriber<String> subscriber;
    private Disposable streamHandle;

    public App() {
        this.gui = new GUI("Initial Text", (c) -> this.subscriber.onNext(c));
        createFlux((str) -> this.gui.updateText(str));
    }

    public Subscriber<String> createFlux(Consumer<String> callback) {
        EmitterProcessor<String> processor = EmitterProcessor.create();
        this.subscriber = processor;
        this.streamHandle = processor
            .transform(Mapper::map)
            .doOnNext(callback)
            .subscribe();
        return processor;
    }

    public static void main(String[] args) {
        App app = new App();
    }
}
