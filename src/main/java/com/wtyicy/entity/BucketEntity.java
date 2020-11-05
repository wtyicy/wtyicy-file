package com.wtyicy.entity;

import com.aliyun.oss.model.CannedAccessControlList;

/**
 * 设置OSS权限
 * @author wtyicy
 *  2020-10-14 09:43
 */
public class BucketEntity extends AbstractEntity {

    /**
     * 私有读写	      CannedAccessControlList.Private <br>
     * 公共读私有写	  CannedAccessControlList.PublicRead <br>
     * 公共读写	      CannedAccessControlList.PublicReadWrite
     */
    private CannedAccessControlList acl;

    public BucketEntity(String bucketName) {
        super(bucketName);
    }

    public BucketEntity setAcl(CannedAccessControlList acl) {
        this.acl = acl;
        return this;
    }

    public CannedAccessControlList getAcl() {
        return acl;
    }
}
