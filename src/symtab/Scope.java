package symtab;

import java.util.HashMap;

public interface Scope {
	public String getScopeName();
	public Scope getEnclosingScope();
	public void define(Symbol symbol);
	public Symbol resolve(String name);
	public HashMap<String,Symbol> getSymbols();
	public Symbol resolveForAll(String name);
}
