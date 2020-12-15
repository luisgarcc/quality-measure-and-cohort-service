package com.ibm.cohort.engine;

public class LibraryExecution {
	private LibraryInfo library;
	private String patientId;

	public LibraryExecution() { }

	public LibraryExecution(LibraryInfo library, String patientId) {
		this.library = library;
		this.patientId = patientId;
	}

	public LibraryInfo getLibrary() {
		return library;
	}

	public void setLibrary(LibraryInfo library) {
		this.library = library;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	@Override
	public String toString() {
		return "LibraryExecution{" +
				"library=" + library +
				", patientId='" + patientId + '\'' +
				'}';
	}
}
