package com.hanson.foundation.timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hanson.foundation.tools.TimeAdminTools;

/**
 * 任务计时器，目前每天0凌晨执行一次
 * @author hanson
 *
 */
@Component("job_timer")
public class JobTimer {
	
	@Autowired
	private TimeAdminTools timeAdminTools;
	
	public void execute(){
		boolean result = true;
		try {
			this.timeAdminTools.updateGroupStatus();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
	}
}
