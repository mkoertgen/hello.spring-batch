package com.example.demo.k8s

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties("k8s")
class K8sProperties {
    lateinit var namespace: String
    lateinit var jobName: String
}
