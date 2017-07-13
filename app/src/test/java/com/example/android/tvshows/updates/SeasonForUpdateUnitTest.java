package com.example.android.tvshows.updates;

import com.example.android.tvshows.ui.updates.SeasonForUpdate;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SeasonForUpdateUnitTest {

    @Test
    public void earlierUpdate(){

        SeasonForUpdate seasonForUpdate = new SeasonForUpdate(1,"Season 1",1,6,7,2017);
        SeasonForUpdate seasonForUpdate1 = new SeasonForUpdate(1,"Season 2",1,7,7,2017);

        assertTrue(seasonForUpdate.earlierUpdate(7,7,2017));
        assertTrue(seasonForUpdate.earlierUpdate(3,8,2017));
        assertTrue(seasonForUpdate.earlierUpdate(1,6,2018));
        assertFalse(seasonForUpdate.earlierUpdate(7,10,2016));
        assertFalse(seasonForUpdate.earlierUpdate(2,7,2017));
        assertFalse(seasonForUpdate.earlierUpdate(7,5,2017));

        assertTrue(seasonForUpdate.earlierUpdate(seasonForUpdate1));

    }


}
