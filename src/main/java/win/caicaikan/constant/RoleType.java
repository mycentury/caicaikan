package win.caicaikan.constant;

public enum RoleType {
	ADMIN("A"),
	GUEST("G"),
	USER("U"),
	VISITOR("V");
	private String code;

	private RoleType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
