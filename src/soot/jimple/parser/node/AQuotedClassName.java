package soot.jimple.parser.node;

import ca.mcgill.sable.util.*;
import soot.jimple.parser.analysis.*;

public final class AQuotedClassName extends PClassName
{
    private TQuotedName _quotedName_;

    public AQuotedClassName()
    {
    }

    public AQuotedClassName(
        TQuotedName _quotedName_)
    {
        setQuotedName(_quotedName_);

    }
    public Object clone()
    {
        return new AQuotedClassName(
            (TQuotedName) cloneNode(_quotedName_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAQuotedClassName(this);
    }

    public TQuotedName getQuotedName()
    {
        return _quotedName_;
    }

    public void setQuotedName(TQuotedName node)
    {
        if(_quotedName_ != null)
        {
            _quotedName_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _quotedName_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_quotedName_);
    }

    void removeChild(Node child)
    {
        if(_quotedName_ == child)
        {
            _quotedName_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_quotedName_ == oldChild)
        {
            setQuotedName((TQuotedName) newChild);
            return;
        }

    }
}
