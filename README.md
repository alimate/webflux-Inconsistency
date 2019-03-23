# Demo Reactive Project
This project is created to demonstrate an inconsistent behavior between `@WebFluxTest`s and what happens in the real world, 
when we manually run a Spring Boot application.

## Steps to Reproduce
As you can spot from the project files, we registered a bean of type 
[WebExceptionHandler](/src/main/kotlin/me/alidg/flux/web/CustomExceptionHandler.kt). This handler catches every exception and eats them
just by returning a `400 Bad Request`, that's what error handlers supposed to do :)

### Manual Run
If we run the Spring Boot application by:
```bash
./mvnw clean spring-boot:run
```
and send a request to the `http://localhost:8080/fail` endpoint, which btw throws a `RuntimeException` every time, then
we get the following JSON:
```json
{
  "timestamp": "2019-03-23T16:10:23.248+0000",
  "path": "/fail",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Failed!"
}
```
Apparently, the registered error handler didn't get a chance to handle the exception, as we've got a `500 Internal Sever Error`,
no the expected `400 Bad Request`.

### WebFluxTest
But if we write a test like the following, as I did [here](/src/test/kotlin/me/alidg/flux/web/DasControllerTest.kt):
```kotlin
@WebFluxTest(DasController::class)
class DasControllerTest {

    @Autowired private lateinit var client: WebTestClient

    @Test
    fun `Quit opposite to the real world, any request to fail endpoint returns 400 Bad Request`() {
        client.get().uri("/fail").exchange()
                .expectStatus().isBadRequest
                .expectBody().isEmpty
    }
}
```
Here the registered error handler caught the exception and returned the expected `400 Bad Request`. This behavior is even
inconsistent with more general `@SpringBootTest`s:
```kotlin
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
```
As you can see, the same request generates different results in `@SpringBootTest`s and `@WebFluxTest`s.
## A Quick Note
If we register the handler bean of type `ErrorWebExceptionHandler` and not the current `WebExceptionHandler`, everything
would work just fine.

## Suggestion
I guess we're better off ignoring the registered `WebExceptionHandler`s in test environments to align application behaviors
in different stages and avoid the possible surprises.

## Acknowledgements
Hat tip to all Spring contributors, you've done and still doing a great job.