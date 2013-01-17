package group150.model;

public enum Skin {
	DEFAULT_SKIN("defaultskin"),
	GREENLANDS("greenlands"),
	DARKSANDS("darksand"),
	LAVALAND("lavaland"),
	CYBOT("cybot"),
	INSANE("insane"),
	NOSKIN("noskin");

	Skin(String directory) {
		this.directory = directory;
	}

	private String directory;

	public String getDirectory() {
		return this.directory;
	}
}