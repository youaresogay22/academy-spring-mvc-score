package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.exception.StudentNotFoundException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import com.nhnacademy.springmvc.repository.StudentRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/student")
@Slf4j
public class StudentController {
    private final StudentRepositoryImpl studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = (StudentRepositoryImpl) studentRepository;
    }

    @GetMapping("/{studentId}")
    public ModelAndView viewStudent(@PathVariable("studentId") Long studentId) {

        if (studentId != null && studentRepository.exists(studentId)) {

            ModelAndView mav = new ModelAndView("studentView");
            mav.addObject("student", studentRepository.getStudent(studentId));
            log.debug("view raw///id: {}, map:{}", studentId, studentRepository.studentMap);
            return mav;
        } else throw new StudentNotFoundException();

    }

    @GetMapping(value = "/{studentId}", params = "hideScore=yes")
    public String viewStudentWithScoreHidden(@PathVariable Long studentId, Model model) {

        if (studentId != null && studentRepository.exists(studentId)) {

            Student student = studentRepository.getStudent(studentId);
            model.addAttribute("student", Student.constructScoreAndCommentMaskedStudent(student));
            log.debug("view masked///id: {}, map:{}", studentId, studentRepository.studentMap);
            return "studentView";
        } else
            throw new StudentNotFoundException();
    }

}
