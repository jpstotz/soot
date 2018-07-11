package soot.dexpler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StringConstant;
import soot.tagkit.InitialValueTag;

/**
 * A scene transformer that converts the initial values of static (non-final) fields (present in form of
 * {@link InitialValueTag}) to Jimple-Code in the correspondent <code>clinit</code> method.
 *
 * @author Fabian Brenner
 */
public class InitialValueTransformer extends SceneTransformer {

  @Override
  protected void internalTransform(String phaseName, Map<String, String> options) {

    for (SootClass sc : Scene.v().getApplicationClasses()) {

      List<SootField> initialValueFields = sc.getFields().stream().filter( //
          sf -> sf.getTag(InitialValueTag.NAME) != null).collect(Collectors.toList());

      if (initialValueFields.size() == 0) {
        continue;
      }

      // Check if we can modify an existing <clinit> method or if we have to create a new one
      if (sc.declaresMethodByName("<clinit>")) {

        SootMethod clinitMethod = sc.getMethodByName("<clinit>");
        JimpleBody clinitBody = (JimpleBody) clinitMethod.getActiveBody();

        int modifiedCount = processFieldsOfClass(sc, initialValueFields, clinitBody);
        if (modifiedCount > 0) {
          clinitMethod.setActiveBody(clinitBody);
        }
      } else {
        SootMethod clinitMethod = new SootMethod("<clinit>", null, VoidType.v(), soot.Modifier.STATIC);
        clinitMethod.setDeclaringClass(sc);
        JimpleBody clinitBody = new JimpleBody(clinitMethod);

        int modifiedCount = processFieldsOfClass(sc, initialValueFields, clinitBody);

        if (modifiedCount > 0) {
          clinitBody.getUnits().add(Jimple.v().newReturnVoidStmt());
          clinitMethod.setActiveBody(clinitBody);
          Scene.v().getSootClass(sc.getName()).addMethod(clinitMethod);
        }
      }
    }

  }

  private int processFieldsOfClass(SootClass sc, List<SootField> initialValueFields, JimpleBody clinitBody) {
    int i = 0;
    for (SootField field : initialValueFields) {
      InitialValueTag tag = (InitialValueTag) field.getTag(InitialValueTag.NAME);
      if (tag == null) {
        continue;
      }

      Object initialValue = tag.getValueObject();

      // Currently we only handle String constants
      if (initialValue instanceof String) {
        Local c = Jimple.v().newLocal("r" + i++, Scene.v().getType("java.lang.String"));
        clinitBody.getLocals().add(c);
        StringConstant stringConst = StringConstant.v((String) initialValue);
        AssignStmt assignStmt = Jimple.v().newAssignStmt(c, stringConst);
        clinitBody.getUnits().addFirst(assignStmt);
      }

    }
    return i;
  }

}
