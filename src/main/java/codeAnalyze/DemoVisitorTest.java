package codeAnalyze;

import org.eclipse.jdt.core.dom.CompilationUnit;


public class DemoVisitorTest {

    public DemoVisitorTest(String path) {
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(path);

        DemoVisitor visitor = new DemoVisitor();
        comp.accept(visitor);
    }
    public static void main(String args[]) {
        DemoVisitorTest t=new DemoVisitorTest("I:\\Test.txt");
    }
}
