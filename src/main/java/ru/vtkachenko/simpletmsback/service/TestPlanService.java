package ru.vtkachenko.simpletmsback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.dto.TestPlanDto;
import ru.vtkachenko.simpletmsback.exception.business.BadRequestException;
import ru.vtkachenko.simpletmsback.exception.business.TestPlanNotFoundException;
import ru.vtkachenko.simpletmsback.exception.enums.ErrorEntity;
import ru.vtkachenko.simpletmsback.mapper.TestPlanMapper;
import ru.vtkachenko.simpletmsback.model.TestPlan;
import ru.vtkachenko.simpletmsback.repository.TestPlanRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestPlanService {
    private final TestPlanRepository testPlanRepository;
    private final TestPlanMapper testPlanMapper;

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
        testPlan = testPlanRepository.save(testPlan);
        return testPlanMapper.toDto(testPlan);
    }
}
