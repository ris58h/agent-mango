package ris58h.agentmango;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@Controller("/promocode")
public class PromoCodeController {
    private final GameClient gameClient;

    public PromoCodeController(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Get(uri = "/500")
    public Mono<HttpResponse<String>> promoCode500() {
        return promoCodeResponse(Value.VALUE_500);
    }

    @Get(uri = "/300")
    public Mono<HttpResponse<String>> promoCode300() {
        return promoCodeResponse(Value.VALUE_300);
    }

    @Get(uri = "/200")
    public Mono<HttpResponse<String>> promoCode200() {
        return promoCodeResponse(Value.VALUE_200);
    }

    @Error(global = true)
    HttpResponse<String> error(Throwable e) {
        return HttpResponse.serverError()
                .contentType(MediaType.TEXT_PLAIN)
                .body(e.getMessage());
    }

    private Mono<HttpResponse<String>> promoCodeResponse(Value codeValue) {
        return promoCode(codeValue)
                .map(promoCode -> HttpResponse.ok(promoCode)
                        .contentType(MediaType.TEXT_PLAIN)
                        .header(HttpHeaders.CACHE_CONTROL, "no-store"));
    }

    private Mono<String> promoCode(Value codeValue) {
        int time = time(codeValue);
        return gameClient.fetchCode(time)
                .map(code -> code.promo_code);
    }

    private enum Value {
        VALUE_500, VALUE_300, VALUE_200
    }

    private int time(Value value) {
        switch (value) {
            case VALUE_200: return randomInt(52, 59);
            case VALUE_300: return randomInt(45, 50);
            default: return randomInt(36, 40);
        }
    }

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
