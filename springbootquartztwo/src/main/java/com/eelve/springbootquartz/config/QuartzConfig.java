package com.eelve.springbootquartz.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.zaxxer.hikari.HikariDataSource;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author lbt
 * @date 2020/1/2
 */
@Configuration
public class QuartzConfig {

    /**
     * 1.通过name+group获取唯一的jobKey;2.通过groupname来获取其下的所有jobkey
     */
    private final static String GROUP_NAME = "QuartzJobGroups";

    @Autowired
    private QuartzProperty quartzProperty;

    /**
     * 设置属性
     *
     * @return
     */
    private Properties quartzProperties() {
        Properties prop = new Properties();
        // 调度标识名 集群中每一个实例都必须使用相同的名称
        prop.put("quartz.scheduler.instanceName", quartzProperty.getInstanceName());
        // ID设置为自动获取 每一个必须不同
        prop.put("org.quartz.scheduler.instanceId", quartzProperty.getInstanceId());
        // 禁用quartz软件更新
        prop.put("org.quartz.scheduler.skipUpdateCheck", quartzProperty.getSkipUpdateCheck());
        prop.put("org.quartz.scheduler.jmx.export", quartzProperty.getJmxExport());
        // 数据库代理类，一般org.quartz.impl.jdbcjobstore.StdJDBCDelegate可以满足大部分数据库
        prop.put("org.quartz.jobStore.driverDelegateClass", quartzProperty.getDriverDelegateClass());
        // 数据保存方式为数据库持久化
        prop.put("org.quartz.jobStore.class", quartzProperty.getJobStoreClass());
        // 数据库别名 随便取
        prop.put("org.quartz.jobStore.dataSource", quartzProperty.getDataSource());
        //prop.put("org.quartz.jobStore.dataSource", "myDS");
        // 表的前缀，默认QRTZ_
        prop.put("org.quartz.jobStore.tablePrefix", quartzProperty.getTablePrefix());
        // 是否加入集群
        prop.put("org.quartz.jobStore.isClustered", quartzProperty.getIsClustered());

        // 调度实例失效的检查时间间隔
        prop.put("org.quartz.jobStore.clusterCheckinInterval", quartzProperty.getClusterCheckinInterval());
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", quartzProperty.getMaxMisfiresToHandleAtTime());
        // 信息保存时间 ms 默认值60秒
        prop.put("org.quartz.jobStore.misfireThreshold", quartzProperty.getMisfireThreshold());
        prop.put("org.quartz.jobStore.txIsolationLevelSerializable", quartzProperty.getTxIsolationLevelSerializable());
        prop.put("org.quartz.jobStore.selectWithLockSQL", quartzProperty.getSelectWithLockSql());
        // 程池的实现类（一般使用SimpleThreadPool即可满足几乎所有用户的需求）
        prop.put("org.quartz.threadPool.class", quartzProperty.getThreadPoolClass());
        // 定线程数，至少为1（无默认值）(一般设置为1-100之间的整数合适)
        prop.put("org.quartz.threadPool.threadCount", quartzProperty.getThreadCount());
        // 设置线程的优先级（最大为java.lang.Thread.MAX_PRIORITY 10，最小为Thread.MIN_PRIORITY 1，默认为5）
        prop.put("org.quartz.threadPool.threadPriority", quartzProperty.getThreadPriority());
        prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", quartzProperty.getInitializingThread());

        prop.put("org.quartz.plugin.triggHistory.class", quartzProperty.getTriggHistoryClass());
        prop.put("org.quartz.plugin.shutdownhook.class", quartzProperty.getShutdownHookClass());
        prop.put("org.quartz.plugin.shutdownhook.cleanShutdown", quartzProperty.getCleanShutdown());

        //数据源
        /*prop.put("org.quartz.dataSource.myDS.driver", quartzProperty.getMyDsDriver());
        prop.put("org.quartz.dataSource.myDS.URL", quartzProperty.getMyDsUrl());
        prop.put("org.quartz.dataSource.myDS.user", quartzProperty.getMyDsUser());
        prop.put("org.quartz.dataSource.myDS.password", quartzProperty.getMyDsPassword());
        prop.put("org.quartz.dataSource.myDS.maxConnections", quartzProperty.getMaxConnections());*/
        //#自定义连接池
        //org.quartz.dataSource.myDS.connectionProvider.class=com.poly.pay.schedule.DruidConnectionProvider

        return prop;
    }

    /**
     * 数据源
     *
     * @return
     */
    @Bean
    public HikariDataSource createDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(quartzProperty.getMyDsUrl());
        dataSource.setDriverClassName(quartzProperty.getMyDsDriver());
        dataSource.setUsername(quartzProperty.getMyDsUser());
        dataSource.setPassword(quartzProperty.getMyDsPassword());
        dataSource.setMaximumPoolSize(Integer.parseInt(quartzProperty.getMaxConnections()));
        return dataSource;
    }


    /**
     * 创建触发器工厂
     *
     * @param jobDetail
     * @param cronExpression
     * @return
     */
    private static CronTriggerFactoryBean cronTriggerFactoryBean(JobDetail jobDetail, String cronExpression) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        return factoryBean;
    }


    /**
     * 调度工厂
     * 此处配置需要调度的触发器 例如 executeJobTrigger
     *
     * @param executeJobTrigger
     * @return
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("executeJobTrigger") Trigger executeJobTrigger) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        //用于quartz集群,QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //用于quartz集群,加载quartz数据源
        //factory.setDataSource(dataSource);
        //QuartzScheduler 延时启动，应用启动完10秒后 QuartzScheduler 再启动
        //factory.setStartupDelay(10);
        //用于quartz集群,加载quartz数据源配置
        factory.setAutoStartup(true);
        factory.setQuartzProperties(quartzProperties());
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        factory.setDataSource(createDataSource());
        //注册触发器
        Trigger[] triggers = {executeJobTrigger};
        factory.setTriggers(triggers);

        return factory;
    }


    /**
     * 加载触发器
     * <p>
     * 新建触发器进行job 的调度  例如 executeJobDetail
     *
     * @param jobDetail
     * @return
     */
    @Bean(name = "executeJobTrigger")
    public CronTriggerFactoryBean executeJobTrigger(@Qualifier("executeJobDetail") JobDetail jobDetail) {
        return cronTriggerFactoryBean(jobDetail, "0 0/10 22 ? * TUE,THU,SAT");
    }


    /**
     * 加载job
     * <p>
     * 新建job 类用来代理
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean executeJobDetail() {
        return createJobDetail();
    }


    /**
     * 执行规则job工厂
     * <p>
     * 配置job 类中需要定时执行的 方法  execute
     *
     * @return
     */
    private static JobDetailFactoryBean createJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(InvokingJobDetailFactory.class);
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        factoryBean.setGroup(QuartzConfig.GROUP_NAME);
        Map<String, String> map = new HashMap<>();
        map.put("targetMethod", "execute");
        map.put("targetObject", "executeJob");
        factoryBean.setJobDataAsMap(map);
        return factoryBean;
    }

}
