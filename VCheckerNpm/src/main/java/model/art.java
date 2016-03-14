package model;

public class art {
	String artifact;
	String version;
	
	art(){}

	public art(String artifact, String version) {
		super();
		this.artifact = artifact;
		this.version = version;
	}

	public String getArtifact() {
		return artifact;
	}

	public void setArtifact(String artifact) {
		this.artifact = artifact;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	};	
	
}
