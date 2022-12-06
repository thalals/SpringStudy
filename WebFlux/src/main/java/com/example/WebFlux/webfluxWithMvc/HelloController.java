package com.example.WebFlux.webfluxWithMvc;

import com.example.WebFlux.router.handler.ExampleHandler;
import com.example.WebFlux.webfluxWithMvc.request.UserRequest;
import com.example.WebFlux.webfluxWithMvc.response.MessageResponse;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mvc")
public class HelloController {

    @GetMapping("/hello")
    public Mono<String> getHello(){
        return Mono.just("hello webflux based @mvc");
    }

    @GetMapping("/flux/hello")
    public Flux<String> getManyHello(){
        return Flux.just(
                "내가 만약 ",
                "외로울때면 ",
                "누가 나를 위로해주나 ",
                "바로 여러분..."
        );
    }

    @GetMapping("/flux/message")
    public Flux<MessageResponse> getMessageFlux(){
        return Flux.just(
                new MessageResponse(1,"첫번째 메세지"),
                new MessageResponse(2, "두번째 메세지")
        );
    }

    /**
     * Mono, Flux 는 Stream을 정의하기 때문에 클라이언트로부터 Stream을 받을 수 있음
     */
    @PostMapping("/stream")
    public Flux<String> receivedStream(@RequestBody Flux<UserRequest> userRequestFlux){
        return userRequestFlux.map(u -> "Hello " + u.getName() + "\n");
    }

}
