package me.alidg.flux.web

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebExceptionHandler
import reactor.core.publisher.Mono

/**
 * When we run the spring application manually, this component will be ignored and the
 * default handler from [org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration]
 * will be used.
 *
 * But in [org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest]s this handler will be
 * used. This behavior is inconsistent with what happens in the read world.
 */
@Component
class CustomExceptionHandler : WebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        exchange.response.statusCode = HttpStatus.BAD_REQUEST
        return Mono.empty()
    }
}