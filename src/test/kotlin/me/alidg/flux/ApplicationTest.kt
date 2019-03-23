package me.alidg.flux

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
class ApplicationTest {

    @Autowired private lateinit var client: WebTestClient

    @Test
    fun `Any request to fail endpoint would fail with 500 error`() {
        client.get().uri("/fail").exchange()
                .expectStatus().is5xxServerError
                .expectBody()
                    .jsonPath("$.path").isEqualTo("/fail")
                    .jsonPath("$.message").isEqualTo("Failed!")
                    .jsonPath("$.status").isEqualTo(500)
                    .jsonPath("$.error").isEqualTo("Internal Server Error")
                    .jsonPath("$.timestamp").exists()
    }
}