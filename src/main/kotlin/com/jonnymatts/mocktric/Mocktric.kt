package com.jonnymatts.mocktric

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import mu.KLogger
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private val metrics: MutableMap<MetricDefinition, Long> = mutableMapOf()

fun Application.main() {

    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(StatusPages) {
        handleException<IllegalArgumentException>(logger, HttpStatusCode.BadRequest)
        handleException<Throwable>(logger, HttpStatusCode.InternalServerError)
    }
    install(Routing) {
        route("/metrics") {
            get {
                val metricsOutput = metrics.entries.joinToString("\n", postfix = "\n") {
                    (metric, value) -> format(Metric(metric.name, metric.labels, value))
                }
                call.respond(HttpStatusCode.OK, metricsOutput)
            }
            put {
                val metricInput = call.receive<Metric>()
                handleInput(metricInput)
                call.respond(HttpStatusCode.OK)
            }
            delete {
                metrics.clear()
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

private inline fun <reified T : Throwable> StatusPages.Configuration.handleException(logger: KLogger, statusCode: HttpStatusCode) {
    exception<T> { cause ->
        logger.error { cause }
        call.respond(statusCode, mapOf("error" to cause.message))
    }
}

private fun handleInput(metricInput: Metric) {
    val metricName = metricInput.name ?: throw IllegalArgumentException("Missing 'name' for metric - $metricInput")
    val metricValue = metricInput.value ?: throw IllegalArgumentException("Missing 'value' for metric - $metricInput")
    metrics[MetricDefinition(metricName, metricInput.labels)] = metricValue
}