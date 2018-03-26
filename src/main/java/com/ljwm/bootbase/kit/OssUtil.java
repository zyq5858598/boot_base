package com.ljwm.bootbase.kit;

import cn.hutool.core.thread.ThreadUtil;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * Created by yunqisong on 2017/12/25/025.
 */
@Slf4j
public class OssUtil {
  public static int RETRY = 3; // 操作重试次数


  private String endpoint;
  private String accessKeyId;
  private String accessKeySecret;
  private String defaultBucket = null;

  private OSSClient ossClient = null;

  public static OssUtil me = new OssUtil();

  private OssUtil() {
  }

  public static boolean retryOnException(int retryLimit, Callable<Boolean> retryCallable) {

    Boolean result = null;

    for (int i = 0; i < retryLimit; i++) {
      try {
        result = retryCallable.call();
      } catch (Exception e) {
        log.error("Error when try on {} times", i + 1, e);
      }
      if (result != null && result.booleanValue()) {
        return true;
      } else if (i == retryLimit - 1) {
        log.error("After try {} times, it still fail", retryLimit);
      }
    }

    return false;
  }

  public OssUtil(OSSClient ossClient) {
    this.ossClient = ossClient;
  }

  public OssUtil setOssClient(String endpoint,
                              String accessKeyId,
                              String accessKeySecret,
                              String defaultBucket) {
    this.endpoint = endpoint;
    this.accessKeyId = accessKeyId;
    this.accessKeySecret = accessKeySecret;
    this.defaultBucket = defaultBucket;
    return setOssClient();
  }

  public OssUtil setOssClient() {
    this.ossClient = new OSSClient(this.endpoint,
      new DefaultCredentialProvider(this.accessKeyId, this.accessKeySecret),
      new ClientConfiguration()); // TODO: 看有哪些客户端配置可以使用

    if(this.defaultBucket != null && !this.ossClient.doesBucketExist(this.defaultBucket)) {
      Bucket bucket = this.ossClient.createBucket(this.defaultBucket);
      log.info("Default bucket {} is created", bucket);
    }
    return me;
  }

  public OssUtil setOssClient(OSSClient ossClient) {
    this.ossClient = ossClient;
    return me;
  }

  public OssUtil asyncUploadLocal(final String filePath, final String key) {
    assert defaultBucket != null;
    this.asyncUploadLocal(filePath, defaultBucket, key);
    return me;
  }

  public OssUtil asyncUploadLocal(final String filePath, final String bucket, final String key) {
    ThreadUtil.execute(() -> {
      uploadLocal(filePath, bucket, key);
    });
    return me;
  }

  public boolean uploadLocal(String filePath, String key) {
    assert defaultBucket != null;
    return this.uploadLocal(filePath, defaultBucket, key);
  }

  /**
   * 上传文件
   *
   * @param filePath
   * @param bucket
   * @param key
   */
  public boolean uploadLocal(String filePath, String bucket, String key) {
    log.debug("Uploading file {} into {} as {} ...", filePath, bucket, key);

    boolean success = retryOnException(RETRY, () -> {
      PutObjectResult result = ossClient.putObject(bucket, key, new File(filePath));
      return result != null;
    });

    if (success) {
      log.debug("Uploaded success !!!");
    } else {
      log.error("Upload fail !!!");
    }
    return success;
  }


  public boolean downloadLocal(String filePath, String key) {
    assert defaultBucket != null;
    return this.downloadLocal(filePath, defaultBucket, key);
  }

  public boolean downloadLocal(String filePath, String bucket, String key) {
    log.debug("Downloading {} from {} as file {} ...", key, bucket, filePath);

    boolean success = retryOnException(RETRY, () -> {
      ObjectMetadata result = ossClient.getObject(new GetObjectRequest(bucket, key), new File(filePath));
      return result != null;
    });

    if (success) {
      log.debug("Downloaded success !!!");
    } else {
      log.error("Download fail !!!");
    }
    return success;
  }

  public void shutdownClient() {
    if (this.ossClient != null) {
      log.debug("Shutdown OSS client ...");
      ossClient.shutdown();
    }
  }

  public void asyncDelete(final String bucket, final String key) {
    ThreadUtil.execute(() -> {
      delete(bucket, key);
    });
  }

  public boolean delete(String bucket, String key) {

    log.debug("Deleting {} from {}  ...", key, bucket);

    boolean success = retryOnException(RETRY, () -> {
      ossClient.deleteObject(bucket, key);
      return true;
    });

    if (success) {
      log.debug("Deleted success !!!");
    } else {
      log.error("Delete fail !!!");
    }

    return success;
  }

  public boolean copyRemoteFile(String bucket, String srcPath, String destPath) {
    log.debug("Copy {} from {} in bucket  ...", srcPath, destPath, bucket);

    boolean success = retryOnException(RETRY, () -> {
      ossClient.copyObject(bucket, srcPath, bucket, destPath);
      return true;
    });

    if (success) {
      log.debug("Copied success !!!");
    } else {
      log.error("Copy fail !!!");
    }

    return success;
  }
}
