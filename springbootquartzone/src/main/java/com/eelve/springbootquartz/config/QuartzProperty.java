package com.eelve.springbootquartz.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Data
public class QuartzProperty {
    @Value("${org.quartz.scheduler.instanceName}")
    private String instanceName;
    @Value("${org.quartz.scheduler.instanceId}")
    private String instanceId;
    @Value("${org.quartz.scheduler.skipUpdateCheck}")
    private String skipUpdateCheck;
    @Value("${org.quartz.scheduler.jmx.export}")
    private String jmxExport;
    @Value("${org.quartz.jobStore.driverDelegateClass}")
    private String driverDelegateClass;
    @Value("${org.quartz.jobStore.class}")
    private String jobStoreClass;
    @Value("${org.quartz.jobStore.dataSource}")
    private String dataSource;
    @Value("${org.quartz.jobStore.tablePrefix}")
    private String tablePrefix;
    @Value("${org.quartz.jobStore.isClustered}")
    private String isClustered;
    @Value("${org.quartz.jobStore.clusterCheckinInterval}")
    private String clusterCheckinInterval;
    @Value("${org.quartz.jobStore.maxMisfiresToHandleAtATime}")
    private String maxMisfiresToHandleAtTime;
    @Value("${org.quartz.jobStore.misfireThreshold}")
    private String misfireThreshold;
    @Value("${org.quartz.jobStore.txIsolationLevelSerializable}")
    private String txIsolationLevelSerializable;
    @Value("${org.quartz.jobStore.selectWithLockSQL}")
    private String selectWithLockSql;
    @Value("${org.quartz.threadPool.class}")
    private String threadPoolClass;
    @Value("${org.quartz.threadPool.threadCount}")
    private String threadCount;
    @Value("${org.quartz.threadPool.threadPriority}")
    private String threadPriority;
    @Value("${org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread}")
    private String initializingThread;
    @Value("${org.quartz.plugin.triggHistory.class}")
    private String triggHistoryClass;
    @Value("${org.quartz.plugin.shutdownhook.class}")
    private String shutdownHookClass;
    @Value("${org.quartz.plugin.shutdownhook.cleanShutdown}")
    private String cleanShutdown;
    @Value("${org.quartz.dataSource.myDS.driver}")
    private String myDsDriver;
    @Value("${org.quartz.dataSource.myDS.URL}")
    private String myDsUrl;
    @Value("${org.quartz.dataSource.myDS.user}")
    private String myDsUser;
    @Value("${org.quartz.dataSource.myDS.password}")
    private String myDsPassword;
    @Value("${org.quartz.dataSource.myDS.maxConnections}")
    private String maxConnections;
}
