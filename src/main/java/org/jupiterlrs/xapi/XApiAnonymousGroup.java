package org.jupiterlrs.xapi;

public class XApiAnonymousGroup extends XApiActor {
    XApiAgent[] members;

    public XApiAnonymousGroup() {
        this.objectType = "Group";
    }
}
