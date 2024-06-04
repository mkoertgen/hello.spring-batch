package com.example.demo.k8s

import com.example.demo.JobService
import io.kubernetes.client.openapi.apis.BatchV1Api
import io.kubernetes.client.util.Config
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("k8s")
class K8sJobService(private val props: K8sProperties) : JobService {
    override fun run(job: Job, params: JobParameters) {
        // https://github.com/kubernetes-client/java/wiki/3.-Code-Examples
        val apiClient = Config.defaultClient()
        val api = BatchV1Api(apiClient)
        val k8sJob = api.readNamespacedJob(props.jobName, props.namespace).execute()
        if (k8sJob.status.active != null) {
            throw IllegalStateException("Job is already running")
        }
        if (k8sJob.status.succeeded != null) {
            throw IllegalStateException("Job has already succeeded")
        }
    }
}
