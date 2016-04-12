package symtab;

import java.util.HashMap;

import japa.parser.ast.stmt.Statement;
public class ScopedSymbol extends Symbol implements Scope {
	
	protected Scope enclosingScope = null;
	// Key: Name; Value: Symbol
	protected HashMap<String,Symbol> symbols = new HashMap<String,Symbol>();
	//Key: Method Name; Value: YieldBlock
	private HashMap<String,Statement> yieldBlocks = new HashMap<String,Statement>(); 

	public ScopedSymbol(String name, Type type, Scope enclosingScope) {
		super(name, type);
		this.enclosingScope = enclosingScope;
	}

	@Override
	public String getScopeName() {
		return this.name;
	}

	@Override
	public Scope getEnclosingScope() {
		return this.enclosingScope;
	}

	@Override
	public void define(Symbol symbol) {
		String name = symbol.getName();
		symbols.put(name, symbol);
	}

	@Override
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
	public void defineYield(String methodName, Statement stmt){
		this.yieldBlocks.put(methodName, stmt );
	}
	
	@Override
	public Statement resolveYield(String methodName) {
		return this.yieldBlocks.get(methodName);
	}

	@Override
	public Symbol resolveLocal(String name) {
		Symbol s = symbols.get(name);
			return s;
	}


}
