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
        double fitScore = 0;
      
        fitScore = (requiredMatchRatio * 60) + (optionalMatchRatio * 20) + (roleAlignment * 20);
        return fitScore * maxScore / 100;
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

    double calculateRoleAlignment(String candidatesRole, String jobRole) {
        if(jobRole.equals(candidatesRole)) {
            return 1.0;
        } else {
            return 0.6;
        }
    }

    double calculateSkillMatchRatio(List<String> candidatesSkills, List<String> jobSkills) {
        if(jobSkills.size() == 0) {
            return 0.0;
        }
        int skillMatch = 0;
        for (String skill : jobSkills) {
            if(candidatesSkills.contains(skill)) {
                skillMatch++;
            }
        }
        return (double) skillMatch / jobSkills.size();
    }
}
