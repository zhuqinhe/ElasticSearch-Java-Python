package com.hoob.search.server.impl;

import java.util.Date;

import com.hoob.search.common.SigarCollection;
import com.hoob.search.model.OverLoadItems;
import com.hoob.search.server.IParamConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hoob.search.redis.utils.CommonConsts;
import com.hoob.search.redis.utils.JsonUtil;
import com.hoob.search.utils.StringUtil;


@Component("overLoadCheckTimer")
public class OverLoadCheckTimer implements ApplicationContextAware {
	/**
	 * 日志
	 */
	private final Logger LOGGER = LogManager.getLogger("SysInfoLog");

	/**
	 * 参数配置处理类
	 */
	@Autowired
	private IParamConfigService paramConfigService;

	/**
	 * 上下文对象
	 */
	private ApplicationContext applicationContext;

	/**
	 * 过载sigar采集对象
	 */
	private SigarCollection sigarCollection = new SigarCollection();

	/**
	 * 定时采集任务状态
	 */
	private static final String COLL_TIMER_STATUS = "overLoad_TimerStatus";

	/**
	 * 单机模式，避免多次触发手工采集
	 */
	private static boolean isRun;

	/**
	 * 过载采集间隔时间，用于过载后加快检测的频率
	 */
	private long overLoadCollMilli = CommonConsts.NUMBER_ONE_MINUTE;

	/**
	 * 触发马上执行
	 * 
	 * @param id
	 *            可配置的访问路径
	 * @return 成功失败状态
	 * @see
	 */
	// public boolean reCollection(String id)
	// {
	// // 对比配置路径是否和实际一致
	// String code =
	// paramConfigService.getParamConfigVal("overLoad_reCollectionCode");
	//
	// if (!StringUtil.isEmpty(id) && id.equals(code))
	// {
	// cheOverLoad();
	// return true;
	// }
	//
	// return false;
	// }

	/**
	 * 触发定时任务
	 * 
	 * @see
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	// @Scheduled(fixedRate = 5000)
	public void cheOverLoad() {
		if (isRun()) {
			return;
		}

		setRun(true);

		// 获取定时任务是否开启
		String cdmTimer = paramConfigService.getParamConfigVal("overLoad_Timer");

		// 0为开启，1为关闭
		if (CommonConsts.NUMBER_ZERO_STR.equals(cdmTimer)) {
			// 获取采集定时任务状态
			String cdmCollectionTimer = paramConfigService.getParamConfigVal(COLL_TIMER_STATUS);

			// 采集状态锁：0准备，1运行中，3单机（单机不用处理该锁机制）
			if (CommonConsts.NUMBER_ZERO_STR.equals(cdmCollectionTimer)
					|| CommonConsts.NUMBER_THREE_STR.equals(cdmCollectionTimer)) {
				LOGGER.info("begin Collection Timer :" + new Date());

				// 将运行状态修改为运行中
				if (CommonConsts.NUMBER_ZERO_STR.equals(cdmCollectionTimer)) {
					paramConfigService.updateParamConfig(COLL_TIMER_STATUS, CommonConsts.NUMBER_ONE_STR);
				}

				// 进程内存使用率（JAVA监测老年代）
				String proMemory = paramConfigService.getParamConfigVal("overLoad_proMemory");

				// 进程并发请求数
				String proConcurrency = paramConfigService.getParamConfigVal("overLoad_proConcurrency");

				// 操作系统内存使用率
				String sysMemory = paramConfigService.getParamConfigVal("overLoad_sysMemory");

				// 操作系统CPU使用率
				String sysCPU = paramConfigService.getParamConfigVal("overLoad_sysCPU");

				// 采集次数
				String collectionCount = paramConfigService.getParamConfigVal("overLoad_collectionCount");

				// 采集间隔时间
				String collectionMilli = paramConfigService.getParamConfigVal("overLoad_collectionMilli");

				// 过载后采集间隔时间
				String overLoadCollMilliStr = paramConfigService.getParamConfigVal("overLoad_olCollectionMilli");

				// 批量邮件
				// String mailForm =
				// paramConfigService.getParamConfigVal("mailForm");

				// 批量邮件
				// String mailTo =
				// paramConfigService.getParamConfigVal("mailTo");

				LOGGER.info("Collection Timer Config Set Info：===================================================");
				LOGGER.info("proMemory：" + proMemory);
				LOGGER.info("proConcurrency：" + proConcurrency);
				LOGGER.info("sysMemory：" + sysMemory);
				LOGGER.info("sysCPU：" + sysCPU);
				LOGGER.info("collectionCount：" + collectionCount);
				LOGGER.info("collectionMilli：" + collectionMilli);
				LOGGER.info("overLoadCollMilli：" + overLoadCollMilliStr);
				// LOGGER.info("mailForm：" + mailForm);
				// LOGGER.info("mailTo：" + mailTo);
				LOGGER.info("====================================================================");

				try {

					// 采集次数，默认5次
					int cCount = null != collectionCount ? Integer.parseInt(collectionCount) : CommonConsts.NUMBER_FIVE;

					// 每次间隔多久默认 5秒
					int cMilli = null != collectionMilli ? Integer.parseInt(collectionMilli)
							: CommonConsts.NUMBER_FIVE_Q;

					overLoadCollMilli = null != overLoadCollMilliStr ? Long.parseLong(overLoadCollMilliStr)
							: CommonConsts.NUMBER_ONE_MINUTE;

					// 进程内存使用率（JAVA监测老年代）范围
					String[] proMemoryBetw = proMemory.split(CommonConsts.COMMA);

					// 进程并发请求数
					String[] proConcurrencyBetw = proConcurrency.split(CommonConsts.COMMA);

					// 操作系统内存使用率
					String[] sysMemoryBetw = sysMemory.split(CommonConsts.COMMA);

					// 操作系统CPU使用率
					String[] sysCPUBetw = sysCPU.split(CommonConsts.COMMA);

					// 启动过载检测
					if(0 < cCount){
						startCheckOverLoad(proMemoryBetw, proConcurrencyBetw, sysMemoryBetw, sysCPUBetw, cCount, cMilli);
					}
				} catch (Exception e) {
					resetLoad();
					LOGGER.error("collection Exception:" + e);
				} finally {
					LOGGER.info("end Collenction Timer:" + new Date());

					// 采集完成重置标识
					setRun(false);

					if (CommonConsts.NUMBER_ZERO_STR.equals(cdmCollectionTimer)) {
						paramConfigService.updateParamConfig(COLL_TIMER_STATUS, CommonConsts.NUMBER_ZERO_STR);
					}
				}
			} else {
				LOGGER.info("Timer Task Excuteing...... :" + cdmCollectionTimer);
			}
		} else {
			resetLoad();
			LOGGER.info("Timer No Open：" + cdmTimer);
		}

		// 采集完成重置标识
		setRun(false);
	}

	/**
	 * 采集对比处理
	 * 
	 * @param proMemory
	 *            进程内存阀值
	 * @param proConcurrency
	 *            并发送阀值
	 * @param sysMemory
	 *            系统内存阀值
	 * @param sysCPU
	 *            系统cpu阀值
	 * @param cCount
	 *            采集次数
	 * @param cMilli
	 *            间隔时间
	 * @see
	 */
	private void startCheckOverLoad(String[] proMemory, String[] proConcurrency, String[] sysMemory, String[] sysCPU,
			int cCount, int cMilli) {
		// 采集指标数据
		OverLoadItems overLoadItems = getCollectionData(cCount, cMilli);

		// 对比指标数据
		if (!StringUtil.isArrayEmpty(proMemory, CommonConsts.NUMBER_TWO)) {
			// 先判断是否达到过载预警
			if (overLaodCompare(proMemory[CommonConsts.NUMBER_ZERO], proConcurrency[CommonConsts.NUMBER_ZERO],
					sysMemory[CommonConsts.NUMBER_ZERO], sysCPU[CommonConsts.NUMBER_ZERO], overLoadItems)) {

				// 组装指标值
				StringBuilder sb = new StringBuilder("Threshold value:{");
				sb.append("proMemory:").append(proMemory[CommonConsts.NUMBER_ZERO]).append(",")
						.append(proMemory[CommonConsts.NUMBER_ONE]).append(",proConcurrency:")
						.append(proConcurrency[CommonConsts.NUMBER_ZERO]).append(",")
						.append(proConcurrency[CommonConsts.NUMBER_ONE]).append(",sysMemory:")
						.append(sysMemory[CommonConsts.NUMBER_ZERO]).append(",")
						.append(sysMemory[CommonConsts.NUMBER_ONE]).append(",sysCPU:")
						.append(sysCPU[CommonConsts.NUMBER_ZERO]).append(",").append(sysCPU[CommonConsts.NUMBER_ONE])
						.append("}\t\n").append("Actual value:");
				// 发送告警邮件和记录日志
				try {
					sb.append(JsonUtil.writeObject2JSON(overLoadItems));

				} catch (JsonProcessingException e) {
					LOGGER.error(e);
				}

				// 再判断是否已经过载
				if (overLaodCompare(proMemory[CommonConsts.NUMBER_ONE], proConcurrency[CommonConsts.NUMBER_ONE],
						sysMemory[CommonConsts.NUMBER_ONE], sysCPU[CommonConsts.NUMBER_ONE], overLoadItems)) {
					// 设置过载标识
					OverloadProtection.setOverLoad(true);
					LOGGER.warn("系统过载.....");
					LOGGER.warn(sb.toString());

					try {
						// 如果已过载，就每隔1分钟（默认）检测一次
						Thread.sleep(overLoadCollMilli);
					} catch (Exception e) {
						LOGGER.error(e);
					}

					// 马上启动过载密集检测
					LOGGER.info("马上启动过载密集检测");
					startCheckOverLoad(proMemory, proConcurrency, sysMemory, sysCPU, cCount, cMilli);
				} else {
					// 如果没有过载，修改标识
					OverloadProtection.setOverLoad(false);

					LOGGER.warn(sb.toString());
				}
			} else {
				// 如果没有过载，修改标识
				OverloadProtection.setOverLoad(false);
			}
		} else {
			// 如果没有过载，修改标识
			OverloadProtection.setOverLoad(false);
		}
	}

	/**
	 * 过载判断
	 * 
	 * @param proMemory
	 *            进程内存阀值
	 * @param proConcurrency
	 *            并发送阀值
	 * @param sysMemory
	 *            系统内存阀值
	 * @param sysCPU
	 *            系统cpu阀值
	 * @param overLoadItems
	 *            实际数据
	 * @return 返回对比结果
	 * @see
	 */
	private boolean overLaodCompare(String proMemory, String proConcurrency, String sysMemory, String sysCPU,
			OverLoadItems overLoadItems) {
		// 对比进程内存
		if (StringUtil.compareDouble(Double.parseDouble(proMemory), overLoadItems.getProMemory())) {
			return true;
		}
		// 对比并数
		else if (StringUtil.compareDouble(Double.parseDouble(proConcurrency), overLoadItems.getProConcurrency())) {
			return true;
		}
		// 对比系统内存
		else if (StringUtil.compareDouble(Double.parseDouble(sysMemory), overLoadItems.getSysMemory())) {
			return true;
		}
		// 对比系统CPU
		else if (StringUtil.compareDouble(Double.parseDouble(sysCPU), overLoadItems.getSysCPU())) {
			return true;
		}
		// 如果所有指标都正常，返回没有负载
		else {
			return false;
		}
	}

	/**
	 * 获取采集的指标数据
	 * 
	 * @param cCount
	 *            采集次数
	 * @param cMilli
	 *            每次间隔毫秒
	 * @return 采集结果
	 * @throws Exception
	 *             异常
	 * @see
	 */
	private OverLoadItems getCollectionData(int cCount, long cMilli) {
		OverLoadItems olItems = new OverLoadItems();
		double runtimeMemoryRate = 0L;
		double systemMemoryRate = 0L;
		double cpuRate = 0L;
		int proConcurrency = 0;

		for (int i = 0; i < cCount; i++) {
			// 进程内存使用率
			runtimeMemoryRate += sigarCollection.getRuntimeMemoryRate();

			// 系统内存使用率
			systemMemoryRate += sigarCollection.getSystemMemoryRate();

			// 系统CPU使用率
			cpuRate += sigarCollection.getCPURate();

			// 当前并发数
			proConcurrency += OverloadProtection.getProConcurrency().get();

			try {
				// 间隔多久再采集
				Thread.sleep(cMilli);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}

		// 进程内存使用率
		olItems.setProMemory(runtimeMemoryRate / cCount);

		// 系统内存使用率
		olItems.setSysMemory(systemMemoryRate / cCount);

		// 系统COU使用率
		olItems.setSysCPU(cpuRate / cCount);

		// 当前并发数
		olItems.setProConcurrency(proConcurrency / cCount);
		LOGGER.warn(olItems);
		return olItems;
	}

	/**
	 * 获取当前bean
	 * 
	 * @param name
	 *            bean名称
	 * @return 对象实例
	 * @see
	 */
	public Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	// 重置过载
	private void resetLoad() {
		if (OverloadProtection.isOverLoad()) {
			OverloadProtection.setOverLoad(false);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public SigarCollection getSigarCollection() {
		return sigarCollection;
	}

	public void setSigarCollection(SigarCollection sigarCollection) {
		this.sigarCollection = sigarCollection;
	}

	public static boolean isRun() {
		return isRun;
	}

	public static void setRun(boolean isRun) {
		OverLoadCheckTimer.isRun = isRun;
	}
}
