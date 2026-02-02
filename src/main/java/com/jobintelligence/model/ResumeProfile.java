package com.jobintelligence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeProfile {
    public List<String> skills;
    public String primaryRole;
    /** Total years of professional experience (e.g. from resume). Default 0 if not provided. */
    public int yearsOfExperience = 0;
}
