package com.hoob.search.model;

import com.hoob.search.vo.BaseVO;

public class OverLoadItems implements BaseVO {

	/**
	 * 序列ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 百分比处理
	 */
	private int rate = 100;

	/**
	 * 线程内存使用率
	 */
	private Double proMemory;

	/**
	 * 进程并发数
	 */
	private int proConcurrency;

	/**
	 * 系统内存使用率
	 */
	private Double sysMemory;

	/**
	 * 系统CPU使用率
	 */
	private Double sysCPU;

	public Double getProMemory() {
		return proMemory * rate;
	}

	public void setProMemory(Double proMemory) {
		this.proMemory = proMemory;
	}

	public int getProConcurrency() {
		return proConcurrency;
	}

	public void setProConcurrency(int proConcurrency) {
		this.proConcurrency = proConcurrency;
	}

	public Double getSysMemory() {
		return sysMemory;
	}

	public void setSysMemory(Double sysMemory) {
		this.sysMemory = sysMemory;
	}

	public Double getSysCPU() {
		return sysCPU * rate;
	}

	public void setSysCPU(Double sysCPU) {
		this.sysCPU = sysCPU;
	}

	@Override
	public String toString() {
		return "OverLoadItems [" + "proMemory=" + proMemory + ", proConcurrency=" + proConcurrency + ", sysMemory="
				+ sysMemory + ", sysCPU=" + sysCPU + "]";
	}

}
