package com.jobintelligence.model;

import java.util.HashMap;

public class MissingSkillFrequency {
    HashMap<String, Integer> missingRequiredSkillsFreq;
    HashMap<String, Integer> missingNiceToHaveSkillsFreq;

    public MissingSkillFrequency(HashMap<String, Integer> missingRequiredSkillsFreq, HashMap<String, Integer> missingNiceToHaveSkillsFreq) {
        this.missingRequiredSkillsFreq = missingRequiredSkillsFreq;
        this.missingNiceToHaveSkillsFreq = missingNiceToHaveSkillsFreq;
    }

    public HashMap<String, Integer> getMissingRequiredSkillsFreq() {
        return missingRequiredSkillsFreq;
    }

    public HashMap<String, Integer> getMissingNiceToHaveSkillsFreq() {
        return missingNiceToHaveSkillsFreq;
    }
}
