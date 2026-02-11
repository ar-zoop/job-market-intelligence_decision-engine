package com.jobintelligence.service;

import com.jobintelligence.model.JobMatchResult;
import com.jobintelligence.model.JobProfile;
import com.jobintelligence.model.ResumeProfile;

import java.util.ArrayList;
import java.util.List;

public class GapExplanationService {
    public GapExplanationService(){

    }

    private List<String> calculateSkillMatchList(List<String> candidateSkills, List<String> jobSkills) {
        List<String> matchedSkills = new ArrayList<>();
        for (String skill : jobSkills) {
            if (candidateSkills.contains(skill)) {
                matchedSkills.add(skill);
            }
        }
        return matchedSkills;
    }

    private List<String> calculateSkillMissList(List<String> candidateSkills, List<String> jobSkills) {
        List<String> missingSkills = new ArrayList<>();
        for (String skill : jobSkills) {
            if (!candidateSkills.contains(skill)) {
                missingSkills.add(skill);
            }
        }
        return missingSkills;
    }

    private String roleAlignmentExplanation(ResumeProfile resume, JobProfile job) {
        if (resume.primaryRole.equals(job.roleType)) {
            return "Exact match. Job role : " + job.roleType + " and Resume role : " + resume.primaryRole;
        } else {
            return "Did not match. Job role : " + job.roleType + " and Resume role : " + resume.primaryRole;
        }
    }

    private String experienceMatchExplanation(ResumeProfile resume, JobProfile job) {
        if (job.yearsOfExperience <= 0) {
            return "Job does not specify required experience.";
        }
        int candidate = resume.yearsOfExperience;
        int required = job.yearsOfExperience;
        if (candidate >= required) {
            return String.format("Experience met. You have %d years; job requires %d.", candidate, required);
        }
        int shortfall = required - candidate;
        return String.format("Experience gap. You have %d years; job requires %d (%d year(s) short).", candidate, required, shortfall);
    }

    public void printExplanation(JobMatchResult jobMatchResult) {
        System.out.println("job id : " + jobMatchResult.getJobId());
        System.out.println("fit score : " + jobMatchResult.getFitScore());
        System.out.println("recommendation : " + jobMatchResult.getRecommendation());
        System.out.println("Role type alignment : " + jobMatchResult.getRoleAlignmentExplanation());
        System.out.println("Experience : " + jobMatchResult.getExperienceMatchExplanation());
        System.out.println("matched required skills : " + jobMatchResult.getMatchedRequiredSkills());
        System.out.println("matched nice to have skills : " + jobMatchResult.getMatchedNiceToHaveSkills());
        System.out.println("missing required skills : " + jobMatchResult.getMissingRequiredSkills());
        System.out.println("missing nice to have skills : " + jobMatchResult.getMissingNiceToHaveSkills());

    }

    public void enrichJobMatchResult(JobMatchResult jobMatchResult, ResumeProfile resume, JobProfile job) {
        jobMatchResult.setJobId(job.jobId);
        jobMatchResult.setMatchedRequiredSkills(calculateSkillMatchList(resume.skills, job.requiredSkills));
        jobMatchResult.setMatchedNiceToHaveSkills(calculateSkillMatchList(resume.skills, job.niceToHaveSkills));
        jobMatchResult.setMissingRequiredSkills(calculateSkillMissList(resume.skills, job.requiredSkills));
        jobMatchResult.setMissingNiceToHaveSkills(calculateSkillMissList(resume.skills, job.niceToHaveSkills));
        jobMatchResult.setRoleAlignmentExplanation(roleAlignmentExplanation(resume, job));
        jobMatchResult.setExperienceMatchExplanation(experienceMatchExplanation(resume, job));
    }
}
