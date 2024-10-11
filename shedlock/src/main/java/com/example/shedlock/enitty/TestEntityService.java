package com.example.shedlock.enitty;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestEntityService {

    private final TestEntityRepository repository;

    @Transactional
    public void createEntity(String name) {

        TestEntity entity = TestEntity.of(name, LocalDateTime.now());
        repository.save(entity);
    }
}
