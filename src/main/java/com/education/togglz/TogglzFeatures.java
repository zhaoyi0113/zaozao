package com.education.togglz;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

/**
 * Created by yzzhao on 12/17/15.
 */
public enum TogglzFeatures implements Feature {
    @Label("WE_CHAT")
    WE_CHAT;

    public boolean isWeChatEnabled(){
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
