package ru.vtkachenko.simpletmsback.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.constant.TestsTreeNodeType;
import ru.vtkachenko.simpletmsback.dto.TestsTreeNodeDto;
import ru.vtkachenko.simpletmsback.dto.request.TestsTreeNodeUpdateParentDto;
import ru.vtkachenko.simpletmsback.model.Project;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;
import ru.vtkachenko.simpletmsback.repository.TestSuiteRepository;

import java.util.*;

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
        final String SUITE = "suite/";
        final String CASE = "case/";


        List<TestSuite> testSuites = testSuiteRepository.findTestSuiteByProject_Id(projectId);
        List<TestCase> testCases = testCaseRepository.findTestCaseByProject_Id(projectId);
        List<TestsTreeNodeDto> treeNodes = new ArrayList<>();

        Map<String, List<String>> nodeChildren = new HashMap<>();

        testSuites.forEach(testSuite -> {
            String parentSyntheticId = testSuite.getParentSuite() == null ? null : SUITE + testSuite.getParentSuite().getId();
            List<String> children = nodeChildren.computeIfAbsent(parentSyntheticId, k -> new ArrayList<>());
            children.add(SUITE + testSuite.getId());
        });

        testCases.forEach(testCase -> {
            String parentSyntheticId = testCase.getParentSuite() == null ? null : SUITE + testCase.getParentSuite().getId();
            List<String> children = nodeChildren.computeIfAbsent(parentSyntheticId, k -> new ArrayList<>());
            children.add(CASE + testCase.getId());
        });

        testSuites.forEach(testSuite -> {
            TestsTreeNodeDto treeNode = TestsTreeNodeDto.builder()
                    .id(testSuite.getId())
                    .text(testSuite.getName())
                    .parentId(testSuite.getParentSuite() == null ? null : testSuite.getParentSuite().getId())
                    .type(TestsTreeNodeType.SUITE)
                    .children(nodeChildren.getOrDefault(SUITE + testSuite.getId(), new ArrayList<>()))
                    .build();
            treeNodes.add(treeNode);
        });

        testCases.forEach(testCase -> {
            TestsTreeNodeDto treeNode = TestsTreeNodeDto.builder()
                    .id(testCase.getId())
                    .text(testCase.getName())
                    .parentId(testCase.getParentSuite() == null ? null : testCase.getParentSuite().getId())
                    .type(TestsTreeNodeType.CASE)
                    .children(new ArrayList<>())
                    .build();
            treeNodes.add(treeNode);
        });

        return treeNodes;
    }


    public void updateTestsTreeNodeParentSuite(Long nodeId, TestsTreeNodeUpdateParentDto updateParentDto) {
        if (!Objects.equals(nodeId, updateParentDto.getNodeId())) {
            throw new RuntimeException(""); // TODO создать ошибку для таких случаев
        }
        if (updateParentDto.getType() == TestsTreeNodeType.SUITE) {
            TestSuite testSuite = testSuiteRepository.findById(nodeId).orElseThrow(() -> new RuntimeException(""));
            TestSuite parentSuite = updateParentDto.getParentId() == null
                    ? null
                    : testSuiteRepository.getReferenceById(updateParentDto.getParentId());
            testSuite.setParentSuite(parentSuite);
            testSuiteRepository.save(testSuite);
        }
        if (updateParentDto.getType() == TestsTreeNodeType.CASE) {
            TestCase testCase = testCaseRepository.findById(nodeId).orElseThrow(() -> new RuntimeException(""));
            TestSuite parentSuite = updateParentDto.getParentId() == null
                    ? null
                    : testSuiteRepository.getReferenceById(updateParentDto.getParentId());
            testCase.setParentSuite(parentSuite);
            testCaseRepository.save(testCase);
        }
    }
}
