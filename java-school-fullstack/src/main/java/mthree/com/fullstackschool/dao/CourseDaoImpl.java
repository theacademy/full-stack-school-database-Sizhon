package mthree.com.fullstackschool.dao;

import mthree.com.fullstackschool.dao.mappers.CourseMapper;
import mthree.com.fullstackschool.model.Course;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CourseDaoImpl implements CourseDao {

    private final JdbcTemplate jdbcTemplate;

    public CourseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Course createNewCourse(Course course) {
        //YOUR CODE STARTS HERE
        final String sql = "INSERT INTO course (courseCode, courseDesc, teacherId) VALUES ( ?, ?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseDesc());
            statement.setInt(3, course.getTeacherId());
            return statement;

        }, keyHolder);

        course.setCourseId(keyHolder.getKey().intValue());

        return course;

        //YOUR CODE ENDS HERE
    }

    @Override
    public List<Course> getAllCourses() {
        //YOUR CODE STARTS HERE
        final String sql = "SELECT * FROM course;";
        return jdbcTemplate.query(sql, new CourseMapper());
        //YOUR CODE ENDS HERE
    }

    @Override
    public Course findCourseById(int id) {
        //YOUR CODE STARTS HERE
        final String sql = "SELECT * FROM course WHERE cid = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, new CourseMapper(), id);
        } catch (DataAccessException e) {
            Course course = new Course();
            course.setCourseName("Course Not Found");
            course.setCourseDesc("Course Not Found");
            return course;
        }

        //YOUR CODE ENDS HERE
    }

    @Override
    public void updateCourse(Course course) {
        //YOUR CODE STARTS HERE
        final String sql = "UPDATE course SET " +
                "courseCode = ?, " +
                "courseDesc = ?, " +
                "teacherID = ? " +
                "WHERE cid = ?;";

        jdbcTemplate.update(sql,
                course.getCourseName(),
                course.getCourseDesc(),
                course.getTeacherId(),
                course.getCourseId());
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteCourse(int id) {
        //YOUR CODE STARTS HERE
        final String sql = "DELETE FROM course WHERE cid = ?;";
        jdbcTemplate.update(sql, id);
        //YOUR CODE ENDS HERE
    }

    @Override
    public void deleteAllStudentsFromCourse(int courseId) {
        //YOUR CODE STARTS HERE
        final String sql = "DELETE FROM course_student WHERE course_id = ?;";
        jdbcTemplate.update(sql, courseId);
        //YOUR CODE ENDS HERE
    }
}
