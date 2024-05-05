package ru.vtkachenko.simpletmsback.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.vtkachenko.simpletmsback.constant.enums.TestRunState;
import ru.vtkachenko.simpletmsback.model.TestRun;
import ru.vtkachenko.simpletmsback.repository.TestRunRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestRunService {
    private final TestRunRepository testRunRepository;

    public TestRun test() {
        TestRun testRun = TestRun.builder()
                .name("testRunFirst")
                .state(TestRunState.NOT_STARTED)
                .build();
        TestRun saved = testRunRepository.save(testRun);
        return saved;
    }
}
