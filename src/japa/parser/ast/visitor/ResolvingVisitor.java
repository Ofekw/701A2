/*
 * Copyright (C) 2007 Júlio Vilmar Gesser.
 * 
 * This file is part of Java 1.5 parser and Abstract Syntax Tree.
 *
 * Java 1.5 parser and Abstract Syntax Tree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Java 1.5 parser and Abstract Syntax Tree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Java 1.5 parser and Abstract Syntax Tree.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 05/10/2006
 */
package japa.parser.ast.visitor;

import japa.parser.ast.BlockComment;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.LineComment;
import japa.parser.ast.Node;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.TypeParameter;
import japa.parser.ast.body.AnnotationDeclaration;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.EmptyMemberDeclaration;
import japa.parser.ast.body.EmptyTypeDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.InitializerDeclaration;
import japa.parser.ast.body.JavadocComment;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayAccessExpr;
import japa.parser.ast.expr.ArrayCreationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.BinaryExpr;
import japa.parser.ast.expr.BooleanLiteralExpr;
import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.CharLiteralExpr;
import japa.parser.ast.expr.ClassExpr;
import japa.parser.ast.expr.ConditionalExpr;
import japa.parser.ast.expr.DoubleLiteralExpr;
import japa.parser.ast.expr.EnclosedExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.FieldAccessExpr;
import japa.parser.ast.expr.InstanceOfExpr;
import japa.parser.ast.expr.IntegerLiteralExpr;
import japa.parser.ast.expr.IntegerLiteralMinValueExpr;
import japa.parser.ast.expr.LongLiteralExpr;
import japa.parser.ast.expr.LongLiteralMinValueExpr;
import japa.parser.ast.expr.MarkerAnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.NullLiteralExpr;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.expr.SuperExpr;
import japa.parser.ast.expr.SuperMemberAccessExpr;
import japa.parser.ast.expr.ThisExpr;
import japa.parser.ast.expr.UnaryExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.AssertStmt;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.BreakStmt;
import japa.parser.ast.stmt.CatchClause;
import japa.parser.ast.stmt.ContinueStmt;
import japa.parser.ast.stmt.DoStmt;
import japa.parser.ast.stmt.EmptyStmt;
import japa.parser.ast.stmt.ExplicitConstructorInvocationStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.LabeledStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.stmt.SynchronizedStmt;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.stmt.TryStmt;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.stmt.WhileStmt;
import japa.parser.ast.stmt.YieldStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import japa.parser.ast.type.WildcardType;
import se701.A2SemanticsException;
import symtab.ClassSymbol;
import symtab.MethodSymbol;
import symtab.Scope;
import symtab.Symbol;
import symtab.VariableSymbol;

import java.util.Iterator;
import java.util.List;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;

/**
 * @author Julio Vilmar Gesser
 */

public final class ResolvingVisitor implements VoidVisitor<Object> {

    private final SourcePrinter printer = new SourcePrinter();

    public String getSource() {
        return printer.getSource();
    }

    private void printModifiers(int modifiers) {
    }

    private void printMembers(List<BodyDeclaration> members, Object arg) {
        for (BodyDeclaration member : members) {
            
            member.accept(this, arg);
            
        }
    }

    private void printMemberAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
                
            }
        }
    }

    private void printAnnotations(List<AnnotationExpr> annotations, Object arg) {
        if (annotations != null) {
            for (AnnotationExpr a : annotations) {
                a.accept(this, arg);
                
            }
        }
    }

    private void printTypeArgs(List<Type> args, Object arg) {
        if (args != null) {
            
            for (Iterator<Type> i = args.iterator(); i.hasNext();) {
                Type t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
            
        }
    }

    private void printTypeParameters(List<TypeParameter> args, Object arg) {
        if (args != null) {
            
            for (Iterator<TypeParameter> i = args.iterator(); i.hasNext();) {
                TypeParameter t = i.next();
                t.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
            
        }
    }

    public void visit(Node n, Object arg) {
        throw new IllegalStateException(n.getClass().getName());
    }

    public void visit(CompilationUnit n, Object arg) {
        if (n.getPakage() != null) {
            n.getPakage().accept(this, arg);
        }
        if (n.getImports() != null) {
            for (ImportDeclaration i : n.getImports()) {
                i.accept(this, arg);
            }
            
        }
        if (n.getTypes() != null) {
            for (Iterator<TypeDeclaration> i = n.getTypes().iterator(); i.hasNext();) {
                i.next().accept(this, arg);
                
                if (i.hasNext()) {
                    
                }
            }
        }
    }

    public void visit(PackageDeclaration n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        
        n.getName().accept(this, arg);
        
        
    }

    public void visit(NameExpr n, Object arg) {
        
    }

    public void visit(QualifiedNameExpr n, Object arg) {
        n.getQualifier().accept(this, arg);
        
        
    }

    public void visit(ImportDeclaration n, Object arg) {
        
        if (n.isStatic()) {
            
        }
        n.getName().accept(this, arg);
        if (n.isAsterisk()) {
            
        }
        
    }

    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        if (n.isInterface()) {
            
        } else {
            
        }

        

        printTypeParameters(n.getTypeParameters(), arg);

        if (n.getExtends() != null) {
            
            for (Iterator<ClassOrInterfaceType> i = n.getExtends().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }

        if (n.getImplements() != null) {
            
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }

        
        printer.indent();
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
        printer.unindent();
        
    }

    public void visit(EmptyTypeDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        
    }

    public void visit(JavadocComment n, Object arg) {
        
        
        
    }

    public void visit(ClassOrInterfaceType n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            
        }
        
        printTypeArgs(n.getTypeArgs(), arg);
    }

    public void visit(TypeParameter n, Object arg) {
        
        if (n.getTypeBound() != null) {
            
            for (Iterator<ClassOrInterfaceType> i = n.getTypeBound().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
    }

    public void visit(PrimitiveType n, Object arg) {
        switch (n.getType()) {
            case Boolean:
                
                break;
            case Byte:
                
                break;
            case Char:
                
                break;
            case Double:
                
                break;
            case Float:
                
                break;
            case Int:
                
                break;
            case Long:
                
                break;
            case Short:
                
                break;
        }
    }

    public void visit(ReferenceType n, Object arg) {
        n.getType().accept(this, arg);
        for (int i = 0; i < n.getArrayCount(); i++) {
            
        }
    }

    public void visit(WildcardType n, Object arg) {
        
        if (n.getExtends() != null) {
            
            n.getExtends().accept(this, arg);
        }
        if (n.getSuper() != null) {
            
            n.getSuper().accept(this, arg);
        }
    }

    public void visit(FieldDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());
        n.getType().accept(this, arg);

        
        //TODO
        Scope scope = n.getEnclosingScope();
        String varType = n.getType().toString();
        for (Iterator<VariableDeclarator> i = n.getVariables().iterator(); i.hasNext();) {
            VariableDeclarator v = i.next();
            v.accept(this, arg);

        	symtab.Type typeOfLeft = scope.resolve(varType).getType();

        	if (v.getInit() != null){
        	symtab.Type typeOfRight = getTypeOfExpression(v.getInit(), scope);
	        	if(typeOfRight == null){
	        		throw new A2SemanticsException(n.getType().toString() + " on line " + n.getType().getBeginLine() + " is not a defined type");
	        	}
	        	if(typeOfRight.getName() != typeOfLeft.getName() && typeOfRight.getName() != "null"){
	        		throw new A2SemanticsException("Cannot convert from " + typeOfRight.getName() + " to " + typeOfLeft.getName() + " on line " + n.getType().getBeginLine());
	        	}
        	}
          
            if (i.hasNext()) {
                
            }
        }

        
    }

    public void visit(VariableDeclarator n, Object arg) {
        n.getId().accept(this, arg);
        if (n.getInit() != null) {
            
            n.getInit().accept(this, arg);
        }
    }

    public void visit(VariableDeclaratorId n, Object arg) {
        
        for (int i = 0; i < n.getArrayCount(); i++) {
            
        }
    }

    public void visit(ArrayInitializerExpr n, Object arg) {
        
        if (n.getValues() != null) {
            
            for (Iterator<Expression> i = n.getValues().iterator(); i.hasNext();) {
                Expression expr = i.next();
                expr.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
            
        }
        
    }

    public void visit(VoidType n, Object arg) {
        
    }

    public void visit(ArrayAccessExpr n, Object arg) {
        n.getName().accept(this, arg);
        
        n.getIndex().accept(this, arg);
        
    }

    public void visit(ArrayCreationExpr n, Object arg) {
        
        n.getType().accept(this, arg);
        printTypeArgs(n.getTypeArgs(), arg);

        if (n.getDimensions() != null) {
            for (Expression dim : n.getDimensions()) {
                
                dim.accept(this, arg);
                
            }
            for (int i = 0; i < n.getArrayCount(); i++) {
                
            }
        } else {
            for (int i = 0; i < n.getArrayCount(); i++) {
                
            }
            
            n.getInitializer().accept(this, arg);
        }
    }

    public void visit(AssignExpr n, Object arg) {
        n.getTarget().accept(this, arg);
        
        //TODO
        Scope scope = n.getEnclosingScope();
        String varName = n.getTarget().toString();
        Symbol sym;
        if (varName.contains(".")){
        	sym = fieldAccessExpr(n, scope, varName);
        }else{
	        sym = scope.resolve(varName);
        }
        if(sym == null){
        	throw new A2SemanticsException(varName + " on line " + n.getBeginLine() + " is not a declared object");
        }
    	symtab.Type typeOfLeft = sym.getType();
    	symtab.Type typeOfRight = getTypeOfExpression( n.getValue(), scope);
    	if(typeOfRight == null){
    		throw new A2SemanticsException( n.getValue().toString() + " on line " + n.getValue().getBeginLine() + " is not a defined type");
    	}
    	if(typeOfRight.getName() != typeOfLeft.getName()){
    		throw new A2SemanticsException("Cannot convert from " + typeOfRight.getName() + " to " + typeOfLeft.getName() + " on line " + n.getValue().getBeginLine());
    	}
    	
    	//If within a method scope
    	boolean withinMethod = isWithinMethodScope(scope);
    	if (withinMethod){
    		if (n.getBeginLine() < sym.getDeclerationLine()){
    			throw new A2SemanticsException(varName + " on line " + n.getBeginLine() + " cannot be assigned because it's only declared on line: " + sym.getDeclerationLine());
    		}else if( n.getBeginLine()  ==  sym.getDeclerationLine() && n.getBeginColumn() < sym.getDeclerationColumn()){
    			throw new A2SemanticsException(varName + " on line " + n.getBeginLine() + ", column: "+ n.getBeginColumn() +" cannot be assigned because it's only declared on line: " + sym.getDeclerationLine() + " column: " + sym.getDeclerationColumn());
    		}
    	}
        n.getValue().accept(this, arg);
    }

	private boolean isWithinMethodScope(Scope scope) {
		while (scope != null){
			if (scope instanceof MethodSymbol){
				return true;
			}
			scope = scope.getEnclosingScope();
		}
		return false;
	}

	private Symbol fieldAccessExpr(Expression n, Scope scope, String varName) {
		Symbol sym;
		String[] objectField = varName.split("\\.");
		String variableName = objectField[0];
		String fieldName = objectField[1];
		Symbol variableSym = null;
		if (variableName.contains("this")){
			while (!(scope instanceof ClassSymbol)){
				// get class scope
				scope = scope.getEnclosingScope();
			}
			variableSym = scope.resolve(fieldName);
		}else{
			variableSym = scope.resolve(variableName);
		}
		if(variableSym == null){
			throw new A2SemanticsException(varName + " on line " + n.getBeginLine() + " is not a declared object");
		}
		Symbol classSym = scope.resolve(variableSym.getType().getName());
		if (variableName.contains("this")){
			sym = variableSym;
			return sym;
		}
		if (!(classSym instanceof ClassSymbol)){
			throw new A2SemanticsException(varName + " on line " + n.getBeginLine() + " is not a class object with fields");
		}
		sym = ((ClassSymbol)classSym).resolve(fieldName);
		return sym;
	}

    public void visit(BinaryExpr n, Object arg) {
        n.getLeft().accept(this, arg);
        Scope scope = n.getEnclosingScope();
        symtab.Type leftItem = getTypeOfExpression(n.getLeft(), scope);
        symtab.Type rightItem = getTypeOfExpression(n.getRight(), scope);
        if (leftItem.getName() != rightItem.getName()){
        	throw new A2SemanticsException("Cannot compare from " + rightItem.getName() + " to " + leftItem.getName() + " on line " + n.getBeginLine());
        }
        n.getRight().accept(this, arg);
    }

    public void visit(CastExpr n, Object arg) {
        
        n.getType().accept(this, arg);
        
        n.getExpr().accept(this, arg);
    }

    public void visit(ClassExpr n, Object arg) {
        n.getType().accept(this, arg);
        
    }

    public void visit(ConditionalExpr n, Object arg) {
        n.getCondition().accept(this, arg);
        
        n.getThenExpr().accept(this, arg);
        
        n.getElseExpr().accept(this, arg);
    }

    public void visit(EnclosedExpr n, Object arg) {
        
        n.getInner().accept(this, arg);
        
    }

    public void visit(FieldAccessExpr n, Object arg) {
        n.getScope().accept(this, arg);
        
        
    }

    public void visit(InstanceOfExpr n, Object arg) {
        n.getExpr().accept(this, arg);
        
        n.getType().accept(this, arg);
    }

    public void visit(CharLiteralExpr n, Object arg) {
        
        
        
    }

    public void visit(DoubleLiteralExpr n, Object arg) {
        
    }

    public void visit(IntegerLiteralExpr n, Object arg) {
        
    }

    public void visit(LongLiteralExpr n, Object arg) {
        
    }

    public void visit(IntegerLiteralMinValueExpr n, Object arg) {
        
    }

    public void visit(LongLiteralMinValueExpr n, Object arg) {
        
    }

    public void visit(StringLiteralExpr n, Object arg) {
        
        
        
    }

    public void visit(BooleanLiteralExpr n, Object arg) {
        
    }

    public void visit(NullLiteralExpr n, Object arg) {
        
    }

    public void visit(ThisExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
            
        }
        
    }

    public void visit(SuperExpr n, Object arg) {
        if (n.getClassExpr() != null) {
            n.getClassExpr().accept(this, arg);
            
        }
        
    }

    public void visit(MethodCallExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            
        }
        
        Scope scope = n.getEnclosingScope();
        MethodSymbol sym = (MethodSymbol)scope.resolve(n.getName());
        
        List<Expression> args = n.getArgs();
        List<Parameter> params = sym.getMethodParameter();
        if (args != null && params != null && args.size() > 0 && params.size() > 0){
	        for (int i = 0; i < n.getArgs().size(); i++){
	        	Symbol parameter = sym.resolve(params.get(i).getId().getName());
	        	symtab.Type paramaterType = parameter.getType();
	        	Expression argVal = args.get(i);
	        	symtab.Type argType = getTypeOfExpression(argVal, scope);
	        	if (argType.getName() != paramaterType.getName()){
	        		throw new A2SemanticsException(argVal.toString() + " of type " + argType.getName() + " on line " + n.getBeginLine() + " does not match the "+ n.getName() +" method parameter of type " + paramaterType.getName() );
	        	}
	        	
	        }
        }else if (args == null && params != null){
    		throw new A2SemanticsException(n.getName()+ " on line " + n.getBeginLine() + " does not have parameters in the method signature");
        }else if (args != null && params == null){
        	throw new A2SemanticsException(n.getName()+ " on line " + n.getBeginLine() + " has parameters in the method signature which where not assigned");
        }
        
    	Statement yield = n.getYieldBlock();
    	if (yield != null){
    		MethodSymbol methodSym = (MethodSymbol)scope.resolve(n.getName());
    		methodSym.defineYield(n.getName(), yield);
    		
    		while (!(scope instanceof ClassSymbol)){
    			// get class scope
    			scope = scope.getEnclosingScope();
    		}
    		scope = ((ClassSymbol)scope).getYieldScope(n.getName());
    		if (scope == null){
    			throw new A2SemanticsException(n.getName()+ " on line " + n.getBeginLine() + " does not have a yield statement, hence a yield block is invalid");
    		}
    	}
        
        printTypeArgs(n.getTypeArgs(), arg);
        
        
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        
    }

    public void visit(ObjectCreationExpr n, Object arg) {
        if (n.getScope() != null) {
            n.getScope().accept(this, arg);
            
        }

        

        printTypeArgs(n.getTypeArgs(), arg);
        n.getType().accept(this, arg);

        
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        

        if (n.getAnonymousClassBody() != null) {
            
            printer.indent();
            printMembers(n.getAnonymousClassBody(), arg);
            printer.unindent();
            
        }
    }

    public void visit(SuperMemberAccessExpr n, Object arg) {
        
        
    }

    public void visit(UnaryExpr n, Object arg) {

        n.getExpr().accept(this, arg);
    }

    public void visit(ConstructorDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        printTypeParameters(n.getTypeParameters(), arg);
        if (n.getTypeParameters() != null) {
            
        }
        

        
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        

        if (n.getThrows() != null) {
            
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        
        n.getBlock().accept(this, arg);
    }

    public void visit(MethodDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        printTypeParameters(n.getTypeParameters(), arg);
        if (n.getTypeParameters() != null) {
            
        }

        n.getType().accept(this, arg);
        
        

        
        if (n.getParameters() != null) {
            for (Iterator<Parameter> i = n.getParameters().iterator(); i.hasNext();) {
                Parameter p = i.next();
                p.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        

        for (int i = 0; i < n.getArrayCount(); i++) {
            
        }

        if (n.getThrows() != null) {
            
            for (Iterator<NameExpr> i = n.getThrows().iterator(); i.hasNext();) {
                NameExpr name = i.next();
                name.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        if (n.getBody() == null) {
            
        } else {
            
            n.getBody().accept(this, arg);
        }
    }

    public void visit(Parameter n, Object arg) {
        printAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, arg);
        if (n.isVarArgs()) {
            
        }
        
        n.getId().accept(this, arg);
    }

    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        if (n.isThis()) {
            printTypeArgs(n.getTypeArgs(), arg);
            
        } else {
            if (n.getExpr() != null) {
                n.getExpr().accept(this, arg);
                
            }
            printTypeArgs(n.getTypeArgs(), arg);
            
        }
        
        if (n.getArgs() != null) {
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        
    }

    public void visit(VariableDeclarationExpr n, Object arg) {
    	printAnnotations(n.getAnnotations(), arg);
    	printModifiers(n.getModifiers());

    	n.getType().accept(this, arg);
    	
    	
    	//TODO
        Scope scope = n.getEnclosingScope();
        String varType = n.getType().toString();
    	symtab.Type typeOfLeft = scope.resolve(varType).getType();
    	
    	for (Iterator<VariableDeclarator> i = n.getVars().iterator(); i.hasNext();) {
    		VariableDeclarator v = i.next();
    		
    		Expression init = v.getInit();
    		if (init != null){
	        	symtab.Type typeOfRight = getTypeOfExpression(init, scope);
	        	if(typeOfRight == null){
	        		throw new A2SemanticsException(n.getType().toString() + " on line " + n.getType().getBeginLine() + " is not a defined type");
	        	}
	        	if(typeOfRight.getName() != typeOfLeft.getName()){
	        		throw new A2SemanticsException("Cannot convert from " + typeOfRight.getName() + " to " + typeOfLeft.getName() + " on line " + n.getType().getBeginLine());
	        	}
    		}

    		v.accept(this, arg);
    		if (i.hasNext()) {
    			
    		}
    	}


    }

private symtab.Type getTypeOfExpression(Expression init, Scope scope) {
	symtab.Type type = null;
	if(init != null){
		Symbol sym = null;
		if(init.getClass() == NameExpr.class){
			sym = scope.resolve(init.toString());
			if (sym instanceof ClassSymbol){
				throw new A2SemanticsException(init + " is not valid instantiation on line " + init.getBeginLine() + ", use the 'new' keyword");
			}
			if(sym == null){
				throw new A2SemanticsException(init + " is not defined on line " + init.getBeginLine());
			}
			if(!(sym.getType() instanceof symtab.Type)){
				throw new A2SemanticsException(init + " is not valid on line " + init.getBeginLine());
			}
			type = sym.getType();
		}else if(init.getClass() == ObjectCreationExpr.class){
			ObjectCreationExpr obj = (ObjectCreationExpr)init;
			sym = scope.resolve((obj.getType().toString()));
			if(sym == null){
				throw new A2SemanticsException(init + " is not defined on line " + init.getBeginLine());
			}
			if(!(sym.getType() instanceof symtab.Type)){
				throw new A2SemanticsException(init + " is not valid on line " + init.getBeginLine());
			}
			type = sym.getType();
		}else{
			//NOTE: IntegerLiteralExpr extends StringLiteralExpr, so must check IntegerLiteralExpr first
			if(init.getClass() == IntegerLiteralExpr.class){
				sym = scope.resolve("int");
			}else if(init.getClass() == BooleanLiteralExpr.class){
				sym = scope.resolve("boolean");
			}else if (init.getClass() == StringLiteralExpr.class){
				sym = scope.resolve("String");
			}else if (init.getClass() == NullLiteralExpr.class){
				sym = scope.resolve("null");
			}else if (init.getClass() == MethodCallExpr.class){
				// for each get argument type, deal with mutiple method paramterss, get type by using methodSym
				sym = scope.resolve(((MethodCallExpr) init).getName());
			}else if (init.getClass() == FieldAccessExpr.class){
				// for each get argument type, deal with mutiple method paramterss, get type by using methodSym
				sym = fieldAccessExpr(init, scope, init.toString());
			//TODO other primitive types (and others?)
			}else{
				System.out.println("Add " + init.getClass() + " to getTypeofExpression helper method");
			}
			type = sym.getType(); 
		}
	}
	if(type == null){
		throw new A2SemanticsException(init + " is not defined on line " + init.getBeginLine());
	}
	return type;
}

    public void visit(TypeDeclarationStmt n, Object arg) {
        n.getTypeDeclaration().accept(this, arg);
    }

    public void visit(AssertStmt n, Object arg) {
        
        n.getCheck().accept(this, arg);
        if (n.getMessage() != null) {
            
            n.getMessage().accept(this, arg);
        }
        
    }

    public void visit(BlockStmt n, Object arg) {
        
        if (n.getStmts() != null) {
            printer.indent();
            for (Statement s : n.getStmts()) {
            	s.setEnclosingScope(n.getEnclosingScope());
                s.accept(this, arg);
                
            }
            printer.unindent();
        }
        

    }

    public void visit(LabeledStmt n, Object arg) {
        
        
        n.getStmt().accept(this, arg);
    }

    public void visit(EmptyStmt n, Object arg) {
        
    }

    public void visit(ExpressionStmt n, Object arg) {
    	n.getExpression().setEnclosingScope(n.getEnclosingScope());
        n.getExpression().accept(this, arg);
        
    }

    public void visit(SwitchStmt n, Object arg) {
        
        n.getSelector().accept(this, arg);
        
        if (n.getEntries() != null) {
            printer.indent();
            for (SwitchEntryStmt e : n.getEntries()) {
                e.accept(this, arg);
            }
            printer.unindent();
        }
        

    }

    public void visit(SwitchEntryStmt n, Object arg) {
        if (n.getLabel() != null) {
            
            n.getLabel().accept(this, arg);
            
        } else {
            
        }
        
        printer.indent();
        if (n.getStmts() != null) {
            for (Statement s : n.getStmts()) {
                s.accept(this, arg);
                
            }
        }
        printer.unindent();
    }

    public void visit(BreakStmt n, Object arg) {
        
        if (n.getId() != null) {
            
            
        }
        
    }

    public void visit(ReturnStmt n, Object arg) {
        Scope scope = n.getEnclosingScope();
        Symbol sym = getEnclosingMethodSym(scope);
        if(sym == null){
        	throw new A2SemanticsException("Return statement on line: " + n.getBeginLine() + " is not within a method");
        }
        String returnType = getTypeOfExpression(n.getExpr(), scope).getName();
        if (sym.getType().getName() != returnType){
        	throw new A2SemanticsException("Return statement on line: " + n.getBeginLine() + " returns: " + returnType + " does not match method return type of: " + sym.getType().getName());
        }
        if (n.getExpr() != null) {
            
            n.getExpr().accept(this, arg);
        }
    }

    private Symbol getEnclosingMethodSym(Scope scope) {
    	while (scope != null){
			if (scope instanceof MethodSymbol){
				return (MethodSymbol)scope;
			}
			scope = scope.getEnclosingScope();
		}
		return null;
	}
    public void visit(EnumDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        
        

        if (n.getImplements() != null) {
            
            for (Iterator<ClassOrInterfaceType> i = n.getImplements().iterator(); i.hasNext();) {
                ClassOrInterfaceType c = i.next();
                c.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }

        
        printer.indent();
        if (n.getEntries() != null) {
            
            for (Iterator<EnumConstantDeclaration> i = n.getEntries().iterator(); i.hasNext();) {
                EnumConstantDeclaration e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        if (n.getMembers() != null) {
            
            printMembers(n.getMembers(), arg);
        } else {
            if (n.getEntries() != null) {
                
            }
        }
        printer.unindent();
        
    }

    public void visit(EnumConstantDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        

        if (n.getArgs() != null) {
            
            for (Iterator<Expression> i = n.getArgs().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
            
        }

        if (n.getClassBody() != null) {
            
            printer.indent();
            printMembers(n.getClassBody(), arg);
            printer.unindent();
            
        }
    }

    public void visit(EmptyMemberDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        
    }

    public void visit(InitializerDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        if (n.isStatic()) {
            
        }
        n.getBlock().accept(this, arg);
    }

    public void visit(IfStmt n, Object arg) {
        
        n.getCondition().accept(this, arg);
        
        n.getThenStmt().accept(this, arg);
        if (n.getElseStmt() != null) {
            
            n.getElseStmt().accept(this, arg);
        }
    }

    public void visit(WhileStmt n, Object arg) {
        
        n.getCondition().accept(this, arg);
        
        n.getBody().accept(this, arg);
    }

    public void visit(ContinueStmt n, Object arg) {
        
        if (n.getId() != null) {
            
            
        }
        
    }

    public void visit(DoStmt n, Object arg) {
        
        n.getBody().accept(this, arg);
        
        n.getCondition().accept(this, arg);
        
    }

    public void visit(ForeachStmt n, Object arg) {
        
        n.getVariable().accept(this, arg);
        
        n.getIterable().accept(this, arg);
        
        n.getBody().accept(this, arg);
    }

    public void visit(ForStmt n, Object arg) {
        
        if (n.getInit() != null) {
            for (Iterator<Expression> i = n.getInit().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        
        if (n.getCompare() != null) {
        	n.getCompare().setEnclosingScope(n.getEnclosingScope());
            n.getCompare().accept(this, arg);
        }
        
        if (n.getUpdate() != null) {
            for (Iterator<Expression> i = n.getUpdate().iterator(); i.hasNext();) {
                Expression e = i.next();
                e.accept(this, arg);
                if (i.hasNext()) {
                    
                }
            }
        }
        
        n.getBody().setEnclosingScope(n.getEnclosingScope());
        n.getBody().accept(this, arg);
    }

    public void visit(ThrowStmt n, Object arg) {
        
        n.getExpr().accept(this, arg);
        
    }

    public void visit(SynchronizedStmt n, Object arg) {
        
        n.getExpr().accept(this, arg);
        
        n.getBlock().accept(this, arg);
    }

    public void visit(TryStmt n, Object arg) {
        
        n.getTryBlock().accept(this, arg);
        if (n.getCatchs() != null) {
            for (CatchClause c : n.getCatchs()) {
                c.accept(this, arg);
            }
        }
        if (n.getFinallyBlock() != null) {
            
            n.getFinallyBlock().accept(this, arg);
        }
    }

    public void visit(CatchClause n, Object arg) {
        
        n.getExcept().accept(this, arg);
        
        n.getCatchBlock().accept(this, arg);

    }

    public void visit(AnnotationDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        
        
        
        printer.indent();
        if (n.getMembers() != null) {
            printMembers(n.getMembers(), arg);
        }
        printer.unindent();
        
    }

    public void visit(AnnotationMemberDeclaration n, Object arg) {
        if (n.getJavaDoc() != null) {
            n.getJavaDoc().accept(this, arg);
        }
        printMemberAnnotations(n.getAnnotations(), arg);
        printModifiers(n.getModifiers());

        n.getType().accept(this, arg);
        
        
        
        if (n.getDefaultValue() != null) {
            
            n.getDefaultValue().accept(this, arg);
        }
        
    }

    public void visit(MarkerAnnotationExpr n, Object arg) {
        
        n.getName().accept(this, arg);
    }

    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        
        n.getName().accept(this, arg);
        
        n.getMemberValue().accept(this, arg);
        
    }

    public void visit(NormalAnnotationExpr n, Object arg) {
        
        n.getName().accept(this, arg);
        
        for (Iterator<MemberValuePair> i = n.getPairs().iterator(); i.hasNext();) {
            MemberValuePair m = i.next();
            m.accept(this, arg);
            if (i.hasNext()) {
                
            }
        }
        
    }

    public void visit(MemberValuePair n, Object arg) {
        
        
        n.getValue().accept(this, arg);
    }

    public void visit(LineComment n, Object arg) {
        
        
    }

    public void visit(BlockComment n, Object arg) {
        
        
        
    }

	@Override
	public void visit(YieldStmt n, Object arg) {
		Scope scope = n.getEnclosingScope();
		while (!(scope instanceof ClassSymbol)){
			// get class scope
			scope = scope.getEnclosingScope();
		}
		String methodName = n.getId();
		Symbol sym = scope.resolve(methodName);
		if (sym == null){
			throw new A2SemanticsException(methodName + "is not in scope");
		}
		if (!(sym instanceof MethodSymbol)){
			throw new A2SemanticsException(methodName + "stashed in node and resolved as a " + sym.getType().toString() + "type instead MethodSymbol");
		}
		Statement yieldStmt = ((MethodSymbol) sym).resolveYield(methodName);
		yieldStmt.accept(this, arg);
	}
}
