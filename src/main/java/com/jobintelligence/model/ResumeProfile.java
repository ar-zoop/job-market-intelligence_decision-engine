package com.jobintelligence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeProfile {
    @JsonProperty("skills")
    public List<String> skills;

    @JsonProperty("role_type")
    public String primaryRole;
    /** Total years of professional experience (e.g. from resume). Default 0 if not provided. */
    @JsonProperty("experience_years")
    public int yearsOfExperience = 0;
}
