package com.jobintelligence.model;

import java.util.List;

public class JobMatchResult {
    public String jobId;
    public double fitScore;
    public DecisionType recommendation;
    public List<String> missingRequiredSkills;
    public List<String> missingNiceToHaveSkills;
    public List<String> matchedRequiredSkills;
    public List<String> matchedNiceToHaveSkills;
    public String roleAlignmentExplanation;

    public JobMatchResult(double fitScore, DecisionType recommendation) {
        this.fitScore = fitScore;
        this.recommendation = recommendation;
    }

    public String getJobId() {
        return jobId;
    }

    public double getFitScore() {
        return fitScore;
    }

    public DecisionType getRecommendation() {
        return recommendation;
    }

    public List<String> getMissingRequiredSkills() {
        return missingRequiredSkills;
    }

    public List<String> getMissingNiceToHaveSkills() {
        return missingNiceToHaveSkills;
    }

    public List<String> getMatchedRequiredSkills() {
        return matchedRequiredSkills;
    }

    public List<String> getMatchedNiceToHaveSkills() {
        return matchedNiceToHaveSkills;
    }

    public String getRoleAlignmentExplanation() {
        return roleAlignmentExplanation;
    }
}

