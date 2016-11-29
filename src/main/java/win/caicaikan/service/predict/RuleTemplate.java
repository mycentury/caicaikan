package win.caicaikan.service.predict;

public abstract class RuleTemplate {
	abstract String doBefore();

	abstract String doAfter();

	abstract String run();

	abstract String getRuleId();
}
