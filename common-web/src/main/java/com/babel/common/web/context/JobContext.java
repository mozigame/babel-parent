package com.babel.common.web.context;

import java.util.HashMap;
import java.util.Map;

public class JobContext {
	private static final ThreadLocal<JobContext> LOCAL = new ThreadLocal() {
		protected JobContext initialValue() {
			return new JobContext();
		}
	};
	private final Map<String, Object> attachments = new HashMap<>();
	
	public final static String JOB_TYPE_CONTROLLER="controller";
	public final static String JOB_TYPE_LOG_AUDIT="logAudit";
	
	
	public static JobContext getContext() {
		return ((JobContext) LOCAL.get());
	}

	public static void removeContext() {
		LOCAL.remove();
	}
	
	public Object getAttachment(String key) {
		return  this.attachments.get(key);
	}

	public JobContext setAttachment(String key, Object value) {
		if (value == null)
			this.attachments.remove(key);
		else {
			this.attachments.put(key, value);
		}
		return this;
	}

	public JobContext removeAttachment(String key) {
		this.attachments.remove(key);
		return this;
	}

	public Map<String, Object> getAttachments() {
		return this.attachments;
	}

	public JobContext setAttachments(Map<String, Object> attachment) {
		this.attachments.clear();
		if ((attachment != null) && (attachment.size() > 0)) {
			this.attachments.putAll(attachment);
		}
		return this;
	}

	public void clearAttachments() {
		this.attachments.clear();
	}
}
