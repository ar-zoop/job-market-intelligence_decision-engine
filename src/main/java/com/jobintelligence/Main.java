package com.jobintelligence;

import com.jobintelligence.model.*;
import com.jobintelligence.service.ActionPlanService;
import com.jobintelligence.service.GapExplanationService;
import com.jobintelligence.service.JobScorer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main (String[] args) {
        try{
            List<JobMatchResult> jobMatchResultList = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            // Load resume.json from resources
            InputStream resumeStream =
                Main.class.getClassLoader().getResourceAsStream("resume.json");
            ResumeProfile resume =
                mapper.readValue(resumeStream, ResumeProfile.class);

            // Load job.json from resources
            for (int i = 0 ; i < 3; i++) {
                String job_filename = String.format("jds/jd_%d.json", i+1);
                InputStream jobStream =
                    Main.class.getClassLoader().getResourceAsStream(job_filename);
                JobProfile job =
                    mapper.readValue(jobStream, JobProfile.class);

                JobScorer jobScorerReport = new JobScorer();
                JobMatchResult result = jobScorerReport.score(resume, job);

                GapExplanationService gapExplanationService = new GapExplanationService();
                gapExplanationService.enrichJobMatchResult(result, resume, job);
                gapExplanationService.printExplanation(result);

                jobMatchResultList.add(result);
            }

            ActionPlanService actionPlanService = new ActionPlanService();
            actionPlanService.execute(jobMatchResultList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

