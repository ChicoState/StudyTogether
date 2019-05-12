package com.studytogether.studytogether;

import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.Models.Group;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Mock
    Course course;

    @Test
    public void initializeCourse() {
        course = new Course("CSCI", 111, "Programming");
        assertEquals("CSCI", course.getSubject());
    }


}

