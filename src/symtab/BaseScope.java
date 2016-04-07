package symtab;

import java.util.HashMap;

import japa.parser.ast.stmt.Statement;


public class BaseScope implements Scope  {
	
	private Scope enclosingScope;
	// Key: Name; Value: Symbol
	private HashMap<String,Symbol> symbols = new HashMap<String,Symbol>();
	//Key: Method Name; Value: YieldBlock
	private HashMap<String,Statement> yieldBlocks = new HashMap<String,Statement>(); 


	public BaseScope(){
		define(new BuiltInTypeSymbol("int"));
		define(new BuiltInTypeSymbol("boolean"));
		define(new BuiltInTypeSymbol("false", "boolean"));
		define(new BuiltInTypeSymbol("true", "boolean"));
		define(new BuiltInTypeSymbol("boolean"));
		define(new BuiltInTypeSymbol("String[]","[]"));
		define(new BuiltInTypeSymbol("int[]","[]"));
		define(new BuiltInTypeSymbol("float"));
		define(new BuiltInTypeSymbol("double"));
		define(new BuiltInTypeSymbol("byte"));
		define(new BuiltInTypeSymbol("char"));
		define(new BuiltInTypeSymbol("null"));
		define(new BuiltInTypeSymbol("void"));
		
		define(new ClassSymbol("String"));
		define(new MethodSymbol("println", null));
		
		
	}
	
	public String getScopeName() {
		return "Base Scope";
	}

	public void define(Symbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}
	
	@Override
	public void defineYield(String methodName, Statement stmt){
		this.yieldBlocks.put(methodName, stmt );
	}
	
	@Override
	public Statement resolveYield(String methodName) {
		return this.yieldBlocks.get(methodName);
	}

	public Symbol resolve(String name) {
			// if the symbol exists in the current scope, return it
			Symbol s = symbols.get(name);
			if (s != null)
				return s;
			
			// otherwise look in the enclosing scope, if there is one
			if (enclosingScope != null)
				return enclosingScope.resolve(name);
			
			// otherwise it doesn't exist
			return null;
	}
	

	@Override
	public Scope getEnclosingScope() {
		return this.enclosingScope;
	}
	
	public HashMap<String,Symbol> getSymbols(){
		return symbols;
	}
	
	public void setEnclosingScope(Scope scope){
		this.enclosingScope = scope;
	}

	@Override
	public Symbol resolveLocal(String name) {
		Symbol s = symbols.get(name);
			return s;
	}



}
