package symtab;

import java.util.HashMap;
public class ScopedSymbol extends Symbol implements Scope {
	
	private HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();
	protected Scope enclosingScope = null;

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

}
