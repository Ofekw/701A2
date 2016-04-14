package symtab;

public abstract class Symbol {

	protected String name;
	protected Type type;
	protected int declerationLine = 0;
	protected int declerationColumn = 0;
	
	public int getDeclerationLine() {
		return declerationLine;
	}

	public void setDeclerationLine(int declerationLine) {
		this.declerationLine = declerationLine;
	}

	public int getDeclerationColumn() {
		return declerationColumn;
	}

	public void setDeclerationColumn(int declerationColumn) {
		this.declerationColumn = declerationColumn;
	}

	public Symbol(String name, Type type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType(){
		return type;
	}
}
