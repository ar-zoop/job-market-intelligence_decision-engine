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

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setFitScore(double fitScore) {
        this.fitScore = fitScore;
    }

    public void setRecommendation(DecisionType recommendation) {
        this.recommendation = recommendation;
    }

    public void setMissingRequiredSkills(List<String> missingRequiredSkills) {
        this.missingRequiredSkills = missingRequiredSkills;
    }

    public void setMissingNiceToHaveSkills(List<String> missingNiceToHaveSkills) {
        this.missingNiceToHaveSkills = missingNiceToHaveSkills;
    }

    public void setMatchedRequiredSkills(List<String> matchedRequiredSkills) {
        this.matchedRequiredSkills = matchedRequiredSkills;
    }

    public void setMatchedNiceToHaveSkills(List<String> matchedNiceToHaveSkills) {
        this.matchedNiceToHaveSkills = matchedNiceToHaveSkills;
    }

    public void setRoleAlignmentExplanation(String roleAlignmentExplanation) {
        this.roleAlignmentExplanation = roleAlignmentExplanation;
    }

    public String getRoleAlignmentExplanation() {
        return roleAlignmentExplanation;
    }
}

