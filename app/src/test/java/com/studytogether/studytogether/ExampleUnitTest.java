package com.studytogether.studytogether;

import com.studytogether.studytogether.Models.Course;
import com.studytogether.studytogether.Models.Group;
import com.studytogether.studytogether.Models.GroupChat;
import com.studytogether.studytogether.Models.TutorCourseListOfUser;
import com.studytogether.studytogether.Models.TutorCourseOfUser;
import com.studytogether.studytogether.Models.User;
import com.studytogether.studytogether.Models.UserGroupList;

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
    @Mock
    User user;
    @Mock
    UserGroupList userGroupList;
    @Mock
    Group group;
    @Mock
    Group group2;
    @Mock
    TutorCourseListOfUser tutorCourseListOfUser;
    @Mock
    TutorCourseOfUser tutorCourseOfUser;
    @Mock
    GroupChat groupChat;

    @Test
    public void initializeCourseTEST() {
        course = new Course("CSCI", 111, "Programming");
        assertEquals("CSCI", course.getSubject());
        assertEquals(111, course.getCategoryNum());
        assertEquals("Programming", course.getCourseTitle());
    }

    @Test
    public  void setCourseTEST() {
        course = new Course("CSCI", 111, "Programming");
        course.setSubject("EECE");
        course.setCategoryNum(320);
        course.setCourseTitle("Architecture");
        assertEquals("EECE", course.getSubject());
        assertEquals(320, course.getCategoryNum());
        assertEquals("Architecture", course.getCourseTitle());
    }

    @Test
    public void initializeUserTEST(){
        user = new User("admin@gmail.com", "111", "userImage", "admin", "groupKey", "member");
        assertEquals("admin@gmail.com", user.getUserEmail());
        assertEquals("111", user.getuserId());
        assertEquals("userImage", user.getuserImage());
        assertEquals("admin", user.getuserName());
        assertEquals("member", user.getUserPosition());
    }
    @Test
    public void setUserTEST(){
        user = new User("admin@gmail.com", "111", "userImage", "admin", "groupKey", "member");
        user.setUserPosition("owner");
        assertEquals("admin@gmail.com", user.getUserEmail());
        assertEquals("111", user.getuserId());
        assertEquals("userImage", user.getuserImage());
        assertEquals("admin", user.getuserName());
        assertEquals("owner", user.getUserPosition());
    }

    @Test
    public void initializeUserGroupListTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");

        userGroupList = new UserGroupList(group, "groupKey");

        assertEquals("groupKey", userGroupList.getGroupKey());
        assertEquals(group, userGroupList.getGroup());
    }

    @Test
    public void setUserGroupListTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");

        userGroupList = new UserGroupList(group, "groupKey");

        group2 = new Group("EECE", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");
        userGroupList.setGroup(group2);
        userGroupList.setGroupKey("groupKey2");

        assertEquals("groupKey2", userGroupList.getGroupKey());
        assertEquals(group2, userGroupList.getGroup());
    }

    @Test
    public void initializeTutorCourseListOfUserTEST() {
        course = new Course("CSCI", 111, "Programming");
        tutorCourseListOfUser = new TutorCourseListOfUser(course, "courseKey");
        assertEquals(course, tutorCourseListOfUser.getCourse());
        assertEquals("courseKey", tutorCourseListOfUser.getCourseKey());
    }

    @Test
    public void setTutorCourseListOfUserTEST() {
        course = new Course("CSCI", 111, "Programming");
        tutorCourseListOfUser = new TutorCourseListOfUser(course, "courseKey");
        course = new Course("EECE", 111, "Programming");
        tutorCourseListOfUser.setCourse(course);
        tutorCourseListOfUser.setCourseKey("courseKey2");
        assertEquals(course, tutorCourseListOfUser.getCourse());
        assertEquals("courseKey2", tutorCourseListOfUser.getCourseKey());
    }

    @Test
    public void initializeTutorCourseOfUserTEST() {
        course = new Course("CSCI", 111, "Programming");
        tutorCourseOfUser = new TutorCourseOfUser(course, "courseKey");
        assertEquals(course, tutorCourseOfUser.getCourse());
        assertEquals("courseKey", tutorCourseOfUser.getCourseKey());
    }

    @Test
    public void setTutorCourseOfUserTEST() {
        course = new Course("CSCI", 111, "Programming");
        tutorCourseOfUser = new TutorCourseOfUser(course, "courseKey");
        course = new Course("EECE", 111, "Programming");
        tutorCourseOfUser.setCourse(course);
        tutorCourseOfUser.setCourseKey("courseKey2");
        assertEquals(course, tutorCourseOfUser.getCourse());
        assertEquals("courseKey2", tutorCourseOfUser.getCourseKey());
    }

    @Test
    public void initializeGroupChatTEST() {
        groupChat = new GroupChat("hi", "userId", "userImage", "userName", "groupKey", false);
        assertEquals("hi", groupChat.getContent());
        assertEquals("groupKey", groupChat.getGroupKey());
        assertEquals("userId", groupChat.getuserId());
        assertEquals("userImage", groupChat.getuserImage());
        assertEquals("userName", groupChat.getuserName());
    }

    @Test
    public void setGroupChatTEST() {
        groupChat = new GroupChat("hi", "userId", "userImage", "userName", "groupKey", false);
        //groupChat

        assertEquals("hi", groupChat.getContent());
        assertEquals("groupKey", groupChat.getGroupKey());
        assertEquals("userId", groupChat.getuserId());
        assertEquals("userImage", groupChat.getuserImage());
        assertEquals("userName", groupChat.getuserName());
    }


}

