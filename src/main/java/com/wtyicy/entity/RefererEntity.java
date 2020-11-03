package com.wtyicy.entity;


import java.util.List;

/**
 * 推荐实体
 * @author wtyicy
 * @date 2020-10-14 09:43
 */
public class RefererEntity extends AbstractEntity {

    List<String> refererList;

    public RefererEntity(String bucketName) {
        super(bucketName);
    }

    public void setRefererList(List<String> refererList) {
        this.refererList = refererList;
    }

    public List<String> getRefererList() {
        return refererList;
    }
}
