
package com.hoob.search.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import com.hoob.search.utils.ObjectUtils;
import com.hoob.search.utils.OsCheck;


public class SigarCollection {
	/**
	 * 日志
	 */
	private static final Logger LOGGER = LogManager.getLogger("SysInfoLog");

	/**
	 * 初始化sigar对象
	 */
	private final Sigar sigar = initSigar();

	/**
	 * 获取当前进程内存使用率
	 * 
	 * @return 返回未做百分比处理的值
	 * @throws Exception
	 * @see
	 */
	public Double getRuntimeMemoryRate() {
		Double memoryRate = 0.0;
		try {
			if (ObjectUtils.isNotEmpty(Runtime.getRuntime())) {
				Runtime run = Runtime.getRuntime();
				Long max = run.maxMemory();
				LOGGER.info("getRuntimeMemory:=======================");
				LOGGER.info("maxMemory:" + max);
				Long total = run.totalMemory();
				LOGGER.info("totalMemory:" + total);
				Long free = run.freeMemory();
				LOGGER.info("freeMemory:" + free);
				Long usable = (total - free);
				memoryRate = usable.doubleValue() / max.doubleValue();
				LOGGER.info("RuntimeMemoryRate=(totalMemory-freeMemory)/maxMemory=" + memoryRate);
			}

		} catch (Exception e) {
			LOGGER.error(e);
		}

		return memoryRate;
	}

	/**
	 * 获取系统内存
	 * 
	 * @return 返回未做百分比处理的值
	 * @throws Exception
	 * @see
	 */
	public Double getSystemMemoryRate() {
		Double memoryRate = 0.0;
		try {
			if (ObjectUtils.isNotEmpty(sigar)) {
				if (ObjectUtils.isNotEmpty(sigar.getMem())) {
					Mem mem = sigar.getMem();
					LOGGER.info("getSystemMemory:=======================");
					Long total = mem.getTotal();
					LOGGER.info("getTotal:" + total);
					Long used = mem.getUsed();
					LOGGER.info("getUsed:" + used);
					Long free = mem.getFree();
					LOGGER.info("getFree:" + free);
					LOGGER.info("mem actualUsed：" + mem.getActualUsed() + " B");
					LOGGER.info("mem actualFree：" + mem.getActualFree() + " B");
					LOGGER.info("mem usedPercent：" + mem.getUsedPercent() + "%");
					LOGGER.info("mem freePercent：" + mem.getFreePercent() + "%");
					memoryRate = mem.getUsedPercent();
					LOGGER.info("SystemMemoryRate=used/total=" + used.doubleValue() / total.doubleValue());
				}
			}
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return memoryRate;
	}

	/**
	 * 获取CPU使用率
	 * 
	 * @return 返回未做百分比处理的值
	 * @throws Exception
	 * @see
	 */
	public Double getCPURate() {
		Double combined = 0.0;
		try {
			if (ObjectUtils.isNotEmpty(sigar)) {
				if (ObjectUtils.isNotEmpty(sigar.getCpuPerc())) {
					CpuPerc cpu = sigar.getCpuPerc();
					LOGGER.info("getCPURate:=======================");
					combined = cpu.getCombined();
					if (Double.isNaN(combined)) {
						Sigar sg = new Sigar();
						cpu = sg.getCpuPerc();
						combined = cpu.getCombined();
					}
					LOGGER.info("CPU总的使用率getCombined:" + CpuPerc.format(combined));
					LOGGER.info("CPU当前空闲率 getIdle:" + CpuPerc.format(cpu.getIdle()));
					LOGGER.info("CPU用户使用率:" + CpuPerc.format(cpu.getUser()));// 用户使用率
					LOGGER.info("CPU系统使用率:" + CpuPerc.format(cpu.getSys()));// 系统使用率
					LOGGER.info("CPU当前等待率:" + CpuPerc.format(cpu.getWait()));// 当前等待率
					LOGGER.info("CPU当前错误率:" + CpuPerc.format(cpu.getNice()));//
				}

			}
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return combined;
	}

	/**
	 * 初始化sigar对象
	 * 
	 * @return 返回对象
	 * @see
	 */
	private Sigar initSigar() {
		try {
			// 在java.library.path环境中添加配置所需配置文件所在目录
			String path = System.getProperty("java.library.path");

			// windows和linux在环境变量中有分号和引号的区别
			if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows) {
				String pathFile = Sigar.class.getClassLoader().getResource("").getPath();
				path += ";" + pathFile;
			} else {
				String pathFile = "/opt/fonsview/NE/search-server/search/";
				path += ":" + pathFile;
			}

			// 添加一层sigarUML，然后再追加到环境变量中去
			path += "/sigar";
			System.setProperty("java.library.path", path);
			return new Sigar();
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return new Sigar();
	}
}
