package se701;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.visitor.DumpVisitor;
import japa.parser.ast.visitor.ResolvingVisitor;
import japa.parser.ast.visitor.ScopeVisitor;
import japa.parser.ast.visitor.DefinitionVisitor;

public class A2Compiler {
	
	/*
	 * This is the only method you should need to change inside this class. But do not modify the signature of the method! 
	 */
	public static void compile(File file) throws ParseException, FileNotFoundException {

		// parse the input, performs lexical and syntatic analysis
		JavaParser parser = new JavaParser(new FileReader(file));
		CompilationUnit ast = parser.CompilationUnit();
		
		//Create Scope/Typing Visitor
		ScopeVisitor scopeV = new ScopeVisitor();
		ast.accept(scopeV, null);
		
		// Create Definition Visitor
		DefinitionVisitor definitionV = new DefinitionVisitor();
		ast.accept(definitionV, null);
		
		//Resolver
		ResolvingVisitor resolvingV = new ResolvingVisitor();
		ast.accept(resolvingV, null);
		
		//Runner to identify yield methods
		
		// Visitor to identify yield content
		
		// Method to inject yield data
		
		// perform visit N 
		DumpVisitor printVisitor = new DumpVisitor();
		ast.accept(printVisitor, null);
		
		
		String result = printVisitor.getSource();
		
		// save the result into a *.java file, same level as the original file
		File javaFile = getAsJavaFile(file);
		writeToFile(javaFile, result);
	}
	
	/*
	 * Given a *.javax File, this method returns a *.java File at the same directory location  
	 */
	private static File getAsJavaFile(File javaxFile) {
		String javaxFileName = javaxFile.getName();
		File containingDirectory = javaxFile.getAbsoluteFile().getParentFile();
		String path = containingDirectory.getAbsolutePath()+System.getProperty("file.separator");
		String javaFilePath = path + javaxFileName.substring(0,javaxFileName.lastIndexOf("."))+".java";
		return new File(javaFilePath);
	}
	
	/*
	 * Given the specified file, writes the contents into it.
	 */
	private static void writeToFile(File file, String contents) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(file);
		writer.print(contents);
		writer.close();
	}
}
