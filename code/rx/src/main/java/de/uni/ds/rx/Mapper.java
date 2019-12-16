package de.uni.ds.rx;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.reactivestreams.Publisher;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Mapper {

    public static Publisher<String> map(Publisher<String> input) 
    {
        return Flux.from(input)
                // .filter(str -> str.endsWith(".")) // update only if input ends with '.'
                // .sampleTimeout((str) -> Mono.just(str).delay(Duration.ofSeconds(1))) // update only after 1s of inactivity
                // .switchMap(str -> Mono.just("Loading...").concatWith(shoutService(str)))
                .switchMap(str -> Mono.just("Loading...").concatWith(wikipediaService(str)))
                ;
    }

    public static Publisher<String> shoutService(String value) {
        return Mono.just(value)
                .publishOn(Schedulers.elastic())
                .map(String::toUpperCase)
                .doOnNext((v) -> System.out.println(Thread.currentThread().getName()))
                .delayElement(Duration.ofSeconds(1))
                ;
    }

    public static Publisher<String> wikipediaService(String value) {
        return Mono.just(value)
                .filter(str -> !str.isEmpty())
                .publishOn(Schedulers.fromExecutor(httpExecutor))
                .doOnNext(s -> System.out.println(Thread.currentThread().getName()))
                .map(Mapper::fetchArticles)
                .publishOn(Schedulers.elastic())
                .doOnNext(s -> System.out.println(Thread.currentThread().getName()))
                .map(Mapper::formatLinkList)
                .onErrorReturn("There was an error.")
                ;
    }

    public static List<String> fetchArticles(String searchTerm) {
        HttpResponse<JsonNode> response = Unirest.get("https://en.wikipedia.org/w/api.php")
                .header("accept", "application/json")
                .queryString("action", "opensearch")
                .queryString("search", searchTerm)
                .queryString("limit", 5)
                .queryString("namespce", 0)
                .queryString("format", "json")
                .asJson();
        if (response.isSuccess()) {
            return response.getBody().getArray().getJSONArray(3).toList();
        } else {
            return Arrays.asList("Error when fetching: " + response.getStatusText());
        }
    }

    public static String formatLinkList(List<String> list) {
        return list.stream()
            .map(str -> "<li>" + str + "</li>")
            .reduce("<html><ul>", (v1, v2) -> v1 + v2) 
                + "</ul></html>";
    }

    private static final ExecutorService httpExecutor;
    static {
        Unirest.config().enableCookieManagement(false);
        httpExecutor = Executors.newFixedThreadPool(4, 
                new ThreadFactoryBuilder().setNameFormat("http-thread-%d").build()
        );
    }
}