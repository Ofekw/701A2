package symtab;

import java.util.HashMap;

public class ScopedSymbol extends Symbol implements Scope {

	public ScopedSymbol(String name, Type type) {
		super(name, type);
	}

	@Override
	public String getScopeName() {
		return null;
	}

	@Override
	public Scope getEnclosingScope() {
		return null;
	}

	@Override
	public void define(Symbol symbol) {
		
	}

	@Override
	public Symbol resolve(String name) {
		return null;
	}

	@Override
	public HashMap<String, Symbol> getSymbols() {
		return this.getSymbols();
	}

	@Override
	public Symbol resolveForAll(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
