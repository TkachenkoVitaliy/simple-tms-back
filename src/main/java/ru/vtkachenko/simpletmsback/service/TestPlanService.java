package ru.vtkachenko.simpletmsback.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.dto.response.PageDto;
import ru.vtkachenko.simpletmsback.exception.business.BadRequestException;
import ru.vtkachenko.simpletmsback.exception.business.TestPlanNotFoundException;
import ru.vtkachenko.simpletmsback.exception.enums.ErrorEntity;
import ru.vtkachenko.simpletmsback.mapper.TestPlanMapper;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestPlan;
import ru.vtkachenko.simpletmsback.repository.TestPlanRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestPlanService {
    private final TestPlanRepository testPlanRepository;
    private final TestPlanMapper testPlanMapper;
    private final TestCaseService testCaseService;

    public List<TestPlanDto> getTestPlans(Long projectId) {
        List<TestPlan> testPlans = testPlanRepository.findTestPlansByProject_Id(projectId);
        return testPlans.stream()
                .map(testPlanMapper::toDto)
                .toList();
    }

    public TestPlanDto getTestPlanById(Long projectId, Long id) {
        TestPlan testPlan = findTestPlanById(projectId, id);
        return testPlanMapper.toDto(testPlan);
    }

    @Transactional
    public TestPlanDto createTestPlan(Long projectId, TestPlanDto testPlanDto) {
        if (testPlanDto.getProjectId() == null) {
            testPlanDto.setProjectId(projectId);
        }
        if (!testPlanDto.getProjectId().equals(projectId)) {
            String message = String.format("Project id conflicts. In path -- %s. In dto -- %s",
                    projectId, testPlanDto.getProjectId());
            log.error(message);
            throw new BadRequestException(message, ErrorEntity.PLAN);
        }
        TestPlan testPlan = testPlanMapper.toEntity(testPlanDto);
        List<Long> testCasesIds = new ArrayList<>(testPlanDto.getTestCases());
        List<TestCase> testCasesByIds = testCaseService.getTestCasesByIds(testCasesIds);
        testPlan.getTestCases().addAll(testCasesByIds);
        testPlan = testPlanRepository.save(testPlan);
        return testPlanMapper.toDto(testPlan);
    }

    @Transactional
    public TestPlanDto updateTestSuite(Long projectId, TestPlanDto testPlanDto) {
        TestPlan testPlan = findTestPlanById(projectId, testPlanDto.getId());
        testPlan.setName(testPlanDto.getName());
        testPlan.setDescription(testPlanDto.getDescription());
        Set<Long> testCasesIds = new HashSet<>(testPlanDto.getTestCases());
        Set<TestCase> copyTestCases = new HashSet<>(testPlan.getTestCases());
        copyTestCases.forEach(testCase -> {
            if (!testCasesIds.contains(testCase.getId())) {
                testPlan.removeTestCase(testCase);
                testCasesIds.remove(testCase.getId());
            } else {
                testCasesIds.remove(testCase.getId());
            }
        });
        log.info("testCasesIds - {}", testCasesIds);
        testCaseService.getTestCasesByIds(new ArrayList<>(testCasesIds))
                .forEach(testPlan::addTestCase);
        return testPlanMapper.toDto(testPlan);
    }

    @Transactional
    public void deleteTestPlan(Long projectId, Long id) {
        TestPlan testPlan = findTestPlanById(projectId, id);
        testPlanRepository.delete(testPlan);
    }

    private TestPlan findTestPlanById(Long projectId, Long id) {
        TestPlan testPlan = testPlanRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Cant find test plan with id - %s", id);
            log.error(message);
            return new TestPlanNotFoundException(message);
        });
        if (!Objects.equals(testPlan.getProject().getId(), projectId)) {
            String message = String.format("Cant find test plan with id - %s in project with id -- %s", id, projectId);
            log.error(message);
            throw new TestPlanNotFoundException(message);
        }
        return testPlan;
    }

    public PageDto<TestPlanDto> getTestPlansPageable(Long projectId, Integer page, Integer pageSize) {
        Page<TestPlan> testPlansPage = testPlanRepository.findTestPlansByProject_Id(projectId, PageRequest.of(page, pageSize));
        List<TestPlanDto> testPlans = testPlansPage.getContent().stream()
                .map(testPlanMapper::toDto)
                .collect(Collectors.toList());
        return new PageDto<>(testPlansPage.getTotalElements(), testPlans);
    }
}
