package com.mainline.magic.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsuranceTriggerListner implements TriggerListener {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "InsuranceTriggerListner";
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/**
	 *	작업 완료 
	 *	MCP_TERMS_JOB 테이블에 완료 상태로 업데이트 해준다.
	 */
	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			log.info( " triggerComplete  : "+ context.getJobDetail().getJobDataMap().getString("jobInfo"));
		
	}



}
