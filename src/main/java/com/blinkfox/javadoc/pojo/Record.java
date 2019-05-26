package com.blinkfox.javadoc.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Javadoc 上传的记录表.
 *
 * @author blinkfox on 2019-05-26.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "t_record", schema = "db_jdoc")
public class Record {

    /**
     * ID.
     */
    @Id
    @Column(name = "c_id")
    private String id;

    /**
     * jar 的组 ID.
     */
    @Column(name = "c_group_id")
    private String groupId;

    /**
     * jar 的 artifactId.
     */
    @Column(name = "c_artifact_id")
    private String artifactId;

    /**
     * jar 的版本.
     */
    @Column(name = "c_version")
    private String version;

    /**
     * 该 javadoc 资源在 MinIO 中的 URL 地址.
     */
    @Column(name = "c_url")
    private String url;

    /**
     * jar 上传更新的时间.
     */
    @Column(name = "dt_uptime")
    private Date uptime;

}
