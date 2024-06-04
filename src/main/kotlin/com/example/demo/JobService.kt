package com.example.demo

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

interface JobService {
    fun run(job: Job, params: JobParameters)
}

@Service
@Profile("dev,!argo,!k8s")
class TaskJobService(private val launcher: JobLauncher) : JobService {
    override fun run(job: Job, params: JobParameters) {
        launcher.run(job, params)
    }
}
