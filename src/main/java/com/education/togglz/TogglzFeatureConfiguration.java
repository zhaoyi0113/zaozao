package com.education.togglz;

import org.springframework.stereotype.Component;
import org.togglz.core.Feature;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.file.FileBasedStateRepository;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;

import java.io.File;
import java.net.URL;

/**
 * Created by yzzhao on 12/18/15.
 */
@Component
public class TogglzFeatureConfiguration implements TogglzConfig {
    @Override
    public Class<? extends Feature> getFeatureClass() {
        return TogglzFeatures.class;
    }

    @Override
    public StateRepository getStateRepository() {
        URL url = this.getClass().getClassLoader().getResource("features.properties");
        return new FileBasedStateRepository(new File(url.getFile()));
    }

    @Override
    public UserProvider getUserProvider() {
//        return new UserProvider() {
//            @Override
//            public FeatureUser getCurrentUser() {
//                return new SimpleFeatureUser("admin", true);
//            }
//        };
        return ()-> new SimpleFeatureUser("admin", true);
    }
}
