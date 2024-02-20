package com.nhnacademy.springmvc.controller;

import com.nhnacademy.springmvc.domain.Student;
import com.nhnacademy.springmvc.domain.StudentModifyRequest;
import com.nhnacademy.springmvc.exception.ValidationFailedException;
import com.nhnacademy.springmvc.repository.StudentRepository;
import com.nhnacademy.springmvc.repository.StudentRepositoryImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/student")
public class StudentController {
    private final StudentRepositoryImpl studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = (StudentRepositoryImpl) studentRepository;
    }

    @GetMapping("/{studentId}")
    public String viewStudent(@PathVariable("studentId") long studentId) {
        ModelAndView mav = new ModelAndView("studentView");
        mav.addObject("student", studentRepository.getStudent(studentId));
        return "studentView";
    }

    @GetMapping("/student/{studentId}?hideScore=yes")
    public String viewStudentWithScoreHidden(@PathVariable String studentId,
                                             ModelMap modelMap) {
        Student student = studentRepository.getStudent(Long.parseLong(studentId));
        modelMap.addAttribute("student", Student.constructScoreAndCommentMaskedStudent(student));
        return "studentView";
    }

    @GetMapping("/{studentId}/modify")
    public String studentModifyForm(@PathVariable("studentId") long studentId, @ModelAttribute Student student, Model model) {

        return "studentModify";
    }

    @PostMapping("/{studentId}/modify")
    public String modifyStudent(@PathVariable("studentId") long studentId,
                                @ModelAttribute Student student,
                                @Valid @ModelAttribute StudentModifyRequest studentModifyRequest,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        student.setName(studentModifyRequest.getName());
        student.setEmail(studentModifyRequest.getEmail());
        student.setScore(studentModifyRequest.getScore());
        student.setComment(studentModifyRequest.getComment());

        studentRepository.modify(student);

        model.addAttribute("student", student);
        return "studentView";
    }

}
