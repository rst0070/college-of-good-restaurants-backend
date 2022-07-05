package com.matjipdaehak.fo.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeStudentCount {

    @JsonProperty("college_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int collegeId;

    @JsonProperty("college_name")
    private String collegeName;

    @JsonProperty("number_of_students")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int numberOfStudents;
}
