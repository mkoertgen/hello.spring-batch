package com.example.demo

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/jobs")
class JobsController(
    private val jobs: List<Job>,
    private val launcher: JobService,
) {
    @GetMapping
    fun getJobs(): List<Job> = jobs

    @GetMapping("{name}")
    fun getJob(@PathVariable name: String) = jobs.find { it.name == name }

    @PostMapping("_start")
    fun startJob(name: String, params: JobParameters) {
        // https://docs.spring.io/spring-batch/reference/job/running.html#runningJobsFromWebContainer
		val job = jobs.find { it.name == name } ?: throw IllegalArgumentException("Job not found")
        launcher.run(job, params)
    }
}