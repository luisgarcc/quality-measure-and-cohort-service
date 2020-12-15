package com.ibm.cohort.engine;

public class ExecutionResult {
	private String libraryId;
	private String patientId;
	private boolean result;

	public ExecutionResult() { }

	public String getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(String libraryId) {
		this.libraryId = libraryId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ExecutionResult{" +
				"libraryId='" + libraryId + '\'' +
				", patientId='" + patientId + '\'' +
				", result=" + result +
				'}';
	}
}
