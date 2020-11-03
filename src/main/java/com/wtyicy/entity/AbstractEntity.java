package com.wtyicy.entity;


/**
 * 定义bucketName
 * @author wtyicy
 * @date 2020-10-14 08:43
 */
public abstract class AbstractEntity {

    private String bucketName;

    public AbstractEntity() {
    }

    public AbstractEntity(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
