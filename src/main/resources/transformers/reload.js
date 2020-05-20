function initializeCoreMod() {
	return {
		'knockback': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.server.MinecraftServer',
				'methodName': 'func_193031_aM',
				'methodDesc': '()V'
			},
			'transformer': function(method) {
			    log("Patching MinecraftServer#func_193031_aM");
                patch_MinecraftServer_reload(method);
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

function patch_MinecraftServer_reload(method) {
    var predicate = {
        test: function(node) {
            return node.getOpcode() == Opcodes.INVOKESPECIAL && node.owner.equals("net/minecraft/server/MinecraftServer") && node.name.equals(ASMAPI.mapMethod("func_229737_ba_")) && node.desc.equals("()V");
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
        method.instructions.insert(targetNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/guns/common/Hooks", "onReload", "()V", false));
        log("Successfully patched MinecraftServer#func_193031_aM");
        return;
    }
    log("Failed to patch MinecraftServer#func_193031_aM");
}

function log(s) {
    print("[MrCrayfish's Gun Mod] " + s);
}