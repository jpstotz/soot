package soot.jimple.parser.node;

import ca.mcgill.sable.util.*;
import soot.jimple.parser.analysis.*;

public final class AIdentityNoTypeStatement extends PStatement
{
    private PLocalName _localName_;
    private TColonEquals _colonEquals_;
    private TAtIdentifier _atIdentifier_;
    private TSemicolon _semicolon_;

    public AIdentityNoTypeStatement()
    {
    }

    public AIdentityNoTypeStatement(
        PLocalName _localName_,
        TColonEquals _colonEquals_,
        TAtIdentifier _atIdentifier_,
        TSemicolon _semicolon_)
    {
        setLocalName(_localName_);

        setColonEquals(_colonEquals_);

        setAtIdentifier(_atIdentifier_);

        setSemicolon(_semicolon_);

    }
    public Object clone()
    {
        return new AIdentityNoTypeStatement(
            (PLocalName) cloneNode(_localName_),
            (TColonEquals) cloneNode(_colonEquals_),
            (TAtIdentifier) cloneNode(_atIdentifier_),
            (TSemicolon) cloneNode(_semicolon_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIdentityNoTypeStatement(this);
    }

    public PLocalName getLocalName()
    {
        return _localName_;
    }

    public void setLocalName(PLocalName node)
    {
        if(_localName_ != null)
        {
            _localName_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _localName_ = node;
    }

    public TColonEquals getColonEquals()
    {
        return _colonEquals_;
    }

    public void setColonEquals(TColonEquals node)
    {
        if(_colonEquals_ != null)
        {
            _colonEquals_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _colonEquals_ = node;
    }

    public TAtIdentifier getAtIdentifier()
    {
        return _atIdentifier_;
    }

    public void setAtIdentifier(TAtIdentifier node)
    {
        if(_atIdentifier_ != null)
        {
            _atIdentifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _atIdentifier_ = node;
    }

    public TSemicolon getSemicolon()
    {
        return _semicolon_;
    }

    public void setSemicolon(TSemicolon node)
    {
        if(_semicolon_ != null)
        {
            _semicolon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _semicolon_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_localName_)
            + toString(_colonEquals_)
            + toString(_atIdentifier_)
            + toString(_semicolon_);
    }

    void removeChild(Node child)
    {
        if(_localName_ == child)
        {
            _localName_ = null;
            return;
        }

        if(_colonEquals_ == child)
        {
            _colonEquals_ = null;
            return;
        }

        if(_atIdentifier_ == child)
        {
            _atIdentifier_ = null;
            return;
        }

        if(_semicolon_ == child)
        {
            _semicolon_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_localName_ == oldChild)
        {
            setLocalName((PLocalName) newChild);
            return;
        }

        if(_colonEquals_ == oldChild)
        {
            setColonEquals((TColonEquals) newChild);
            return;
        }

        if(_atIdentifier_ == oldChild)
        {
            setAtIdentifier((TAtIdentifier) newChild);
            return;
        }

        if(_semicolon_ == oldChild)
        {
            setSemicolon((TSemicolon) newChild);
            return;
        }

    }
}
