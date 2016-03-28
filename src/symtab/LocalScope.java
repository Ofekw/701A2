package symtab;

import java.util.HashMap;

public class LocalScope extends BaseScope {
	
	public LocalScope (Scope enclosingScope){
		this.setEnclosingScope(enclosingScope);
	}
	
	
	public String getScopeName() {
		return "Local Scope";
	}

}
