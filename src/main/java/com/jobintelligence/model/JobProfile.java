package com.jobintelligence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobProfile {

    @JsonProperty("job_id")
    public String jobId;
    @JsonProperty("role_type")
    public String roleType;
    @JsonProperty("required_skills")
    public List<String> requiredSkills;
    @JsonProperty("nice_to_have_skills")
    public List<String> niceToHaveSkills;
    @JsonProperty("required_experience_years")
    public int yearsOfExperience;
}
