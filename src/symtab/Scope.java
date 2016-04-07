package symtab;

import java.util.HashMap;

import japa.parser.ast.stmt.Statement;

public interface Scope {
	public String getScopeName();
	public Scope getEnclosingScope();
	public void define(Symbol symbol);
	public void defineYield(String methodName, Statement stmt);
	public Statement resolveYield(String methodName);
	public Symbol resolve(String name);
	public Symbol resolveLocal(String name);
}
