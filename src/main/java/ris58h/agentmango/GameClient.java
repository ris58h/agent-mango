package ris58h.agentmango;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class GameClient {
    private final HttpClient gameHttpClient;

    public GameClient(
            @Client("https://bk-vill.pokatut.ru/api") HttpClient gameHttpClient
    ) {
        this.gameHttpClient = gameHttpClient;
    }

    public Mono<GameCode> fetchCode(int time) {
        String codeRequest = codeRequest(time);
        HttpRequest<?> httpRequest = HttpRequest.POST("/game_code", codeRequest)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer oMyvGtYe5wmCOk5AXJvPc10dwspFugwWnJ9oxW0T");
        return Mono.from(gameHttpClient.retrieve(httpRequest, Argument.of(GameCode.class), Argument.of(String.class)));
    }

    private String codeRequest(int time) {
        int min = time / 60;
        int sec = time % 60;
        return String.format("{\"time\": \"%02d:%02d\"}", min, sec);
    }
}
