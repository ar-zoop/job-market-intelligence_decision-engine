package com.jobintelligence.service;

import com.jobintelligence.model.DecisionType;
import com.jobintelligence.model.JobBucket;
import com.jobintelligence.model.JobMatchResult;
import com.jobintelligence.model.MissingSkillFrequency;

import java.util.*;

public class ActionPlanService {
    public void execute(List<JobMatchResult> jobMatchResultList) {
        JobBucket jobBucket = jobBucketing(jobMatchResultList);
        MissingSkillFrequency missingSkillFrequency = missingSkillFrequency(jobMatchResultList);
        List<String> topSkillsToLearn = rankTopSkillsToLearn(missingSkillFrequency, jobMatchResultList);
        Map<String, Integer> impactMap = detectHighImpactSkills(missingSkillFrequency, jobMatchResultList);
        List<String> topImpactSkills = getTopImpactSkills(impactMap);
        printActionPlan(jobBucket, topSkillsToLearn, topImpactSkills);
    }

    private JobBucket jobBucketing(List<JobMatchResult> jobMatchResultList) {
        List<String> applyNowList = new ArrayList<>();
        List<String> prepareThenApplyList = new ArrayList<>();
        List<String> skipList = new ArrayList<>();

        for (JobMatchResult result : jobMatchResultList){
            if (result.getRecommendation() == DecisionType.APPLY_NOW) {
                applyNowList.add(result.getJobId());
            } else if ( result.getRecommendation() == DecisionType.PREPARE_THEN_APPLY) {
                prepareThenApplyList.add(result.getJobId());
            } else {
                skipList.add(result.getJobId());
            }
        }

        JobBucket jobBucket = new JobBucket(applyNowList, prepareThenApplyList, skipList);
        return jobBucket;

    }

    private MissingSkillFrequency missingSkillFrequency(List<JobMatchResult> jobMatchResultList) {
        HashMap<String, Integer> missingRequiredSkillsFreq = new HashMap<>();
        HashMap<String, Integer> missingNiceToHaveSkillsFreq = new HashMap<>();
        int freq;
        for (JobMatchResult result : jobMatchResultList) {
            for (String missingSkill : result.getMissingRequiredSkills()) {
                if (missingRequiredSkillsFreq.containsKey(missingSkill)) {
                    freq = missingRequiredSkillsFreq.get(missingSkill);
                    freq++;
                    missingRequiredSkillsFreq.put(missingSkill, freq);
                } else {
                    missingRequiredSkillsFreq.put(missingSkill, 1);
                }
            }
            if (result.getMissingNiceToHaveSkills() != null) {
                for (String missingSkill : result.getMissingNiceToHaveSkills()) {
                    if (missingNiceToHaveSkillsFreq.containsKey(missingSkill)) {
                        freq = missingNiceToHaveSkillsFreq.get(missingSkill);
                        freq++;
                        missingNiceToHaveSkillsFreq.put(missingSkill, freq);
                    } else {
                        missingNiceToHaveSkillsFreq.put(missingSkill, 1);
                    }
                }
            }
        }
        return new MissingSkillFrequency(missingRequiredSkillsFreq, missingNiceToHaveSkillsFreq);
    }

    private List<String> rankTopSkillsToLearn(
            MissingSkillFrequency freq,
            List<JobMatchResult> jobResults
    ) {
        Map<String, Integer> priorityScore = new HashMap<>();

        for (Map.Entry<String, Integer> e : freq.getMissingRequiredSkillsFreq().entrySet()) {
            String skill = e.getKey();
            int score = e.getValue() * 3;   // required weight = 3
            priorityScore.put(skill, score);
        }

        for (Map.Entry<String, Integer> e : freq.getMissingNiceToHaveSkillsFreq().entrySet()) {
            String skill = e.getKey();
            int score = e.getValue();       // optional weight = 1
            priorityScore.put(skill,
                    priorityScore.getOrDefault(skill, 0) + score);
        }

        for (JobMatchResult result : jobResults) {
            if (result.getRecommendation() == DecisionType.PREPARE_THEN_APPLY) {

                for (String skill : result.getMissingRequiredSkills()) {
                    priorityScore.put(skill,
                            priorityScore.getOrDefault(skill, 0) + 2); // strong bonus
                }

                for (String skill : result.getMissingNiceToHaveSkills()) {
                    priorityScore.put(skill,
                            priorityScore.getOrDefault(skill, 0) + 1); // small bonus
                }
            }
        }

        List<Map.Entry<String, Integer>> sorted =
                new ArrayList<>(priorityScore.entrySet());

        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> rankedSkills = new ArrayList<>();
        for (Map.Entry<String, Integer> e : sorted) {
            rankedSkills.add(e.getKey());
        }

        return rankedSkills;
    }

    private Map<String, Integer> detectHighImpactSkills(
            MissingSkillFrequency freq,
            List<JobMatchResult> jobResults
    ) {

        Map<String, Integer> impactScore = new HashMap<>();

        for (Map.Entry<String, Integer> e : freq.getMissingRequiredSkillsFreq().entrySet()) {
            impactScore.put(e.getKey(), e.getValue() * 2);
        }

        for (Map.Entry<String, Integer> e : freq.getMissingNiceToHaveSkillsFreq().entrySet()) {
            impactScore.put(
                    e.getKey(),
                    impactScore.getOrDefault(e.getKey(), 0) + e.getValue()
            );
        }

        for (JobMatchResult result : jobResults) {

            if (result.getRecommendation() == DecisionType.PREPARE_THEN_APPLY) {
                for (String skill : result.getMissingRequiredSkills()) {
                    impactScore.put(
                            skill,
                            impactScore.getOrDefault(skill, 0) + 3
                    );
                }

                for (String skill : result.getMissingNiceToHaveSkills()) {
                    impactScore.put(
                            skill,
                            impactScore.getOrDefault(skill, 0) + 1
                    );
                }
            }
        }

        return impactScore;
    }

    private List<String> getTopImpactSkills(Map<String, Integer> impactMap) {

        List<Map.Entry<String, Integer>> sorted =
                new ArrayList<>(impactMap.entrySet());

        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : sorted) {
            result.add(e.getKey());
        }

        return result;
    }


    private void printActionPlan(JobBucket jobBucket, List<String> topSkillsToLearn, List<String> topImpactSkills) {
        System.out.println("Apply now - ");
        System.out.println(jobBucket.getApplyNowList());
        System.out.println("Prepare then apply - ");
        System.out.println(jobBucket.getPrepareThenApplyList());
        System.out.println("Skip - ");
        System.out.println(jobBucket.getSkipList());
        System.out.println("-----");
        System.out.println("Top skills to learn - ");
        System.out.println(topSkillsToLearn);
        System.out.println("-----");
        System.out.println("Skills that will have the most impact if learnt - ");
        System.out.println(topImpactSkills);
    }
}
