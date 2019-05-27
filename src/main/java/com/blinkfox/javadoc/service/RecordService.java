package com.blinkfox.javadoc.service;

import com.blinkfox.javadoc.consts.Const;
import com.blinkfox.javadoc.repository.RecordRepository;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

/**
 * RecordService.
 *
 * @author blinkfox on 2019-05-27.
 */
@Service
public class RecordService {

    @Resource
    private RecordRepository recordRepository;

    /**
     * 根据 groupId 的值查询其对应的最近新增的前 20 的 artifactId 的值.
     *
     * @return artifactId的集合
     */
    public List<String> findArtifactIdsByGroupId(String groupId) {
        return recordRepository.queryArtifactIdsByGroupId(groupId, Const.PAGE_SIZE);
    }

    /**
     * 根据 groupId 和 artifactId 的值查询其对应的按倒序排列的前 20 的 version 的值.
     *
     * @return version 的集合
     */
    public List<String> findVersionsByGroupIdAndArtifactId(String groupId, String artifactId) {
        return recordRepository.queryVersionsByGroupIdAndArtifactId(groupId, artifactId, Const.PAGE_SIZE);
    }

}
