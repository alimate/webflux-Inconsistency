package me.alidg.flux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FluxExceptionHandlerApplication

fun main(args: Array<String>) {
    runApplication<FluxExceptionHandlerApplication>(*args)
}
