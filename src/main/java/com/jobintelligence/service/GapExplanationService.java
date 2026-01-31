package com.jobintelligence.service;

import com.jobintelligence.model.JobMatchResult;
import com.jobintelligence.model.JobProfile;
import com.jobintelligence.model.ResumeProfile;

import java.util.ArrayList;
import java.util.List;

public class GapExplanationService {
    public GapExplanationService(){

    }

    private List<String> calculateSkillMatchList(List<String> candidatesSkills, List<String> jobSkills) {
        List<String> skillMatch = new ArrayList<>();
        for (String skill : jobSkills) {
            if(candidatesSkills.contains(skill)) {
                skillMatch.add(skill);
            }
        }
        return skillMatch;
    }

    private List<String> calculateSkillMissList(List<String> candidatesSkills, List<String> jobSkills) {
        List<String> skillMiss = new ArrayList<>();
        for (String skill : jobSkills) {
            if(!candidatesSkills.contains(skill)) {
                skillMiss.add(skill);
            }
        }
        return skillMiss;
    }

    private String roleAlignmentExplanation(ResumeProfile resume, JobProfile job) {
        if(resume.primaryRole.equals(job.roleType)) {
            return "Exact match. Job role : " + job.roleType + " and Resume role : " + resume.primaryRole;
        } else {
            return "Did not match. Job role : " + job.roleType + " and Resume role : " + resume.primaryRole;
        }
    }

    public void printExplanation(JobMatchResult jobMatchResult) {
        System.out.println("job id : " + jobMatchResult.getJobId());
        System.out.println("fit score : " + jobMatchResult.getFitScore());
        System.out.println("recommendation : " + jobMatchResult.getRecommendation());
        System.out.println("Role type alignment : " + jobMatchResult.getRoleAlignmentExplanation());
        System.out.println("matched required skills : " + jobMatchResult.getMatchedRequiredSkills());
        System.out.println("matched nice to have skills : " + jobMatchResult.getMatchedNiceToHaveSkills());
        System.out.println("missing required skills : " + jobMatchResult.getMissingNiceToHaveSkills());
        System.out.println("missing nice to have skills : " + jobMatchResult.getMissingRequiredSkills());

    }

    public void enrichJobMatchResult(JobMatchResult jobMatchResult, ResumeProfile resume, JobProfile job) {
        jobMatchResult.matchedRequiredSkills = calculateSkillMatchList(resume.skills, job.requiredSkills);
        jobMatchResult.matchedNiceToHaveSkills = calculateSkillMatchList(resume.skills, job.niceToHaveSkills);
        jobMatchResult.missingRequiredSkills = calculateSkillMissList(resume.skills, job.requiredSkills);
        jobMatchResult.missingNiceToHaveSkills = calculateSkillMissList(resume.skills, job.niceToHaveSkills);
        jobMatchResult.roleAlignmentExplanation = roleAlignmentExplanation(resume, job);
    }
}
