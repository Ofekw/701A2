package symtab;

import java.util.HashMap;

public class GlobalScope extends BaseScope {
	

	public GlobalScope (Scope enclosingScope){
		this.setEnclosingScope(enclosingScope);
	}
	
	public String getScopeName() {
		return "Global Scope";
	}
	
}
