package codes.biscuit.skyblockaddons.asm;

import codes.biscuit.skyblockaddons.asm.utils.TransformerClass;
import codes.biscuit.skyblockaddons.asm.utils.TransformerMethod;
import codes.biscuit.skyblockaddons.tweaker.transformer.ITransformer;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.*;

import java.util.Iterator;

public class EntityRendererTransformer implements ITransformer {

    /**
     * {@link net.minecraft.client.renderer.EntityRenderer}
     */
    @Override
    public String[] getClassName() {
        return new String[]{TransformerClass.EntityRenderer.getTransformerName()};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            if (TransformerMethod.getMouseOver.matches(methodNode)) {

                // Objective:
                // Find: The entity list variable.
                // Insert EntityRendererHook.removeEntities(list);

                Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode abstractNode = iterator.next();
                    if (abstractNode instanceof VarInsnNode && abstractNode.getOpcode() == Opcodes.DLOAD) {
                        VarInsnNode varInsnNode = (VarInsnNode)abstractNode;
                        if (varInsnNode.var == 5) { // List variable is created right before variable 5 is accessed (double d3 = d2;)
                            methodNode.instructions.insertBefore(varInsnNode, insertRemoveEntities());
                            break;
                        }
                    }
                }
            }
        }
    }

    private InsnList insertRemoveEntities() {
        InsnList list = new InsnList();

        list.add(new VarInsnNode(Opcodes.ALOAD, 14)); // EntityRendererHook.removeEntities(list);
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "codes/biscuit/skyblockaddons/asm/hooks/EntityRendererHook", "removeEntities",
                "(Ljava/util/List;)V", false));

        return list;
    }
}
