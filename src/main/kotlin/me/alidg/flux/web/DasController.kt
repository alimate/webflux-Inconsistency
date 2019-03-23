package me.alidg.flux.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.lang.RuntimeException

@RestController
class DasController {

    @GetMapping("/hello")
    fun sayHello(@RequestParam to: String?) = Mono.just("Hello ${to ?: "Stranger"}")

    @GetMapping("/fail")
    fun failMiserably(): Mono<String> = Mono.error(RuntimeException("Failed!"))
}