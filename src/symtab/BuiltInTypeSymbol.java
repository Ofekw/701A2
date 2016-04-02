package symtab;

public class BuiltInTypeSymbol extends Symbol implements Type {
	
	public BuiltInTypeSymbol(String name) {
		super(name, new ObjectType(name));
	}
	
	public BuiltInTypeSymbol(String name, String type) {
		super(name, new ObjectType(type));
	}
	
}
