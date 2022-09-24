package io.github.nickid2018.mcde.asmdl.function;

import io.github.nickid2018.mcde.asmdl.ASMDLSyntaxException;
import io.github.nickid2018.mcde.asmdl.DescFunction;
import io.github.nickid2018.mcde.asmdl.DescFunctionContext;
import io.github.nickid2018.mcde.asmdl.DescFunctions;
import org.objectweb.asm.MethodVisitor;

public class MethodVarIntFunction extends DescFunction {

    private final int opcode;

    public MethodVarIntFunction(String name, int opcode) {
        super(name);
        this.opcode = opcode;
    }

    @Override
    public void process(DescFunctionContext context) throws ASMDLSyntaxException {
        if (context.environment() != DescFunctions.METHOD && context.environment() != DescFunctions.LABEL)
            throw new ASMDLSyntaxException(name + " function must be in a method block or a label block");

        MethodVisitor mv = (MethodVisitor) context.visitor();
        String[] args = context.args();
        if (args.length < 1)
            throw new ASMDLSyntaxException(name + " function requires an integer argument");
        try {
            mv.visitVarInsn(opcode, Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            throw new ASMDLSyntaxException(name + " function requires an integer argument");
        }
    }
}