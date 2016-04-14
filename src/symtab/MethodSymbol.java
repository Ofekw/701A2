package symtab;

import java.util.ArrayList;
import java.util.List;

import japa.parser.ast.body.Parameter;

public class MethodSymbol extends ScopedSymbol {

	private List<Parameter> methodParameter = new ArrayList<Parameter>();
	
	public MethodSymbol(String name, Scope enScope) {
		super(name, new ObjectType(name), enScope);
	}
	
	public MethodSymbol(String name, Type type, Scope enScope) {
		super(name, type, enScope);
	}
	
	public MethodSymbol(String name, Type type, Scope enScope, List<Parameter> param) {
		super(name, type, enScope);
		this.methodParameter = param;
	}
	
	
	public void setMethodParemters(List<Parameter> list){
		this.methodParameter = list;
	}

	public List<Parameter> getMethodParameter() {
		return methodParameter;
	}
	
	public void setType(Type type){
		this.type = type;
	}
}
