package soot.jimple.parser.node;

import ca.mcgill.sable.util.*;
import soot.jimple.parser.analysis.*;

public final class AMultiNewExpr extends PNewExpr
{
    private TNewmultiarray _newmultiarray_;
    private TLParen _lParen_;
    private PBaseType _baseType_;
    private TRParen _rParen_;
    private final LinkedList _arrayDescriptor_ = new TypedLinkedList(new ArrayDescriptor_Cast());

    public AMultiNewExpr()
    {
    }

    public AMultiNewExpr(
        TNewmultiarray _newmultiarray_,
        TLParen _lParen_,
        PBaseType _baseType_,
        TRParen _rParen_,
        List _arrayDescriptor_)
    {
        setNewmultiarray(_newmultiarray_);

        setLParen(_lParen_);

        setBaseType(_baseType_);

        setRParen(_rParen_);

        {
            Object temp[] = _arrayDescriptor_.toArray();
            for(int i = 0; i < temp.length; i++)
            {
                this._arrayDescriptor_.add(temp[i]);
            }
        }

    }

    public AMultiNewExpr(
        TNewmultiarray _newmultiarray_,
        TLParen _lParen_,
        PBaseType _baseType_,
        TRParen _rParen_,
        XPArrayDescriptor _arrayDescriptor_)
    {
        setNewmultiarray(_newmultiarray_);

        setLParen(_lParen_);

        setBaseType(_baseType_);

        setRParen(_rParen_);

        if(_arrayDescriptor_ != null)
        {
            while(_arrayDescriptor_ instanceof X1PArrayDescriptor)
            {
                this._arrayDescriptor_.addFirst(((X1PArrayDescriptor) _arrayDescriptor_).getPArrayDescriptor());
                _arrayDescriptor_ = ((X1PArrayDescriptor) _arrayDescriptor_).getXPArrayDescriptor();
            }
            this._arrayDescriptor_.addFirst(((X2PArrayDescriptor) _arrayDescriptor_).getPArrayDescriptor());
        }

    }
    public Object clone()
    {
        return new AMultiNewExpr(
            (TNewmultiarray) cloneNode(_newmultiarray_),
            (TLParen) cloneNode(_lParen_),
            (PBaseType) cloneNode(_baseType_),
            (TRParen) cloneNode(_rParen_),
            cloneList(_arrayDescriptor_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMultiNewExpr(this);
    }

    public TNewmultiarray getNewmultiarray()
    {
        return _newmultiarray_;
    }

    public void setNewmultiarray(TNewmultiarray node)
    {
        if(_newmultiarray_ != null)
        {
            _newmultiarray_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _newmultiarray_ = node;
    }

    public TLParen getLParen()
    {
        return _lParen_;
    }

    public void setLParen(TLParen node)
    {
        if(_lParen_ != null)
        {
            _lParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lParen_ = node;
    }

    public PBaseType getBaseType()
    {
        return _baseType_;
    }

    public void setBaseType(PBaseType node)
    {
        if(_baseType_ != null)
        {
            _baseType_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _baseType_ = node;
    }

    public TRParen getRParen()
    {
        return _rParen_;
    }

    public void setRParen(TRParen node)
    {
        if(_rParen_ != null)
        {
            _rParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _rParen_ = node;
    }

    public LinkedList getArrayDescriptor()
    {
        return _arrayDescriptor_;
    }

    public void setArrayDescriptor(List list)
    {
        Object temp[] = list.toArray();
        for(int i = 0; i < temp.length; i++)
        {
            _arrayDescriptor_.add(temp[i]);
        }
    }

    public String toString()
    {
        return ""
            + toString(_newmultiarray_)
            + toString(_lParen_)
            + toString(_baseType_)
            + toString(_rParen_)
            + toString(_arrayDescriptor_);
    }

    void removeChild(Node child)
    {
        if(_newmultiarray_ == child)
        {
            _newmultiarray_ = null;
            return;
        }

        if(_lParen_ == child)
        {
            _lParen_ = null;
            return;
        }

        if(_baseType_ == child)
        {
            _baseType_ = null;
            return;
        }

        if(_rParen_ == child)
        {
            _rParen_ = null;
            return;
        }

        if(_arrayDescriptor_.remove(child))
        {
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_newmultiarray_ == oldChild)
        {
            setNewmultiarray((TNewmultiarray) newChild);
            return;
        }

        if(_lParen_ == oldChild)
        {
            setLParen((TLParen) newChild);
            return;
        }

        if(_baseType_ == oldChild)
        {
            setBaseType((PBaseType) newChild);
            return;
        }

        if(_rParen_ == oldChild)
        {
            setRParen((TRParen) newChild);
            return;
        }

        for(ListIterator i = _arrayDescriptor_.listIterator(); i.hasNext();)
        {
            if(i.next() == oldChild)
            {
                if(newChild != null)
                {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }

                i.remove();
                oldChild.parent(null);
                return;
            }
        }

    }

    private class ArrayDescriptor_Cast implements Cast
    {
        public Object cast(Object o)
        {
            PArrayDescriptor node = (PArrayDescriptor) o;

            if((node.parent() != null) &&
                (node.parent() != AMultiNewExpr.this))
            {
                node.parent().removeChild(node);
            }

            if((node.parent() == null) ||
                (node.parent() != AMultiNewExpr.this))
            {
                node.parent(AMultiNewExpr.this);
            }

            return node;
        }
    }
}
