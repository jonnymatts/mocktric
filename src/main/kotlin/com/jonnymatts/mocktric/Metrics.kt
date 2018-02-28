package com.jonnymatts.mocktric

data class MetricDefinition(val name: String, val labels: Map<String, String>?)
data class Metric(val name: String?, val labels: Map<String, String>?, val value: Long?)