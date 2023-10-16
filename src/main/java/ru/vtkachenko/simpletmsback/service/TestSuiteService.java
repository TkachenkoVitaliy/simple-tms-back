package ru.vtkachenko.simpletmsback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Service
public class TestSuiteService {

    private final TestSuiteRepository testSuiteRepository;
    private final TestSuiteMapper mapper;

    @Autowired
    public TestSuiteService(TestSuiteRepository testSuiteRepository, @Lazy TestSuiteMapper mapper) {
        this.testSuiteRepository = testSuiteRepository;
        this.mapper = mapper;
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

    public TestSuiteDto getTestSuiteById(Long id) {
        TestSuite testSuite = testSuiteRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Cant find test suite with id - %s", id);
            log.error(message);
            throw new TestSuiteNotFoundException(message);
        });
        return mapper.toDto(testSuite);
    }

    @Transactional
    public TestSuiteDto createTestSuite(TestSuiteDto testSuiteDto) {
        TestSuite testSuite = mapper.toEntity(testSuiteDto);
        testSuite = testSuiteRepository.save(testSuite);
        return mapper.toDto(testSuite);
    }

    @Transactional
    public TestSuiteDto updateTestSuite(TestSuiteDto testSuiteDto) {
        TestSuite testSuite = testSuiteRepository.findById(testSuiteDto.getId()).orElseThrow(() -> {
            String message = String.format("Cant update test suite with id - %s, cause test suite with this id not found", testSuiteDto.getId());
            log.error(message);
            throw new TestSuiteNotFoundException(message);
        });
        testSuite.setParentSuite(getTestSuiteReferenceById(testSuiteDto.getParentSuiteId()));
        testSuite.setName(testSuiteDto.getName());
        testSuite.setDescription(testSuiteDto.getDescription());
        return mapper.toDto(testSuite);
    }

    @Transactional
    public void deleteTestSuite(Long id) {
        testSuiteRepository.deleteById(id);
    }
}
