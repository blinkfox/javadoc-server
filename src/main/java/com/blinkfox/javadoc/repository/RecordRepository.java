package com.blinkfox.javadoc.repository;

import com.blinkfox.javadoc.pojo.Record;

import java.util.List;

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

    /**
     * 根据 groupId 的值查询其对应的最近新增的前 20 的 artifactId 的值.
     *
     * @param groupId groupId
     * @param count 查询的条数
     * @return 记录对象
     */
    @Query(value = "SELECT c_artifact_id FROM db_jdoc.t_record WHERE c_group_id = :groupId "
            + "GROUP BY c_artifact_id LIMIT :count", nativeQuery = true)
    List<String> queryArtifactIdsByGroupId(@Param("groupId") String groupId, @Param("count") int count);

    /**
     * 根据 groupId 和 artifactId 的值查询其对应的按倒序排列的前 20 的 version 的值.
     *
     * @param groupId groupId
     * @param artifactId artifactId
     * @param count 总数
     * @return version 集合
     */
    @Query(value = "SELECT c_version FROM db_jdoc.t_record WHERE c_group_id = :groupId "
            + "AND c_artifact_id = :artifactId ORDER BY c_version DESC LIMIT :count", nativeQuery = true)
    List<String> queryVersionsByGroupIdAndArtifactId(@Param("groupId") String groupId,
            @Param("artifactId") String artifactId, @Param("count") int count);

}
