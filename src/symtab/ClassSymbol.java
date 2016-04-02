package symtab;

public class ClassSymbol extends ScopedSymbol implements Type {
	

	public ClassSymbol(String name) {
		super(name, new ObjectType(name), null);
	}
	
	public ClassSymbol(String name, Type type) {
		super(name, type, null);
	}


}
