package symtab;

public class ObjectType implements Type {

	private String name;
	
	public ObjectType(String name){
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}

}
