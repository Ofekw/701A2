package symtab;

import java.util.HashMap;

public class ClassSymbol extends ScopedSymbol implements Type {
	
	protected HashMap<String,Scope> yieldScopes = new HashMap<String,Scope>();

	public ClassSymbol(String name) {
		super(name, new ObjectType(name), null);
	}
	
	public ClassSymbol(String name, Scope scope) {
		super(name, new ObjectType(name), scope);
	}
	
	
	
	public ClassSymbol(String name, Type type) {
		super(name, type, null);
	}
	
	public void defineYieldScope(String methodName, Scope scope){
		this.yieldScopes.put(methodName, scope);
	}
	
	public Scope getYieldScope(String methodName){
		return yieldScopes.get(methodName);
	}


}
