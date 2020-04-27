function initializeCoreMod() {
	return {
		'knockback': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.entity.LivingEntity',
				'methodName': 'func_70097_a',
				'methodDesc': '(Lnet/minecraft/util/DamageSource;F)Z'
			},
			'transformer': function(method) {
			    log("Patching LivingEntity#func_70097_a");
                patch_LivingEntity_attackEntityFrom(method);
				return method;
			}
		}
    };
}

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');

function patch_LivingEntity_attackEntityFrom(method) {
    var predicate = {
        test: function(node) {
            return node.getOpcode() == Opcodes.INVOKEVIRTUAL && node.owner.equals("net/minecraft/entity/LivingEntity") && node.name.equals(ASMAPI.mapMethod("func_70653_a")) && node.desc.equals("(Lnet/minecraft/entity/Entity;FDD)V");
        }
    };
    var targetNode = null;
    var instructions = method.instructions.toArray();
    for(var i = 0; i < instructions.length; i++) {
        var node = instructions[i];
        if(predicate.test(node)) {
            targetNode = node;
            break;
        }
    }
    if(targetNode !== null) {
        var startNode = getRelativeNode(targetNode, -5);
        var endNode = getRelativeNode(targetNode, 1);
        if(startNode !== null && startNode.getOpcode() == Opcodes.ALOAD) {
            method.instructions.insertBefore(startNode, new VarInsnNode(Opcodes.ALOAD, 1));
            method.instructions.insertBefore(startNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/guns/common/Hooks", "canCauseKnockBack", "(Lnet/minecraft/util/DamageSource;)Z", false));
            var jumpNode = new LabelNode();
            method.instructions.insertBefore(startNode, new JumpInsnNode(Opcodes.IFEQ, jumpNode));
            method.instructions.insertBefore(endNode, jumpNode);
            method.instructions.insertBefore(endNode, new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
            log("Successfully patched LivingEntity#func_70097_a");
            return;
        }
    }
    log("Failed to patch LivingEntity#func_70097_a");
}

function getRelativeNode(node, n) {
    while(n > 0 && node !== null) {
        node = node.getNext();
        n--;
    }
    while(n < 0 && node !== null) {
        node = node.getPrevious();
        n++;
    }
    return node;
}

function log(s) {
    print("[cgm-transformer] " + s);
}