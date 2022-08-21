package com.matjipdaehak.fo.common.service;
import com.matjipdaehak.fo.common.model.CollegeStudentCount;
import java.util.*;

public interface CommonService {

    List<CollegeStudentCount> getNumberOfStudentsInEachCollege();

    /**
     * 현재 서버시스템상의 날짜를 long으로 반환한다.<br/>
     * new Date(Long date)를 사용해 Date객체를 얻을수있다.
     * @return long - System.currentTimeMillis()
     */
    long getCurrentDate();

    /**
     * 현재 system date정보와 랜덤함수를 조합해 유일한 아이디를 생성한다. <br/>
     * 완벽히 유일하단 보장은 없으므로 DB에 해당 id가 존재할경우를 대비할것.
     * @return
     */
    long getUniqueIdByCurrentDate();

    /**
     * getUniqueIdByCurrentDate메소드가 반환하는 형식의 long을 date로 해석해 반환
     * @param uniqueId - getUniqueIdByCurrentDate()로 부터 생성된 형식의 id
     * @return long형식의 date반환
     */
    long getCurrentDateFromUniqueId(long uniqueId);
}
