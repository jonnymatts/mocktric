package com.jonnymatts.mocktric

fun format(metric: Metric): String {
    return "${metric.name}${getMetricLabels(metric.labels)} ${metric.value}"
}

private fun getMetricLabels(labels: Map<String, String>?): String {
    if (labels == null) return ""
    return labels.entries.joinToString(",", prefix = "{", postfix = "}") { (name, value) -> "$name=\"$value\"" }
}