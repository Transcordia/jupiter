package org.jupiterlrs.xapi;

import java.util.HashMap;

public class XApiLanguageMap extends HashMap<String, String> {
    public XApiLanguageMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public XApiLanguageMap(int initialCapacity) {
        super(initialCapacity);
    }

    public XApiLanguageMap() {
    }
}
