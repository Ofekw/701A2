package symtab;

public class ClassSymbol extends ScopedSymbol implements Type {
	

	public ClassSymbol(String name) {
		super(name, null, null);
	}
	
	public ClassSymbol(String name, Type type) {
		super(name, type, null);
	}


}
