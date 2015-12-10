package com.education.util;

import com.education.ws.util.WSUtility;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yzzhao on 12/9/15.
 */
public class WSUtilityTest {

    @Test
    public void testVideoFileCheck(){
        WSUtility  utility = new WSUtility();
        Assert.assertEquals(true, utility.whetherVideo("aaaa.mp4"));
        Assert.assertEquals(false, utility.whetherVideo("aaaa.jpg"));
        Assert.assertEquals(true, utility.whetherVideo("aaaa.Mp4"));
        Assert.assertEquals(false, utility.whetherVideo("aaaa.Mp41"));
    }

}
