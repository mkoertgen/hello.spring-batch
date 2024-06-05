package com.example.demo.argo

import com.example.demo.JobService
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("argo")
class ArgoJobService : JobService {
    override fun run(job: Job, params: JobParameters) {
        TODO("Not yet implemented")
    }
}
