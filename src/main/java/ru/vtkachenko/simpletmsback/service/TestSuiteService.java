package ru.vtkachenko.simpletmsback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.dto.TestSuiteDto;
import ru.vtkachenko.simpletmsback.dto.response.TestSuiteShortDto;
import ru.vtkachenko.simpletmsback.exception.business.TestSuiteNotFoundException;
import ru.vtkachenko.simpletmsback.mapper.TestSuiteMapper;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.repository.TestSuiteRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final TestSuiteMapper testSuiteMapper;

    public TestSuiteService(TestSuiteRepository testSuiteRepository, @Lazy TestSuiteMapper testSuiteMapper) {
        this.testSuiteRepository = testSuiteRepository;
        this.testSuiteMapper = testSuiteMapper;
    }


    public TestSuite getTestSuiteReferenceById(Long testSuiteId) {
        if (testSuiteId == null) {
            return null;
        }
        try {
            return testSuiteRepository.getReferenceById(testSuiteId);
        } catch (EntityNotFoundException e) {
            String message = String.format("Cant find suite with id - %s", testSuiteId);
            log.error(message);
            throw new TestSuiteNotFoundException(message);
        }
    }

    // TODO возможно убрать
    public List<TestSuiteShortDto> getShortTestSuites(Long projectId) {
        List<TestSuite> testSuites = testSuiteRepository.findTestSuiteByProject_Id(projectId);
        return testSuites.stream().map(testSuite -> TestSuiteShortDto.builder()
                .id(testSuite.getId())
                .name(testSuite.getName())
                .build()
        ).toList();
    }

    public TestSuiteDto getTestSuiteById(Long projectId, Long id) {
        TestSuite testSuite = findTestSuiteById(projectId, id);
        return testSuiteMapper.toDto(testSuite);
    }

    @Transactional
    public TestSuiteDto createTestSuite(TestSuiteDto testSuiteDto) {
        TestSuite testSuite = testSuiteMapper.toEntity(testSuiteDto);
        testSuite = testSuiteRepository.save(testSuite);
        return testSuiteMapper.toDto(testSuite);
    }

    @Transactional
    public TestSuiteDto updateTestSuite(Long projectId, TestSuiteDto testSuiteDto) {
        TestSuite testSuite = findTestSuiteById(projectId, testSuiteDto.getId());
        testSuite.setParentSuite(getTestSuiteReferenceById(testSuiteDto.getParentSuiteId()));
        testSuite.setName(testSuiteDto.getName());
        testSuite.setDescription(testSuiteDto.getDescription());
        return testSuiteMapper.toDto(testSuite);
    }

    @Transactional
    public void deleteTestSuite(Long projectId, Long id) {
        TestSuite testSuite = findTestSuiteById(projectId, id);
        testSuiteRepository.delete(testSuite);
    }

    private TestSuite findTestSuiteById(Long projectId, Long id) {
        TestSuite testSuite = testSuiteRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Cant find test suite with id - %s", id);
            log.error(message);
            return new TestSuiteNotFoundException(message);
        });
        if (!Objects.equals(testSuite.getProject().getId(), projectId)) {
            String message = String.format("Cant find test suite with id - %s in project with id -- %s", id, projectId);
            log.error(message);
            throw new TestSuiteNotFoundException(message);
        }
        return testSuite;
    }
}
