package com.matjipdaehak.fo.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CollegeStudentCount {

    @JsonProperty("college-name")
    private String collegeName;
    @JsonProperty("number-of-students")
    private int numberOfStudents;
}
