package ru.vtkachenko.simpletmsback.controller;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.vtkachenko.simpletmsback.dto.ErrorInfo;
import ru.vtkachenko.simpletmsback.i18n.I18n;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ApplicationAdvice {

    private final I18n i18n;

    public ApplicationAdvice(I18n i18n) {
        this.i18n = i18n;
    }
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<List<ErrorInfo>> handleConstraintException(ConstraintViolationException e) {
//        List<ErrorInfo> constraints = new ArrayList<>();
//        constraints.add(new ErrorInfo(e.getErrorCode(), e.getConstraintName()));
//        List<ErrorInfo> constraints = e.getConstraintViolations().stream()
//                .map(constraint -> constraint.getRootBeanClass().getName() + " " + constraint.getPropertyPath())
//                .map(message -> new ErrorInfo(100, message))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(constraints, HttpStatus.CONFLICT);
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<List<ErrorInfo>> handleHibernateException(DataIntegrityViolationException e) {
        List<ErrorInfo> errors;
        if (e.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintExceptions = (ConstraintViolationException) e.getCause();
            errors = constraintExceptions.getConstraintViolations().stream()
                    .map(constraintEx -> new ErrorInfo(100, constraintEx.getRootBeanClass().getSimpleName()
                            + " " + constraintEx.getPropertyPath() + " " + i18n.translate("error.constraint")))
                    .toList();
        } else {
            errors = List.of(new ErrorInfo(101, i18n.translate("error.constraint")));
        }
        return new ResponseEntity<>(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
