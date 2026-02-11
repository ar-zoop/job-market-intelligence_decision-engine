package com.jobintelligence;

import com.jobintelligence.model.*;
import com.jobintelligence.service.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PythonInterpretationService pythonService = new PythonInterpretationService();
            List<JobMatchResult> jobMatchResults = new ArrayList<>();

            //Resume
            InputStream resumeStream =
                Main.class.getClassLoader().getResourceAsStream("resume.txt");

            if (resumeStream == null) {
                throw new RuntimeException("resume.txt not found in resources");
            }

            String resumeText = new String(
                resumeStream.readAllBytes(),
                StandardCharsets.UTF_8
            );

            if (resumeText.isEmpty()) {
                throw new RuntimeException("Empty resume. Invalid argument.");
            }

            Optional<String> resumeJsonOpt =
                pythonService.interpretResumeText(resumeText);
            ResumeProfile resume = new ResumeProfile();
            if (resumeJsonOpt.isPresent()) {
                 resume = mapper.readValue(resumeJsonOpt.get(), ResumeProfile.class);
            } else {
                System.out.println("Resume interpretation empty. Attempting to convert raw resume text to ResumeProfile.");
                resume = mapper.readValue(resumeText, ResumeProfile.class);
            }

            // Job Loop
            for (int i = 1; i <= 4; i++) {

                String filename = String.format("jds/jd_%d.txt", i);

                InputStream jobStream =
                    Main.class.getClassLoader().getResourceAsStream(filename);

                if (jobStream == null) {
                    System.out.println(filename + " not found. Skipping.");
                    continue;
                }

                String jobText = new String(
                    jobStream.readAllBytes(),
                    StandardCharsets.UTF_8
                );

                Optional<String> jobJsonOpt =
                    pythonService.interpretJobText(jobText);

                if (jobJsonOpt.isEmpty()) {
                    System.out.println("Python job interpretation failed. Skipping job.");
                    continue;
                }

                JobProfile job =
                    mapper.readValue(jobJsonOpt.get(), JobProfile.class);

                // Scoring
                JobScorer jobScorer = new JobScorer();
                JobMatchResult result = jobScorer.score(resume, job);

                GapExplanationService gapService = new GapExplanationService();
                gapService.enrichJobMatchResult(result, resume, job);
                gapService.printExplanation(result);

                jobMatchResults.add(result);
            }

            // Action Plan
            ActionPlanService actionPlanService = new ActionPlanService();
            actionPlanService.execute(jobMatchResults);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
