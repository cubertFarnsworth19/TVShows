package com.example.android.tvshows.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.tvshows.data.db.ShowsRepository;
import com.example.android.tvshows.data.model.credits.Cast;
import com.example.android.tvshows.data.model.credits.Credits;
import com.example.android.tvshows.data.model.season.Episode;
import com.example.android.tvshows.data.model.season.Season;
import com.example.android.tvshows.data.model.tvshowdetailed.CreatedBy;
import com.example.android.tvshows.data.model.tvshowdetailed.Genre;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;
import com.example.android.tvshows.ui.episodes.EpisodeData;
import com.example.android.tvshows.ui.myshows.current.CurrentDatabaseLoad;
import com.example.android.tvshows.ui.myshows.shows.ShowInfo;
import com.example.android.tvshows.ui.showinfo.cast.CastInfo;
import com.example.android.tvshows.ui.showinfo.details.DetailsData;
import com.example.android.tvshows.ui.showinfo.seasons.SeasonInfo;
import com.example.android.tvshows.ui.updates.TVShow;
import com.example.android.tvshows.util.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class SQLiteTest {

    private ShowsRepository mRepository;

    private Integer mYear;
    private Integer mMonth;
    private Integer mDay;

    @Before
    public void setUp(){
        deleteDatabase();

        mYear = Utility.getYear();
        mMonth = Utility.getMonth();
        mDay = Utility.getDay();

        mRepository = new ShowsRepository(InstrumentationRegistry.getTargetContext());
        insertShow1();
        insertShow2();

    }

    @Test
    public void testInitialInsertion()  {

        ArrayList<ShowInfo> shows = mRepository.getAllShows(false,false);
        assertTrue(shows.size()==2);
        assertTrue(shows.get(0).getTitle().equals("TV Show 1"));
        assertTrue(shows.get(1).getTitle().equals("TV Show 2"));
        ArrayList<ShowInfo> showsContinuing = mRepository.getAllShows(true,false);
        assertTrue(showsContinuing.size()==1);
        assertTrue(showsContinuing.get(0).getTitle().equals("TV Show 2"));

        ArrayList<Integer> showIds = mRepository.getAllShowIds();
        assertTrue(showIds.size()==2);
        assertTrue(showIds.get(0)==1);
        assertTrue(showIds.get(1)==2);
        ArrayList<SeasonInfo> seasons = mRepository.getSeasons(1);
        assertTrue(seasons.size()==2);
        assertTrue(seasons.get(0).seasonName.equals("show 1 season 2"));
        assertTrue(seasons.get(1).seasonName.equals("show 1 season 1"));
        ArrayList<EpisodeData> episodes = mRepository.getEpisodes(1,1);
        assertTrue(episodes.size()==2);
        assertTrue(episodes.get(0).getEpisodeName().equals("s1s1e1"));
        assertTrue(episodes.get(1).getEpisodeName().equals("s1s1e2"));
        ArrayList<CastInfo> cast = mRepository.getCast(1);
        assertTrue(cast.size()==2);
        assertTrue(cast.get(0).getActorName().equals("cast 1"));
        assertTrue(cast.get(1).getActorName().equals("cast 2"));
        ArrayList<String> creators = mRepository.getCreators(1);
        assertTrue(creators.size()==1);
        assertTrue(creators.get(0).equals("Creator name 1"));
        ArrayList<String> creators2 = mRepository.getCreators(2);
        assertTrue(creators2.size()==2);
        assertTrue(creators2.get(0).equals("Creator name 1"));
        assertTrue(creators2.get(1).equals("Creator name 2"));
        DetailsData showDetails = mRepository.getShow(1);
        assertTrue(showDetails.name.equals("TV Show 1"));
        assertTrue(showDetails.userScore.equals((8.8)));
        assertTrue(showDetails.voteCount.equals(1034));

    }

    @Test
    public void testAddNewSeason(){
        ArrayList<Episode> s1s2episodes = new ArrayList<>();
        s1s2episodes.add(new Episode("s1s3e1",8.2,10,3,"2017-03-04"));
        s1s2episodes.add(new Episode("s1s3e2",8.2,10,3,"2017-03-11"));

        Season[] seasons = new Season[1];
        Season s3 = new Season("show 1 season 3",s1s2episodes,3);
        seasons[0] = s3;

        mRepository.updateSeasons(1,seasons);
        ArrayList<SeasonInfo> seasonsUpdated = mRepository.getSeasons(1);
        assertTrue(seasonsUpdated.size()==3);
        assertTrue(seasonsUpdated.get(0).seasonName.equals("show 1 season 3"));
        assertTrue(seasonsUpdated.get(0).numberOfEpisodes.equals("2"));
        assertTrue(seasonsUpdated.get(0).seasonNumber==3);
        //DetailsData showDetails = mRepository.getShow(1);
    }

    @Test
    public void testUpdateTVShowDetails(){
        TVShowDetailed tvShowDetailed = new TVShowDetailed();
        tvShowDetailed.setId(1);
        tvShowDetailed.setName("TV Show 1");
        tvShowDetailed.setInProduction(false);
        tvShowDetailed.setVoteAverage(8.8);
        tvShowDetailed.setVoteCount(1034);
        tvShowDetailed.setPopularity(6.0);
        tvShowDetailed.setNumberOfEpisodes(4);

        ArrayList<com.example.android.tvshows.data.model.tvshowdetailed.Season> seasons = new ArrayList<>();
        seasons.add(new com.example.android.tvshows.data.model.tvshowdetailed.Season());
        seasons.add(new com.example.android.tvshows.data.model.tvshowdetailed.Season());
        seasons.add(new com.example.android.tvshows.data.model.tvshowdetailed.Season());
        tvShowDetailed.setSeasons(seasons);
        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(16,"Animation"));
        genres.add(new Genre(18,"Drama"));
        genres.add(new Genre(80,"Crime"));
        tvShowDetailed.setGenres(genres);

        ArrayList<Cast> cast = new ArrayList<>();
        cast.add(new Cast("cast 1",1));
        cast.add(new Cast("cast 2",2));

        ArrayList<CreatedBy> createdBy = new ArrayList<>();
        createdBy.add(new CreatedBy(1001,"Creator name 1"));
        tvShowDetailed.setCreatedBy(createdBy);

        Credits credits = new Credits(100,cast);
        mRepository.updateTVShowDetails(tvShowDetailed,credits);
        ArrayList<ShowInfo> shows = mRepository.getAllShows(false,false);
        assertTrue(shows.size()==2);
        assertTrue(shows.get(0).getTitle().equals("TV Show 1"));
        assertTrue(shows.get(1).getTitle().equals("TV Show 2"));
        DetailsData showDetails = mRepository.getShow(1);
        assertTrue(showDetails.animation);
        assertTrue(showDetails.drama);
        assertTrue(showDetails.crime);
    }

    @Test
    public void testCurrent(){
        ArrayList<CurrentDatabaseLoad> currentLast = mRepository.getEpisodesLastMonth();

        assertTrue(currentLast.size()==2);
        assertTrue(currentLast.get(0).episodeName.equals("s2s1e2"));
        assertTrue(currentLast.get(1).episodeName.equals("s2s1e1"));
        ArrayList<CurrentDatabaseLoad> currentNext = mRepository.getEpisodesNextMonth();
        assertTrue(currentLast.size()==2);
        assertTrue(currentNext.get(0).episodeName.equals("s2s1e3"));
        assertTrue(currentNext.get(1).episodeName.equals("s2s1e4"));

    }

    @Test
    public void testSetFavorites(){
        mRepository.setFavorite(1,true);
        ArrayList<ShowInfo> shows = mRepository.getAllShows(false,true);
        assertTrue(shows.size()==1);
        assertTrue(shows.get(0).getTitle().equals("TV Show 1"));

        mRepository.setFavorite(1,false);
        shows = mRepository.getAllShows(false,false);
        assertTrue(shows.size()==2);
        assertTrue(shows.get(0).getTitle().equals("TV Show 1"));
        assertTrue(shows.get(1).getTitle().equals("TV Show 2"));
    }

    @Test
    public void testGetAllUpdates(){
        ArrayList<TVShow> tvShows = mRepository.getAllShowsUpdate();
        assertTrue(tvShows.get(0).updateDay==mDay);
        assertTrue(tvShows.get(0).updateMonth==mMonth);
        assertTrue(tvShows.get(0).updateYear==mYear);
    }

    @Test
    public void testDeleteShow()  {
        mRepository.deleteShow(2);
        ArrayList<ShowInfo> shows = mRepository.getAllShows(false,false);
        assertTrue(shows.size()==1);
        assertTrue(shows.get(0).getTitle().equals("TV Show 1"));
        ArrayList<String> creators = mRepository.getCreators(1);
        assertTrue(creators.size()==1);
        assertTrue(creators.get(0).equals("Creator name 1"));
    }

    @After
    public void finish(){
        deleteDatabase();
    }

    private void deleteDatabase(){
        InstrumentationRegistry.getTargetContext().deleteDatabase("shows.db");
    }

    private void insertShow1(){
        TVShowDetailed tvShowDetailed = new TVShowDetailed();
        tvShowDetailed.setId(1);
        tvShowDetailed.setName("TV Show 1");
        tvShowDetailed.setInProduction(false);
        tvShowDetailed.setVoteAverage(8.8);
        tvShowDetailed.setVoteCount(1034);
        tvShowDetailed.setPopularity(6.0);
        tvShowDetailed.setNumberOfEpisodes(4);

        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(16,"Animation"));
        genres.add(new Genre(18,"Drama"));
        tvShowDetailed.setGenres(genres);

        ArrayList<Cast> cast = new ArrayList<>();
        cast.add(new Cast("cast 1",1));
        cast.add(new Cast("cast 2",2));

        ArrayList<CreatedBy> createdBy = new ArrayList<>();
        createdBy.add(new CreatedBy(1001,"Creator name 1"));
        tvShowDetailed.setCreatedBy(createdBy);

        Credits credits = new Credits(100,cast);

        ArrayList<Episode> s1s1episodes = new ArrayList<>();
        s1s1episodes.add(new Episode("s1s1e1",8.2,10,1,"2015-06-02"));
        s1s1episodes.add(new Episode("s1s1e2",8.2,10,1,"2015-06-09"));

        ArrayList<Episode> s1s2episodes = new ArrayList<>();
        s1s2episodes.add(new Episode("s1s2e1",8.2,10,2,"2016-06-03"));
        s1s2episodes.add(new Episode("s1s2e2",8.2,10,2,"2016-06-10"));

        Season[] seasons = new Season[2];
        Season s1 = new Season("show 1 season 1",s1s1episodes,1);
        Season s2 = new Season("show 1 season 2",s1s2episodes,2);

        seasons[0] = s1;
        seasons[1] = s2;

        mRepository.insertShowIntoDatabase(tvShowDetailed,credits,seasons);
    }

    private void insertShow2(){
        TVShowDetailed tvShowDetailed = new TVShowDetailed();
        tvShowDetailed.setId(2);
        tvShowDetailed.setName("TV Show 2");
        tvShowDetailed.setInProduction(true);
        tvShowDetailed.setVoteAverage(8.8);
        tvShowDetailed.setVoteCount(1034);
        tvShowDetailed.setPopularity(6.0);
        tvShowDetailed.setNumberOfEpisodes(4);

        ArrayList<Genre> genres = new ArrayList<>();
        genres.add(new Genre(16,"Animation"));
        genres.add(new Genre(18,"Drama"));
        tvShowDetailed.setGenres(genres);

        ArrayList<Cast> cast = new ArrayList<>();
        cast.add(new Cast("cast 3",1));
        cast.add(new Cast("cast 4",2));

        ArrayList<CreatedBy> createdBy = new ArrayList<>();
        createdBy.add(new CreatedBy(1001,"Creator name 1"));
        createdBy.add(new CreatedBy(1002,"Creator name 2"));
        tvShowDetailed.setCreatedBy(createdBy);

        Credits credits = new Credits(100,cast);

        int[] date2 = getPreviousDate(Utility.getDay(),Utility.getMonth(),Utility.getYear());
        int[] date1 = getPreviousDate(date2[0],date2[1],date2[2]);

        int[] date3 = getNextDate(Utility.getDay(),Utility.getMonth(),Utility.getYear());
        int[] date4 = getNextDate(date3[0],date3[1],date3[2]);

        ArrayList<Episode> s2s1episodes = new ArrayList<>();
        s2s1episodes.add(new Episode("s2s1e1",8.2,10,1,getDateAsString(date1[0],date1[1],date1[2])));
        s2s1episodes.add(new Episode("s2s1e2",8.2,10,1,getDateAsString(date2[0],date2[1],date2[2])));
        s2s1episodes.add(new Episode("s2s1e3",8.2,10,2,getDateAsString(date3[0],date3[1],date3[2])));
        s2s1episodes.add(new Episode("s2s1e4",8.2,10,2,getDateAsString(date4[0],date4[1],date4[2])));

        Season[] seasons = new Season[1];
        Season s1 = new Season("show 2 season 1",s2s1episodes,1);

        seasons[0] = s1;

        mRepository.insertShowIntoDatabase(tvShowDetailed,credits,seasons);
    }

    private String getDateAsString(Integer d,Integer m,Integer y){
        String strM,strD;

        if(m<10) strM = "0"+m.toString();
        else strM = m.toString();

        if(d<10) strD = "0"+d.toString();
        else strD = d.toString();

        return y.toString() + "-" + strM + "-" + strD;
    }

    private int[] getNextDate(Integer d,Integer m,Integer y){
        int[] nextDate = new int[3];

        if(!endOfMonth(d,m,y)) {
            nextDate[0] = d + 1;
            nextDate[1] = m;
            nextDate[2] = y;
        }
        else if(m<12) {
            nextDate[0] = 1;
            nextDate[1] = m+1;
            nextDate[2] = y;
        }
        else {
            nextDate[0] = 1;
            nextDate[1] = 1;
            nextDate[2] = y+1;
        }

        return nextDate;
    }

    private boolean endOfMonth(int d,int m,int y){
        if(d<28)
            return false;
        else if(m==2){
            if(d==28 && isLeapYear(y))
                return false;
            else
                return true;
        }
        else if(d<30)
            return false;
        else if (d==30 && isMonth31Days(m))
            return false;
        else
            return true;
    }

    private boolean isMonth31Days(int m){
        return m==1 || m==3 || m==5 || m==7 || m==8 || m==10 || m==12;
    }

    private boolean isLeapYear(int y){
        if(y%4!=0)
            return false;
        else if(y%100==0 && y%400!=0)
            return false;
        else
            return true;
    }

    private int[] getPreviousDate(Integer d,Integer m,Integer y){
        int[] previousDate = new int[3];
        if(d==1 && m!=1){
            previousDate[2] = y;
            previousDate[1] = m-1;
            if(isMonth31Days(m-1)){
                previousDate[0] = 31;
            }
            else if(m-1==2){
                previousDate[0] = isLeapYear(m-1) ? 29 : 28;
            }else
                previousDate[0] = 30;
        }
        else if(d==1){
            previousDate[2] = y-1;
            previousDate[1] = 12;
            previousDate[0] = 31;
        }
        else {
            previousDate[2] = y;
            previousDate[1] = m;
            previousDate[0] = d-1;
        }

        return previousDate;
    }

}
