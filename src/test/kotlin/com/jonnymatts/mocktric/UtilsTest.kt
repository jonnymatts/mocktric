package com.jonnymatts.mocktric

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

import assertk.assert
import assertk.assertions.*

object UtilsTest : Spek({

    on("format") {
        it("should format a metric correctly") {
            val got: String = format(Metric("metric", mapOf("label" to "value"), 200))

            assert(got).isEqualTo("metric{label=\"value\"} 200")
        }

        it("should format a metric with multiple labels correctly") {
            val got: String = format(Metric("metric", mapOf("label1" to "value1", "label2" to "value2"), 200))

            assert(got).isEqualTo("metric{label1=\"value1\",label2=\"value2\"} 200")
        }

        it("should format a metric with no labels correctly") {
            val got: String = format(Metric("metric", null, 200))

            assert(got).isEqualTo("metric 200")
        }
    }
})