package com.ibm.cohort.engine;

public class LibraryInfo {
	private String id;
	private String name;
	private String version;

	public LibraryInfo() { }

	public LibraryInfo(String id, String name, String version) {
		this.id = id;
		this.name = name;
		this.version = version;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "LibraryInfo{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", version='" + version + '\'' +
				'}';
	}
}
