package com.mainteny.challenge.controllers;


import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("challenge")
public class TestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job extractEmployeeDataJob;

    @Autowired
    private Job exportUserJob;

    @Autowired
    private JobExplorer jobExplorer;

    @PostMapping("/import-employees")
    public String importEmployees() throws Exception {
        JobExecution jobExecution = jobLauncher.run(extractEmployeeDataJob, new JobParameters());
        return "{status: " + jobExecution.getStatus() +
                ", jobId: "+jobExecution.getJobId()+
                ", jobName:"+jobExecution.getJobInstance().getJobName()+
                "}";
    }

    @GetMapping("/job-status/{jobId}")
    public String getJobStatus(@PathVariable("jobId") Long jobId) {
        JobExecution jobExecution = jobExplorer.getJobExecution(jobId);
        if (jobExecution != null) {
            BatchStatus status = jobExecution.getStatus();
            if (status.equals(BatchStatus.STARTED)) {
                return "Job is still running.";
            } else if (status.equals(BatchStatus.COMPLETED)) {
                return "Job has completed successfully.";
            } else if (status.equals(BatchStatus.STOPPING)) {
                return "Job is stopping.";
            } else if (status.equals(BatchStatus.STOPPED)) {
                return "Job has stopped.";
            } else if (status.equals(BatchStatus.FAILED)) {
                return "Job has failed.";
            } else {
                return "Unknown job status.";
            }
        } else {
            return "Job not found.";
        }
    }

    @PostMapping("/run-job/{jobName}")
    public String runJob(@PathVariable("jobName") String jobName) throws Exception {
        Job job = getJobByName(jobName);
        if (job != null) {
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
            return "Batch job status: " + jobExecution.getStatus();
        } else {
            return "Job not found.";
        }
    }

    @PostMapping("/run-multiple-jobs")
    public String runMultipleJobs() throws Exception {
        JobExecution extractEmployeeJobExecution = jobLauncher.run(extractEmployeeDataJob, new JobParameters());
        JobExecution exportJobExecution = jobLauncher.run(exportUserJob, new JobParameters());

        return "Import Job status: " + extractEmployeeJobExecution.getStatus() +
                ", Export Job status: " + exportJobExecution.getStatus();
    }

    private Job getJobByName(String jobName) {
        switch (jobName) {
            case "dump-employee-details":
                return extractEmployeeDataJob;
            case "exportUserJob":
                return exportUserJob;
            // Add other job cases as needed
            default:
                return null;
        }
    }


}
