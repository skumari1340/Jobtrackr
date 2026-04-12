package com.jobtrackr.jobtrackr.controller;

import com.jobtrackr.jobtrackr.dto.JobApplicationRequest;
import com.jobtrackr.jobtrackr.model.JobApplication;
import com.jobtrackr.jobtrackr.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@Tag(name = "Job Applications", description = "APIs for managing job applications. All endpoints require JWT token.")
@SecurityRequirement(name = "Bearer Authentication")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Operation(
        summary = "Add a new job application",
        description = "Creates a new job application for a user. Requires company, role, status and userId."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job application created successfully"),
        @ApiResponse(responseCode = "400", description = "User not found or invalid data"),
        @ApiResponse(responseCode = "403", description = "JWT token missing or invalid")
    })
    @PostMapping
    public ResponseEntity<?> addJob(@RequestBody JobApplicationRequest request) {
        try {
            JobApplication job = jobApplicationService.addJob(request);
            return ResponseEntity.ok(job);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Get all job applications for a user",
        description = "Returns a list of all job applications belonging to the specified user."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of job applications returned"),
        @ApiResponse(responseCode = "403", description = "JWT token missing or invalid")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobApplication>> getAllJobs(
        @Parameter(description = "ID of the user whose jobs to retrieve") @PathVariable Long userId) {
        return ResponseEntity.ok(jobApplicationService.getAllJobs(userId));
    }

    @Operation(
        summary = "Get a specific job application",
        description = "Returns a single job application by its ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job application found and returned"),
        @ApiResponse(responseCode = "400", description = "Job application not found"),
        @ApiResponse(responseCode = "403", description = "JWT token missing or invalid")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getJob(
        @Parameter(description = "ID of the job application") @PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobApplicationService.getJobById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Update a job application",
        description = "Updates an existing job application. You can change company, role, status, date and notes."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job application updated successfully"),
        @ApiResponse(responseCode = "400", description = "Job application not found"),
        @ApiResponse(responseCode = "403", description = "JWT token missing or invalid")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(
        @Parameter(description = "ID of the job application to update") @PathVariable Long id,
        @RequestBody JobApplicationRequest request) {
        try {
            JobApplication updated = jobApplicationService.updateJob(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
        summary = "Delete a job application",
        description = "Permanently deletes a job application by its ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Job application deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Job application not found"),
        @ApiResponse(responseCode = "403", description = "JWT token missing or invalid")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(
        @Parameter(description = "ID of the job application to delete") @PathVariable Long id) {
        try {
            jobApplicationService.deleteJob(id);
            return ResponseEntity.ok("Job application deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
