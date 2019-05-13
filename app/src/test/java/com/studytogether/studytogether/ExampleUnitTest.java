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
        assertEquals(false, groupChat.isMyComment());
    }

    @Test
    public void setGroupChatTEST() {
        groupChat = new GroupChat("hi", "userId", "userImage", "userName", "groupKey", false);
        groupChat.setContent("hello");
        groupChat.setGroupKey("groupKey2");
        groupChat.setMyComment(true);
        groupChat.setuserId("userId2");
        groupChat.setuserImage("userImage2");
        groupChat.setuserName("userName2");

        assertEquals("hello", groupChat.getContent());
        assertEquals("groupKey2", groupChat.getGroupKey());
        assertEquals("userId2", groupChat.getuserId());
        assertEquals("userImage2", groupChat.getuserImage());
        assertEquals("userName2", groupChat.getuserName());
        assertEquals(true, groupChat.isMyComment());
    }


    @Test
    public void initializeGroupTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");

        assertEquals("CSCI", group.getGroupCourseSubject());
        assertEquals("111", group.getGroupCourseCategoryNum());
        assertEquals("StudyTogether", group.getGroupName());
        assertEquals("Test", group.getGroupGoal());
        assertEquals("Library", group.getGroupPlace());
        assertEquals(4, group.getMaximumGroupMembers());
        assertEquals(1, group.getCurrentGroupMembers());
        assertEquals(0, group.getStartHour());
        assertEquals(0, group.getStartMin());
        assertEquals(12, group.getEndHour());
        assertEquals(0, group.getEndMin());
        assertEquals("groupPicture", group.getGroupPicture());
        assertEquals("111", group.getOwnerId());
        assertEquals("groupOwnerPhoto", group.getGroupOwnerPhoto());
        assertEquals("groupOwnerName", group.getOwnerName());
        assertEquals("groupPicture", group.getGroupPicture());
        assertEquals(false, group.isTutorHere());


    }
    @Test
    public void setGroupTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");
        group.setTutorHere(true);
        group.setGroupCourseSubject("EECE");
        group.setGroupCourseCategoryNum("211");
        group.setGroupName("Study");
        group.setGroupGoal("Meeting");
        group.setGroupPlace("OCNL");
        group.setMaximumGroupMembers(2);
        group.setCurrentGroupMembers(1);
        group.setStartHour(1);
        group.setStartMin(30);
        group.setEndHour(1);
        group.setEndMin(30);
        group.setGroupPicture("groupPicture2");
        group.setOwnerId("222");
        group.setGroupOwnerPhoto("groupOwnerPhoto2");
        group.setOwnerName("groupOwnerName2");


        assertEquals("EECE", group.getGroupCourseSubject());
        assertEquals("211", group.getGroupCourseCategoryNum());
        assertEquals("Study", group.getGroupName());
        assertEquals("Meeting", group.getGroupGoal());
        assertEquals("OCNL", group.getGroupPlace());
        assertEquals(2, group.getMaximumGroupMembers());
        assertEquals(1, group.getCurrentGroupMembers());
        assertEquals(1, group.getStartHour());
        assertEquals(30, group.getStartMin());
        assertEquals(1, group.getEndHour());
        assertEquals(30, group.getEndMin());
        assertEquals("groupPicture2", group.getGroupPicture());
        assertEquals("222", group.getOwnerId());
        assertEquals("groupOwnerPhoto2", group.getGroupOwnerPhoto());
        assertEquals("groupOwnerName2", group.getOwnerName());
        assertEquals(true, group.isTutorHere());
    }

    @Test
    public void startEndTimeGroupTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");

        assertEquals("12 : 00 AM", group.getStartTime());
        assertEquals("12 : 00 PM", group.getEndTime());
    }

    @Test
    public void addCurrentMemberGroupTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");

        group.addGroupMembers();
        assertEquals(2, group.getCurrentGroupMembers());
    }

    @Test
    public void reduceCurrentMemberGroupTEST() {
        group = new Group("CSCI", "111", "StudyTogether", "Test", "Library", 4, 0, 0, 12, 0, "groupPicture", "111", "groupOwnerPhoto", "groupOwnerName");

        group.addGroupMembers();
        group.addGroupMembers();
        group.addGroupMembers();
        group.reduceGroupMembers();
        assertEquals(3, group.getCurrentGroupMembers());
    }



}

