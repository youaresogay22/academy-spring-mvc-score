package com.nhnacademy.springmvc.repository;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudentRepositoryImpl implements StudentRepository {
    private final Map<Long, Student> studentMap = new HashMap<>();

    @Override
    public boolean exists(long id) {
        return studentMap.containsKey(id);
    }

    @Override
    public Student register(String name, String email, int score, String comment) {
        Student student = Student.constructIdGeneratedStudent(name, email, score, comment);
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public Student getStudent(long id) {
        return exists(id) ? studentMap.get(id) : null;
    }

    public void modify(Student student) {
        Student dbStudent = getStudent(student.getId());

        if (Objects.isNull(dbStudent)) {
            throw new StudentNotFoundException();
        }

        dbStudent.setName(student.getName());
        dbStudent.setEmail(student.getEmail());
        dbStudent.setScore(student.getScore());
        dbStudent.setComment(student.getComment());
    }
}
