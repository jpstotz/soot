package soot.jimple.parser.node;

import ca.mcgill.sable.util.*;
import soot.jimple.parser.analysis.*;

public final class AClassNameBaseType extends PBaseType
{
    private PClassName _className_;

    public AClassNameBaseType()
    {
    }

    public AClassNameBaseType(
        PClassName _className_)
    {
        setClassName(_className_);

    }
    public Object clone()
    {
        return new AClassNameBaseType(
            (PClassName) cloneNode(_className_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAClassNameBaseType(this);
    }

    public PClassName getClassName()
    {
        return _className_;
    }

    public void setClassName(PClassName node)
    {
        if(_className_ != null)
        {
            _className_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _className_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_className_);
    }

    void removeChild(Node child)
    {
        if(_className_ == child)
        {
            _className_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_className_ == oldChild)
        {
            setClassName((PClassName) newChild);
            return;
        }

    }
}
