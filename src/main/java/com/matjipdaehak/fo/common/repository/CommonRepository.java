package com.matjipdaehak.fo.common.repository;
import com.matjipdaehak.fo.common.model.CollegeStudentCount;

import java.util.*;

public interface CommonRepository {

    List<CollegeStudentCount> getNumberOfStudentsInEachCollege();
}
