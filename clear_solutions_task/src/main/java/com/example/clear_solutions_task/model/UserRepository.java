package com.example.clear_solutions_task.model;

import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Stream<UserEntity> findUserEntitiesByBirthDayDateBetween(@Param("from") Long from, @Param("to") Long to);

}
