package soot.jimple.toolkits.annotation.purity;

/*-
 * #%L
 * Soot - a J*va Optimization Framework
 * %%
 * Copyright (C) 2005 Antoine Mine
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

/**
 * A node representing the this parameter.
 */
public class PurityThisNode extends PurityParamNode {

  public static PurityThisNode node = new PurityThisNode();

  private PurityThisNode() {
    super(-1);
  }

  @Override
  public String toString() {
    return "this";
  }

  @Override
  public boolean isInside() {
    return false;
  }

  @Override
  public boolean isLoad() {
    return false;
  }

  @Override
  public boolean isParam() {
    return true;
  }
}
