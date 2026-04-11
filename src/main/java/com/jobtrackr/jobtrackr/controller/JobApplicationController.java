package com.jobtrackr.jobtrackr.controller;

import com.jobtrackr.jobtrackr.dto.JobApplicationRequest;
import com.jobtrackr.jobtrackr.model.JobApplication;
import com.jobtrackr.jobtrackr.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    // ADD a job
    @PostMapping
    public ResponseEntity<?> addJob(@RequestBody JobApplicationRequest request) {
        try {
            JobApplication job = jobApplicationService.addJob(request);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET all jobs for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApplication>> getAllJobs(@PathVariable Long userId) {
        return ResponseEntity.ok(jobApplicationService.getAllJobs(userId));
    }

    // GET one job
    @GetMapping("/{id}")
    public ResponseEntity<?> getJob(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobApplicationService.getJobById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // UPDATE a job
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id,
                                       @RequestBody JobApplicationRequest request) {
        try {
            JobApplication updated = jobApplicationService.updateJob(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE a job
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobApplicationService.deleteJob(id);
            return ResponseEntity.ok("Job application deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
