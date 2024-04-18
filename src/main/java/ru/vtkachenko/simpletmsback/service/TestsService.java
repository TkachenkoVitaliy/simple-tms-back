package ru.vtkachenko.simpletmsback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtkachenko.simpletmsback.constant.enums.TestsTreeNodeType;
import ru.vtkachenko.simpletmsback.dto.TestsTreeNodeDto;
import ru.vtkachenko.simpletmsback.dto.request.TestsTreeNodeUpdateParentDto;
import ru.vtkachenko.simpletmsback.dto.response.TestNodeDto;
import ru.vtkachenko.simpletmsback.model.TestCase;
import ru.vtkachenko.simpletmsback.model.TestSuite;
import ru.vtkachenko.simpletmsback.repository.TestCaseRepository;
import ru.vtkachenko.simpletmsback.repository.TestSuiteRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestsService {
    private final TestSuiteRepository testSuiteRepository;
    private final TestCaseRepository testCaseRepository;

    // TODO переделать на использование сервисов
    @Transactional
    public List<TestNodeDto> getTestsNodesByProject(Long projectId) {
        List<TestSuite> testSuites = testSuiteRepository.findTestSuiteByProject_Id(projectId);
        List<TestCase> testCases = testCaseRepository.findTestCaseByProject_Id(projectId);

        List<TestNodeDto> testSuiteNodes = testSuites.stream()
                .map(testSuite -> TestNodeDto.builder()
                        .id(testSuite.getId())
                        .parentSuiteId(testSuite.getParentSuite() == null ? null : testSuite.getParentSuite().getId())
                        .name(testSuite.getName())
                        .type(TestsTreeNodeType.SUITE)
                        .children(new ArrayList<>())
                        .build())
                .toList();

        List<TestNodeDto> testCaseNodes = testCases.stream()
                .map(testCase -> TestNodeDto.builder()
                        .id(testCase.getId())
                        .parentSuiteId(testCase.getParentSuite() == null ? null : testCase.getParentSuite().getId())
                        .name(testCase.getName())
                        .type(TestsTreeNodeType.CASE)
                        .build())
                .toList();

        List<TestNodeDto> testNodes = new ArrayList<>();

        Map <Long, TestNodeDto> nodesWithChildren = new HashMap<>();
        testSuiteNodes.forEach(testSuiteNode -> nodesWithChildren.put(testSuiteNode.getId(), testSuiteNode));
        testSuiteNodes.forEach(testSuiteNode -> {
            TestNodeDto currentNode = nodesWithChildren.get(testSuiteNode.getId());
            TestNodeDto parentNode = nodesWithChildren.get(testSuiteNode.getParentSuiteId());
            if (parentNode != null) {
                parentNode.getChildren().add(currentNode);
            } else {
                testNodes.add(currentNode);
            }
        });

        testCaseNodes.forEach(testCaseNode -> {
            TestNodeDto parentNode = nodesWithChildren.get(testCaseNode.getParentSuiteId());
            if (parentNode != null) {
                parentNode.getChildren().add(testCaseNode);
            } else {
                testNodes.add(testCaseNode);
            }
        });

        return testNodes;
    }

    @Transactional
    public List<TestsTreeNodeDto> getTestsTreeByProject(Long projectId) {
        final String SUITE = "suite/";
        final String CASE = "case/";


        List<TestSuite> testSuites = testSuiteRepository.findTestSuiteByProject_Id(projectId);
        List<TestCase> testCases = testCaseRepository.findTestCaseByProject_Id(projectId);
        List<TestsTreeNodeDto> treeNodes = new ArrayList<>();

        Map<String, List<String>> nodeChildren = new HashMap<>();

        // TODO вынести в методы
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


    @Transactional
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
