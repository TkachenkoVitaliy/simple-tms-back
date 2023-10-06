package ru.vtkachenko.simpletmsback.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.constant.TestsTreeNodeType;
import ru.vtkachenko.simpletmsback.dto.TestsTreeNodeDto;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;
import ru.vtkachenko.simpletmsback.repository.TestSuiteRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TestsService {
    private final TestSuiteRepository testSuiteRepository;
    private final TestCaseRepository testCaseRepository;

    @Autowired
    public TestsService(TestSuiteRepository testSuiteRepository, TestCaseRepository testCaseRepository) {
        this.testSuiteRepository = testSuiteRepository;
        this.testCaseRepository = testCaseRepository;
    }


    public List<TestsTreeNodeDto> getTestsTreeByProject(Long projectId) {
        List<TestSuite> testSuites = testSuiteRepository.findTestSuiteByProject_Id(projectId);
        List<TestCase> testCases = testCaseRepository.findTestCaseByProject_Id(projectId);
        List<TestsTreeNodeDto> treeNodes = new ArrayList<>();

        Map<Long, List<Long>> nodeChildren = new HashMap<>();

        testSuites.forEach(testSuite -> {
            Long parentId = testSuite.getParentSuite() == null ? null : testSuite.getParentSuite().getId();
            List<Long> children = nodeChildren.computeIfAbsent(parentId, k -> new ArrayList<>());
            children.add(testSuite.getId());
        });

        testCases.forEach(testCase -> {
            Long parentId = testCase.getParentSuite() == null ? null : testCase.getParentSuite().getId();
            List<Long> children = nodeChildren.computeIfAbsent(parentId, k -> new ArrayList<>());
            children.add(testCase.getId());
        });

        testSuites.forEach(testSuite -> {
            TestsTreeNodeDto treeNode = TestsTreeNodeDto.builder()
                    .id(testSuite.getId())
                    .text(testSuite.getName())
                    .parent(testSuite.getParentSuite() == null ? 0 : testSuite.getParentSuite().getId())
                    .type(TestsTreeNodeType.SUITE)
                    .children(nodeChildren.getOrDefault(testSuite.getId(), new ArrayList<>()))
                    .build();
            treeNodes.add(treeNode);
        });

        testCases.forEach(testCase -> {
            TestsTreeNodeDto treeNode = TestsTreeNodeDto.builder()
                    .id(testCase.getId())
                    .text(testCase.getName())
                    .parent(testCase.getParentSuite() == null ? 0 : testCase.getParentSuite().getId())
                    .type(TestsTreeNodeType.CASE)
                    .children(nodeChildren.getOrDefault(testCase.getId(), new ArrayList<>()))
                    .build();
            treeNodes.add(treeNode);
        });

        return treeNodes;
    }


}
