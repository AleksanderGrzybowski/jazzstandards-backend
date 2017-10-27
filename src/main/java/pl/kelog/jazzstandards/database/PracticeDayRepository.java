package pl.kelog.jazzstandards.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeDayRepository extends JpaRepository<PracticeDay, Long> {
}
