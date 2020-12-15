package com.ibm.cohort.engine;

import java.io.Serializable;

public class ServerInfo implements Serializable {
	private String tenantId;
	private String username;
	private String password;
	private String endpoint;

	public ServerInfo() { }

	public ServerInfo(String tenantId, String username, String password, String endpoint) {
		this.tenantId = tenantId;
		this.username = username;
		this.password = password;
		this.endpoint = endpoint;
	}

	public IBMFhirServerConfig toIbmServerConfig() {
		IBMFhirServerConfig config = new IBMFhirServerConfig();
		config.setTenantId(tenantId);
		config.setUser(username);
		config.setPassword(password);
		config.setEndpoint(endpoint);
		return config;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String toString() {
		return "ServerInfo{" +
				"tenantId='" + tenantId + '\'' +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", endpoint='" + endpoint + '\'' +
				'}';
	}
}
