package soot.jimple.parser.node;

import ca.mcgill.sable.util.*;
import soot.jimple.parser.analysis.*;

public final class AShortBaseType extends PBaseType
{
    private TShort _short_;

    public AShortBaseType()
    {
    }

    public AShortBaseType(
        TShort _short_)
    {
        setShort(_short_);

    }
    public Object clone()
    {
        return new AShortBaseType(
            (TShort) cloneNode(_short_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAShortBaseType(this);
    }

    public TShort getShort()
    {
        return _short_;
    }

    public void setShort(TShort node)
    {
        if(_short_ != null)
        {
            _short_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _short_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_short_);
    }

    void removeChild(Node child)
    {
        if(_short_ == child)
        {
            _short_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_short_ == oldChild)
        {
            setShort((TShort) newChild);
            return;
        }

    }
}
