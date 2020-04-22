function initializeCoreMod() {
	return {
		'sensitivity': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.client.MouseHelper',
				'methodName': 'func_198028_a',
				'methodDesc': '()V'
			},
			'transformer': function(method) {
			    log("Patching MouseHelper#func_198028_a");
                patch_MouseHelper_updatePlayerLook(method);
				return method;
			}
		}
    };
}

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

function patch_MouseHelper_updatePlayerLook(method) {
    var predicate = {
        test: function(node) {
            return node.getOpcode() == Opcodes.GETFIELD && node.owner.equals("net/minecraft/client/GameSettings") && node.name.equals(ASMAPI.mapField("field_74341_c")) && node.desc.equals("D");
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
        var storeNode = getRelativeNode(targetNode, 5);
        if(storeNode !== null && storeNode.getOpcode() == Opcodes.DSTORE) {
            method.instructions.insert(storeNode, new VarInsnNode(Opcodes.DSTORE, 5));
            method.instructions.insert(storeNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/guns/client/Hooks", "applyModifiedSensitivity", "(D)D", false));
            method.instructions.insert(storeNode, new VarInsnNode(Opcodes.DLOAD, 5));
            log("Successfully patched MouseHelper#func_198028_a");
            return;
        }
    }
    log("Failed to patch MouseHelper#func_198028_a");
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