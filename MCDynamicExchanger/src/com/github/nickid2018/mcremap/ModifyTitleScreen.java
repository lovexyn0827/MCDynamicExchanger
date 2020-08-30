package com.github.nickid2018.mcremap;

import org.objectweb.asm.*;

public class ModifyTitleScreen extends ClassVisitor {

	private ClassWriter writer;

	public ModifyTitleScreen(ClassWriter writer) {
		super(Opcodes.ASM6, writer);
		this.writer = writer;

	}

	private boolean notFieldAdded = true;

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (notFieldAdded) {
			notFieldAdded = false;
			super.visitField(Opcodes.ACC_PRIVATE, "remapperX", "I", null, null);
		}
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		if (name.equals("init")) {
			return new HackInitMethod(writer.visitMethod(access, name, desc, signature, exceptions));
		}
		if (name.equals("render")) {
			return new HackRenderMethod(writer.visitMethod(access, name, desc, signature, exceptions));
		}
		return writer.visitMethod(access, name, desc, signature, exceptions);
	}

	public class HackInitMethod extends MethodVisitor {

		public HackInitMethod(MethodVisitor defaultVisitor) {
			super(Opcodes.ASM6, defaultVisitor);
			defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
			defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
			defaultVisitor.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/TitleScreen", "width",
					"I");// GETFIELD this.width
			defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
			defaultVisitor.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/TitleScreen", "font",
					"Lnet/minecraft/client/gui/Font;");// GETFIELD this.font
			defaultVisitor.visitLdcInsn("Remapped by Nickid2018");// LDC STRING
			defaultVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/gui/Font", "width",
					"(Ljava/lang/String;)I", false);// this.font.width("Remapped by Nickid2018")
			defaultVisitor.visitInsn(Opcodes.ISUB);// width-font.width("Remapped by Nickid2018")
			defaultVisitor.visitInsn(Opcodes.ICONST_2);// int 2
			defaultVisitor.visitInsn(Opcodes.ISUB);// width-font.width("Remapped by Nickid2018")-2
			defaultVisitor.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/client/gui/screens/TitleScreen", "remapperX",
					"I");// Save to width
		}
	}

	public class HackRenderMethod extends MethodVisitor {

		private boolean over = true;
		private MethodVisitor defaultVisitor;

		public HackRenderMethod(MethodVisitor defaultVisitor) {
			super(Opcodes.ASM6, defaultVisitor);
			this.defaultVisitor = defaultVisitor;
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
			if (over && name.equals("drawString")) {
				over = false;
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
				defaultVisitor.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/TitleScreen", "font",
						"Lnet/minecraft/client/gui/Font;");// GETFIELD this.font
				defaultVisitor.visitLdcInsn("Remapped by Nickid2018");// LDC STRING
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
				defaultVisitor.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/TitleScreen",
						"remapperX", "I");// GETFIELD this.remapperX
				defaultVisitor.visitVarInsn(Opcodes.ALOAD, 0);// ALOAD 0[this]
				defaultVisitor.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/screens/TitleScreen",
						"height", "I");// GETFIELD this.height
				defaultVisitor.visitIntInsn(Opcodes.BIPUSH, 20);// bipush 20
				defaultVisitor.visitInsn(Opcodes.ISUB);// this.height-10
				defaultVisitor.visitLdcInsn(16777215);// LDC INTEGER 16777215
				defaultVisitor.visitVarInsn(Opcodes.ILOAD, 9);// ILOAD 9[var9]
				defaultVisitor.visitInsn(Opcodes.IOR);// 16777215|var9
				defaultVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/gui/screens/TitleScreen",
						"drawString", "(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V", false);
			}
		}
	}
}