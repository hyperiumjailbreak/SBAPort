package codes.biscuit.skyblockaddons.asm;

import codes.biscuit.skyblockaddons.asm.utils.TransformerClass;
import codes.biscuit.skyblockaddons.asm.utils.TransformerMethod;
import codes.biscuit.skyblockaddons.tweaker.transformer.ITransformer;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.lib.tree.*;

public class InventoryPlayerTransformer implements ITransformer {

    /**
     * {@link net.minecraft.entity.player.InventoryPlayer}
     */
    @Override
    public String[] getClassName() {
        return new String[]{TransformerClass.InventoryPlayer.getTransformerName()};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            if (TransformerMethod.changeCurrentItem.matches(methodNode)) {

                // Objective:
                // Find: Method head.
                // Insert: MinecraftHook.updatedCurrentItem();

                methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "codes/biscuit/skyblockaddons/asm/hooks/MinecraftHook",
                        "updatedCurrentItem", "()V", false)); // MinecraftHook.updatedCurrentItem();
            }
        }
    }
}
