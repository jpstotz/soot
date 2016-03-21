package soot.cil.ast;

import java.util.List;

/**
 * Class representing a method in a CIL disassembly file
 * 
 * @author Steven Arzt
 *
 */
public class CilMethod {
	
	private final CilClass clazz;
	private final CilMethodParameterList parameters;
	private final List<CilInstruction> instructions;
	private final List<CilTrap> traps;
	
	public CilMethod(CilClass clazz,
			CilMethodParameterList parameters,
			List<CilInstruction> instructions,
			List<CilTrap> traps) {
		this.clazz = clazz;
		this.parameters = parameters;
		this.instructions = instructions;
		this.traps = traps;
	}
	
	public CilClass getCilClass() {
		return this.clazz;
	}
	
	public CilMethodParameterList getParameters() {
		return this.parameters;
	}
	
	public List<CilInstruction> getInstructions() {
		return this.instructions;
	}
	
	public List<CilTrap> getTraps() {
		return this.traps;
	}

}