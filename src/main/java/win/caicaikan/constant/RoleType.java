package win.caicaikan.constant;

public enum RoleType {
	ADMIN("A",10),
	GUEST("G",5),
	USER("U",3),
	VISITOR("V",1);
	private String code;
	private int priority;

	private RoleType(String code,int priority) {
		this.code = code;
		this.priority = priority;
	}

	public String getCode() {
		return code;
	}

	public int getPriority() {
		return priority;
	}
}
