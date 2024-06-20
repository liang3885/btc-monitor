package org.tron.btcmonitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.tron.btcmonitor.entity.Progress;

public interface ProgressRepository extends JpaRepository<Progress, Long> {

    @Query(value="select * from progress where progress_key = ?1", nativeQuery = true)
    Progress getByKey(String key);

    @Query(value="update progress set value = ?2 where progress_key = ?1", nativeQuery = true)
    void update(String progressKey, Long lastHeight);

}
