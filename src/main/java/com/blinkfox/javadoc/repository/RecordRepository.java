package com.blinkfox.javadoc.repository;

import com.blinkfox.javadoc.pojo.Record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * RecordRepository.
 *
 * @author blinkfox on 2019-05-26.
 */
public interface RecordRepository extends JpaRepository<Record, String> {

    /**
     * 根据 jar 包的三个坐标参数来查询对应的数据.
     *
     * @param groupId groupId
     * @param artifactId artifactId
     * @param version version
     * @return 记录对象
     */
    @Query("SELECT r FROM Record AS r WHERE r.groupId = :groupId AND r.artifactId = :artifactId "
            + "AND r.version = :version")
    Record queryByJarInfo(@Param("groupId") String groupId,
            @Param("artifactId") String artifactId, @Param("version") String version);

}
