package com.jobintelligence.service;

import java.util.List;

import com.jobintelligence.model.*;

public class JobScorer {

    public JobMatchResult score(ResumeProfile resume, JobProfile job) {
        double maxScore = 100.0;
        if (job.niceToHaveSkills.size() == 0) {
            maxScore = 80.0;
        }

        double fitScore = jobFitScoreCalculator(resume, job, maxScore);
        DecisionType decisionLabel = calculateDecisionLabel(fitScore);
        JobMatchResult jobMatchResult = new JobMatchResult(fitScore, decisionLabel);
        return jobMatchResult;
    }

    private double jobFitScoreCalculator(ResumeProfile resume, JobProfile job, double maxScore) {
        double requiredMatchRatio = calculateSkillMatchRatio(resume.skills, job.requiredSkills);
        double optionalMatchRatio = calculateSkillMatchRatio(resume.skills, job.niceToHaveSkills);
        double roleAlignment = calculateRoleAlignment(resume.primaryRole, job.roleType);
        double experienceAlignment = calculateExperienceAlignment(resume.yearsOfExperience, job.yearsOfExperience);

        // Weights: required skills 55%, optional 15%, role 15%, experience 15%
        double fitScore = (requiredMatchRatio * 55) + (optionalMatchRatio * 15) + (roleAlignment * 15) + (experienceAlignment * 15);
        return fitScore * maxScore / 100;
    }

    /**
     * Experience alignment: 1.0 if candidate meets or exceeds required years;
     * partial credit if within 1–2 years; low score if far short.
     * If job does not specify required years (0), treat as no requirement → full score.
     */
    double calculateExperienceAlignment(int candidateYears, int requiredYears) {
        if (requiredYears <= 0) {
            return 1.0;
        }
        if (candidateYears >= requiredYears) {
            return 1.0;
        }
        int shortfall = requiredYears - candidateYears;
        if (shortfall == 1) {
            return 0.75;
        }
        if (shortfall == 2) {
            return 0.5;
        }
        return 0.25; // 3+ years short
    }

    DecisionType calculateDecisionLabel(double fitScore) {
        if (fitScore >= 70) {
            return DecisionType.APPLY_NOW;
        } else if (fitScore >= 50) {
            return DecisionType.PREPARE_THEN_APPLY;
        } else {
            return DecisionType.SKIP;
        }
    }

    double calculateRoleAlignment(String candidateRole, String jobRole) {
        if (jobRole.equals(candidateRole)) {
            return 1.0;
        }
        return 0.6;
    }

    double calculateSkillMatchRatio(List<String> candidateSkills, List<String> jobSkills) {
        if (jobSkills.isEmpty()) {
            return 0.0;
        }
        int matchedSkillCount = 0;
        for (String skill : jobSkills) {
            if (candidateSkills.contains(skill)) {
                matchedSkillCount++;
            }
        }
        return (double) matchedSkillCount / jobSkills.size();
    }
}
