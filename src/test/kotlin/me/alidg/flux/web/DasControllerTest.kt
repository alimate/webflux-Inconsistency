package me.alidg.flux.web

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest(DasController::class)
class DasControllerTest {

    @Autowired private lateinit var client: WebTestClient

    @Test
    fun `Any request to hello endpoint should work as expected`() {
        client.get().uri("/hello?name=Ali").exchange()
                .expectStatus().isOk
    }

    @Test
    fun `Quit opposite to the real world, any request to fail endpoint returns 400 Bad Request`() {
        client.get().uri("/fail").exchange()
                .expectStatus().isBadRequest
                .expectBody().isEmpty
    }
}