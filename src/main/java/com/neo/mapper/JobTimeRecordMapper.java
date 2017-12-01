package com.neo.mapper;

import com.neo.job.entity.JobTimeRecord;

import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/20
 * Desc:
 */
public interface JobTimeRecordMapper {
    List<JobTimeRecord> getJobTimeRecord(String triggerName);

    Integer insertJobTimeRecord(JobTimeRecord jobTimeRecord);

    Integer updateJobTimeRecord(JobTimeRecord jobTimeRecord);
}
