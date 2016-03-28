package symtab;

import java.util.HashMap;

public class BaseScope implements Scope  {
	
	private Scope enclosingScope;
	private HashMap<String,Symbol> symbols = new HashMap<String,Symbol>();


	public BaseScope(){
		define(new BuiltInTypeSymbol("int"));
		define(new BuiltInTypeSymbol("boolean"));
		define(new BuiltInTypeSymbol("float"));
		define(new BuiltInTypeSymbol("double"));
		define(new BuiltInTypeSymbol("byte"));
		define(new BuiltInTypeSymbol("char"));
		define(new BuiltInTypeSymbol("null"));
		
		define(new ClassSymbol("String"));
	}
	
	public String getScopeName() {
		return "Base Scope";
	}

	public void define(Symbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}

	public Symbol resolve(String name) {
		return symbols.get(name);
	}
	
	public Symbol resolveForAll(String name){
		Symbol symbol = symbols.get(name);
		while (symbol == null){
			Scope scope = getEnclosingScope();
			if (scope == null){
				return symbol;
			}else{
				symbol = scope.getSymbols().get(name);
			}
		}
		return symbol;
	}

	@Override
	public Scope getEnclosingScope() {
		return this.enclosingScope;
	}
	
	@Override
	public HashMap<String,Symbol> getSymbols(){
		return symbols;
	}
	
	public void setEnclosingScope(Scope scope){
		this.enclosingScope = scope;
	}

}
