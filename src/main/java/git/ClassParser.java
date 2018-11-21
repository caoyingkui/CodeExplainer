package git;


import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.patch.Patch;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ClassParser {
    CompilationUnit unit;
    String sourceCode;
    List<Integer> lines;
    int codeLength;
    List<Method> methods;

    public ClassParser(String sourceCode){
        this.sourceCode = sourceCode;
        this.codeLength = sourceCode.length();
        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setSource(sourceCode.toCharArray());
        unit = (CompilationUnit) parser.createAST(null);
        getLines();
        getAllMethods();
    }

    int getLine(int position){
        if(position < 0|| position >= codeLength)
            return -1;
        else{
            for(int i = 0 ; i < lines.size() - 1; i ++){
                if(position >= lines.get(i) && position < lines.get(i + 1))
                    return i + 1;
            }
            return lines.size();
        }
    }

    public List<Method> getAllMethods(TypeDeclaration type, String qualifiedName){
        List<Method> result = new ArrayList<>();
        String className = type.getName().toString();
        for(Object object: type.bodyDeclarations()){
            if(object instanceof MethodDeclaration){
                MethodDeclaration declaration = (MethodDeclaration) object;
                String methodName = declaration.getName().toString();
                int startPosition = declaration.getStartPosition();
                int endPosition = startPosition + declaration.getLength() - 1;

                result.add(
                        new Method(qualifiedName + "." + className + "." + methodName,
                                getLine(startPosition),
                                getLine(endPosition)
                        )
                );
            }
        }
        return result;
    }


    private void getLines(){
        lines = new ArrayList<>();
        int length = sourceCode.length();
        int start = 0;
        for(int i = 0 ; i < codeLength ; i++){
            if(sourceCode.charAt(i) =='\n'){
                lines.add(start);
                start = i + 1;
            }
        }
    }

    public Set<String> getAllMethods(){
        Set<String> result = new HashSet<>();
        methods = new ArrayList<>();
        PackageDeclaration p = unit.getPackage();
        String packageName = "";
        if(p != null){
            packageName = p.getName().toString();
        }

        for(Object type: unit.types()){
            if(type instanceof TypeDeclaration){
                methods.addAll(getAllMethods((TypeDeclaration)type, packageName));
            }
        }

        Collections.sort(methods, (o1,o2 )->{
            if(o1.endLine < o2.startLine) return -1;
            else if(o2.endLine < o1.startLine) return 1;
            else return 0;
        });

        for(Method method: methods)
            result.add(method.fullName);
        return result;
    }

    Set<String> getChagneMethod(Patch patch, boolean isNew){
        Set<String> result = new HashSet<>();
        for(FileHeader file: patch.getFiles()){
            for(HunkHeader hunk: file.getHunks()){
                int start = isNew ? hunk.getNewStartLine() : hunk.getOldImage().getStartLine();
                int end  = isNew ? hunk.getNewLineCount() : hunk.getOldImage().getLineCount();
                end = start + end - 1;

                for(Method method: methods){
                    if(start >= method.startLine && start <= method.endLine ||
                    end >= method.startLine && end <= method.endLine)
                        result.add(method.fullName);
                }
            }

        }
        return result;
    }

    public static void main(String[] args){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("code.txt")));
            String line;
            String code = "";
            while ((line = reader.readLine()) != null) {
                code += line + "\n";
            }
            reader.close();

            ClassParser parser = new ClassParser(code);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
