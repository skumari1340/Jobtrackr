package com.jobtrackr.jobtrackr.service;

import com.jobtrackr.jobtrackr.dto.JobApplicationRequest;
import com.jobtrackr.jobtrackr.model.JobApplication;
import com.jobtrackr.jobtrackr.model.User;
import com.jobtrackr.jobtrackr.repository.JobApplicationRepository;
import com.jobtrackr.jobtrackr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    // ADD a new job application
    public JobApplication addJob(JobApplicationRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        JobApplication job = new JobApplication();
        job.setCompany(request.getCompany());
        job.setRole(request.getRole());
        job.setStatus(request.getStatus());
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());
        job.setUser(user);

        return jobApplicationRepository.save(job);
    }

    // GET all jobs for a user
    public List<JobApplication> getAllJobs(Long userId) {
        return jobApplicationRepository.findByUserId(userId);
    }

    // GET one job by id
    public JobApplication getJobById(Long id) {
        return jobApplicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Job application not found!"));
    }

    // UPDATE a job
    public JobApplication updateJob(Long id, JobApplicationRequest request) {
        JobApplication job = getJobById(id);
        job.setCompany(request.getCompany());
        job.setRole(request.getRole());
        job.setStatus(request.getStatus());
        job.setAppliedDate(request.getAppliedDate());
        job.setNotes(request.getNotes());
        return jobApplicationRepository.save(job);
    }

    // DELETE a job
    public void deleteJob(Long id) {
        JobApplication job = getJobById(id);
        jobApplicationRepository.delete(job);
    }
}
