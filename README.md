# challenge

# To access in memory h2 database
http://localhost:8080/h2-console


# Post end point: To run the a job which dumps data from employees.csv into h2 database.Will start the job. and will return it's id, status, name
http://localhost:8080/challenge/import-employees




# Get end point: To get a job status by job id. It displays string representation of the message(s).e.g) Job has completed successfully.
http://localhost:8080/challenge/job-status/1


# Post end Point: To Run a Job by path variable of {jobName}.
http://localhost:8080/challenge/run-job/dump-employee-details


# Post end Point: To Run multiple Jobs.
http://localhost:8080/challenge/run-multiple-jobs




