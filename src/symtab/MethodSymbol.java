package symtab;

public class MethodSymbol extends ScopedSymbol {

	public MethodSymbol(String name, Scope enScope) {
		super(name, null, enScope);
	}

}
