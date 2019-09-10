package soot.jimple.toolkits.typing;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Local;
import soot.Modifier;
import soot.Scene;
import soot.SootClass;
import soot.Type;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.typing.fast.BytecodeHierarchy;
import soot.jimple.toolkits.typing.fast.Typing;
import soot.options.Options;

/**
 * @author brenner
 * 
 *         JUnit-Tests for the Minimization-Method of the Typing-Class
 */
public class TypingMinimizeTest {

  /*********************************************
   * Analysis of 29 (Typing-)Apps:
   *********************************************
   *
   * Most common Classes for Typings:
   * 
   * <pre>
   * java.io.Serializable: 4308 
   * java.lang.Comparable: 4163 
   * java.util.RandomAccess: 238 
   * java.util.AbstractList: 138
   * java.lang.Object: 188 
   * android.view.View: 109 
   * java.util.List: 172 
   * java.lang.Number: 198 
   * java.lang.Cloneable: 102
   * </pre>
   * 
   * ---> mostly Java and Android Classes (mostly Interfaces)
   *
   ***********************************************
   * 
   * ---> Common Pairs:
   * 
   * [I] = 771 [A, I] = 357 [B, I] = 80 [I, O]= 79 [A, I, O] = 40
   * 
   * Most common Typing-Class-Pair:
   * 
   * <pre>
   * 2 LocalType: $u8#772 unknown value:java.io.Serializable
   * 2 LocalType: $u2#523 unknown value:java.lang.Comparable
   * 
   * 8 LocalType: $u4#62 unknown value:java.io.Serializable
   * 9 LocalType: $u11#39 unknown value:java.lang.Number
   * 9 LocalType: $u2#23 unknown value:java.lang.Comparable
   * 
   * 1 LocalType: $u1#18 unknown value:java.util.RandomAccess
   * 1 LocalType: $u0#7 unknown value:java.util.List
   * 2 LocalType: $u1#18 unknown value:java.util.AbstractList
   * 0 LocalType: $u2#9 unknown value:java.lang.Object
   * 
   * 0 LocalType: $u1#4 unknown value:java.lang.Cloneable
   * 1 LocalType: $u1#4 unknown value:java.io.Serializable
   * 2 LocalType: $u1#4 unknown value:java.util.AbstractMap
   * 
   * 0 LocalType: $u0#6 unknown
   * 1 LocalType: $u0#6 unknown value:com.facebook.ads.internal.view.c.c.c
   * 2 LocalType: $u0#6 unknown value:android.view.TextureView
   * 
   * 0 LocalType: $u9#8 unknown value:java.lang.Object
   * 1 LocalType: $u9#8 unknown
   * 2 LocalType: $u9#8 unknown value:android.widget.Adapter
   * </pre>
   * 
   **********************************************
   * 
   * ---> If Typing-Classes are related, then:
   * 
   * 1. (Java) Interface and AbstractClass 2. JavaClass and Abstract 3. Interface and Interface
   * 
   *
   */

  private static final Logger logger = LoggerFactory.getLogger(TypingMinimizeTest.class);

  Type stringType;
  Type integerType;

  Type serializableType;
  Type comparableType;

  Type numberType;
  Type cloneableType;
  Type processType;

  Type randomAccessType;
  Type listType;
  Type abstractListType;
  Type abstractMapType;

  Type objectType;

  // generated Types
  Type interfaceType;
  Type abstractClass_Interface1Type;
  Type abstractClass_Interface2Type;
  Type class_AbstractInterfaceClassType;
  Type class_InterfaceType;
  Type interfaceInterfaceType;
  Type abstractType;
  Type class_AbstractType;
  Type fatherClassType;
  Type childClassType;

  @Before
  public void init() {

    Options o = Options.v();
    o.prepend_classpath();
    o.set_include_all(true);
    o.set_whole_program(true);

    Scene.v().loadNecessaryClasses();
    Scene.v().loadClassAndSupport("java.lang.Object");

    generateClasses();

  }

  private void generateClasses() {

    SootClass sClass = new SootClass("Interface", Modifier.INTERFACE);
    Scene.v().addClass(sClass);

    sClass = new SootClass("AbstractClass_Interface1", Modifier.ABSTRACT);
    sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
    sClass.addInterface(Scene.v().getSootClass("Interface"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("AbstractClass_Interface2", Modifier.ABSTRACT);
    sClass.setSuperclass(Scene.v().getSootClass("AbstractClass_Interface1"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("InterfaceInterface", Modifier.INTERFACE);
    sClass.addInterface(Scene.v().getSootClass("Interface"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("Class_Interface", Modifier.PUBLIC);
    sClass.addInterface(Scene.v().getSootClass("Interface"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("Class_AbstractInterfaceClass", Modifier.PUBLIC);
    sClass.setSuperclass(Scene.v().getSootClass("AbstractClass_Interface2"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("Abstract", Modifier.ABSTRACT);
    sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("Class_Abstract", Modifier.ABSTRACT);
    sClass.setSuperclass(Scene.v().getSootClass("Abstract"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("FatherClass", Modifier.ABSTRACT);
    sClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
    Scene.v().addClass(sClass);

    sClass = new SootClass("ChildClass", Modifier.ABSTRACT);
    sClass.setSuperclass(Scene.v().getSootClass("FatherClass"));
    Scene.v().addClass(sClass);

    stringType = Scene.v().getType("java.lang.String");
    integerType = Scene.v().getType("java.lang.Integer");

    serializableType = Scene.v().getType("java.io.Serializable");
    comparableType = Scene.v().getType("java.lang.Comparable");

    numberType = Scene.v().getType("java.lang.Number");
    cloneableType = Scene.v().getType("java.lang.Cloneable");
    processType = Scene.v().getType("java.lang.Process");

    randomAccessType = Scene.v().getType("java.util.RandomAccess");
    listType = Scene.v().getType("java.util.List");
    abstractListType = Scene.v().getType("java.util.AbstractList");
    abstractMapType = Scene.v().getType("java.util.AbstractMap");

    objectType = Scene.v().getType("java.lang.Object");

    interfaceType = Scene.v().getType("Interface");
    abstractClass_Interface1Type = Scene.v().getType("AbstractClass_Interface1");
    abstractClass_Interface2Type = Scene.v().getType("AbstractClass_Interface2");
    class_AbstractInterfaceClassType = Scene.v().getType("Class_AbstractInterfaceClass");
    class_InterfaceType = Scene.v().getType("Class_Interface");
    interfaceInterfaceType = Scene.v().getType("InterfaceInterface");
    abstractType = Scene.v().getType("Abstract");
    class_AbstractType = Scene.v().getType("Class_Abstract");
    fatherClassType = Scene.v().getType("FatherClass");
    childClassType = Scene.v().getType("ChildClass");

  }

  @Test
  public void testMostCommonTypingPairs_1() {

    logger.debug("Starting Object Random Minimize");

    List<Typing> typingList = new ArrayList<>();
    Type Type1 = serializableType;
    Type Type2 = comparableType;
    Local x1 = new JimpleLocal("$x1", null);
    Typing resultTyping;

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, Type1);
    typingList.add(typing1);
    resultTyping = typing1;

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, Type2);
    typingList.add(typing2);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(2, typingList.size());
    assertEquals(resultTyping, typingList.get(0));
  }

  @Test
  public void testMostCommonTypingPairs_2() {

    logger.debug("Starting Object Random Minimize");

    List<Typing> typingList = new ArrayList<>();

    Type Type1 = serializableType;
    Type Type2 = comparableType;
    Type Type3 = numberType;
    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, Type1);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, Type2);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, Type3);
    typingList.add(typing3);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(2, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing2, typing3));
  }

  @Test
  public void testMostCommonTypingPairs_3() {

    List<Typing> typingList = new ArrayList<>();
    Type Type1 = randomAccessType;
    Type Type2 = listType;
    Type Type3 = abstractListType;
    Type Type4 = objectType;
    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, Type1);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, Type2);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, Type3);
    typingList.add(typing3);

    Typing typing4 = new Typing(Arrays.asList(x1));
    typing4.set(x1, Type4);
    typingList.add(typing4);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(2, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing1, typing3));
  }

  @Test
  public void testMostCommonTypingPairs_4() {

    logger.debug("Starting Object Random Minimize");

    List<Typing> typingList = new ArrayList<>();

    Type Type1 = cloneableType;
    Type Type2 = serializableType;
    Type Type3 = abstractMapType;

    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, Type1);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, Type2);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, Type3);
    typingList.add(typing3);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(3, typingList.size());

  }

  @Test
  public void testHugeCommonTypingPair() {

    /*
     * In this test, no minimization can take place with the current minimization method. Because "Serializable" and
     * "Comparable" are on the same hierarchy level and can be used for any local in any typing. This leads, for example, to
     * the 16384 typings of the Telegram app.
     */

    List<Typing> typingList = new ArrayList<>();

    Type Type1 = serializableType;
    Type Type2 = comparableType;
    Local x1 = new JimpleLocal("$x1", null);
    Local x2 = new JimpleLocal("$x2", null);
    Local x3 = new JimpleLocal("$x3", null);

    Typing typing1 = new Typing(Arrays.asList(x1, x2, x3));
    typing1.set(x1, Type1);
    typing1.set(x2, Type1);
    typing1.set(x3, Type1);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1, x2, x3));
    typing2.set(x1, Type2);
    typing2.set(x2, Type1);
    typing2.set(x3, Type1);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1, x2, x3));
    typing3.set(x1, Type1);
    typing3.set(x2, Type2);
    typing3.set(x3, Type1);
    typingList.add(typing3);

    Typing typing4 = new Typing(Arrays.asList(x1, x2, x3));
    typing4.set(x1, Type1);
    typing4.set(x2, Type1);
    typing4.set(x3, Type2);
    typingList.add(typing4);

    Typing typing5 = new Typing(Arrays.asList(x1, x2, x3));
    typing5.set(x1, Type2);
    typing5.set(x2, Type2);
    typing5.set(x3, Type1);
    typingList.add(typing5);

    Typing typing6 = new Typing(Arrays.asList(x1, x2, x3));
    typing6.set(x1, Type2);
    typing6.set(x2, Type1);
    typing6.set(x3, Type2);
    typingList.add(typing6);

    Typing typing7 = new Typing(Arrays.asList(x1, x2, x3));
    typing7.set(x1, Type1);
    typing7.set(x2, Type2);
    typing7.set(x3, Type2);
    typingList.add(typing7);

    Typing typing8 = new Typing(Arrays.asList(x1, x2, x3));
    typing8.set(x1, Type2);
    typing8.set(x2, Type2);
    typing8.set(x3, Type2);
    typingList.add(typing8);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(8, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing1, typing2, typing3, typing4, typing5, typing6, typing7, typing8));
  }

  @Test
  public void testAbstractInterfaceTyping() {

    List<Typing> typingList = new ArrayList<>();

    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, interfaceType);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, abstractClass_Interface2Type);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, class_AbstractInterfaceClassType);
    typingList.add(typing3);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(1, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing3));
  }

  @Test
  public void testAbstractAbstractTyping() {

    logger.debug("Starting Object Random Minimize");

    List<Typing> typingList = new ArrayList<>();
    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, interfaceType);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, abstractClass_Interface1Type);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, abstractClass_Interface2Type);
    typingList.add(typing3);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(1, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing3));
  }

  @Test
  public void testJavaInterfaceTyping() {

    List<Typing> typingList = new ArrayList<>();

    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, interfaceType);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, integerType);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, numberType);
    typingList.add(typing3);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(2, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing2, typing1));
  }

  @Test
  public void testInterfaceInterfaceTyping() {

    List<Typing> typingList = new ArrayList<>();

    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, interfaceType);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, interfaceInterfaceType);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, numberType);
    typingList.add(typing3);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(2, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing2, typing3));
  }

  @Test
  public void testAllRelatedClassesTyping() {

    /*
     * All Types of Classes with a relationship
     * 
     * Object --> StringType Comparable --> StringType AbstractClass_Interface1Type --> AbstractClass_Interface2Type -->
     * Class_AbstractInterfaceClassType
     * 
     */

    List<Typing> typingList = new ArrayList<>();
    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, objectType);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, stringType);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, comparableType);
    typingList.add(typing3);

    Typing typing4 = new Typing(Arrays.asList(x1));
    typing4.set(x1, abstractClass_Interface2Type);
    typingList.add(typing4);

    Typing typing5 = new Typing(Arrays.asList(x1));
    typing5.set(x1, class_AbstractInterfaceClassType);
    typingList.add(typing5);

    Typing typing6 = new Typing(Arrays.asList(x1));
    typing6.set(x1, abstractClass_Interface1Type);
    typingList.add(typing6);

    Typing typing7 = new Typing(Arrays.asList(x1));
    typing7.set(x1, class_InterfaceType);
    typingList.add(typing7);

    Typing typing8 = new Typing(Arrays.asList(x1));
    typing8.set(x1, abstractType);
    typingList.add(typing8);

    Typing typing9 = new Typing(Arrays.asList(x1));
    typing9.set(x1, class_AbstractType);
    typingList.add(typing9);

    Typing typing10 = new Typing(Arrays.asList(x1));
    typing10.set(x1, fatherClassType);
    typingList.add(typing10);

    Typing typing11 = new Typing(Arrays.asList(x1));
    typing11.set(x1, childClassType);
    typingList.add(typing11);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(5, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing2, typing5, typing7, typing9, typing11));
  }

  @Test
  public void testAllNonRelatedClassesTyping() {

    // All Classes which aren't related to each other (except Object)

    List<Typing> typingList = new ArrayList<>();
    Local x1 = new JimpleLocal("$x1", null);

    Typing typing1 = new Typing(Arrays.asList(x1));
    typing1.set(x1, objectType);
    typingList.add(typing1);

    Typing typing2 = new Typing(Arrays.asList(x1));
    typing2.set(x1, stringType);
    typingList.add(typing2);

    Typing typing3 = new Typing(Arrays.asList(x1));
    typing3.set(x1, cloneableType);
    typingList.add(typing3);

    Typing typing4 = new Typing(Arrays.asList(x1));
    typing4.set(x1, integerType);
    typingList.add(typing4);

    Typing typing5 = new Typing(Arrays.asList(x1));
    typing5.set(x1, processType);
    typingList.add(typing5);

    Typing typing6 = new Typing(Arrays.asList(x1));
    typing6.set(x1, interfaceType);
    typingList.add(typing6);

    Typing typing7 = new Typing(Arrays.asList(x1));
    typing7.set(x1, abstractType);
    typingList.add(typing7);

    Typing typing8 = new Typing(Arrays.asList(x1));
    typing8.set(x1, fatherClassType);
    typingList.add(typing8);

    Typing.minimize(typingList, new BytecodeHierarchy());

    assertEquals(7, typingList.size());
    assertThat(typingList, containsInAnyOrder(typing2, typing3, typing4, typing5, typing6, typing7, typing8));
  }

}
