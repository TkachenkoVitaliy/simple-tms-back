package ru.vtkachenko.simpletmsback.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.dto.response.TestSuiteShortDto;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.repository.TestSuiteRepository;

import java.util.List;

@Slf4j
@Service
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;

    @Autowired
    public TestSuiteService(TestSuiteRepository testSuiteRepository) {
        this.testSuiteRepository = testSuiteRepository;
    }

    public List<TestSuiteShortDto> getShortTestSuites(Long projectId) {
        List<TestSuite> testSuites = testSuiteRepository.findTestSuiteByProject_Id(projectId);
        return testSuites.stream().map(testSuite -> TestSuiteShortDto.builder()
                .id(testSuite.getId())
                .name(testSuite.getName())
                .build()
        ).toList();
    }
}
