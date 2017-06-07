package com.example.android.tvshows.data.rest;

import com.example.android.tvshows.data.model.Actor;
import com.example.android.tvshows.data.model.ExternalIds;
import com.example.android.tvshows.data.model.ExternalIdsTvShow;
import com.example.android.tvshows.data.model.actortvcredits.ActorTVCredits;
import com.example.android.tvshows.data.model.credits.Credits;
import com.example.android.tvshows.data.model.search.DiscoverResults;
import com.example.android.tvshows.data.model.season.Season;
import com.example.android.tvshows.data.model.tvshowdetailed.TVShowDetailed;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiService {
    // results obtained from a search or discover
    @GET("/3/discover/tv")
    Observable<DiscoverResults> getDiscoverResults(
            // api key in gradle file
            @Query("api_key") String apiKey,
            // popularity.desc, first_air_date.desc, vote_average.desc
            @Query("sort_by") String sortBy,
            // entered as comma separated integers e.g. 18,80
            @Query("with_genres") String withGenres,
            @Query("without_genres") String withoutGenres,
            // 0.0 to 10.0
            @Query("vote_average.gte") String minVoteAverage,
            // greater than 0
            @Query("vote_count.gte") String minVoteCount,
            // enter the year
            @Query("first_air_date.gte") String firstAirDateAfter,
            @Query("first_air_date.lte") String firstAirDateBefore,
            @Query("page") String pageNumber);

    @GET("/3/search/tv")
    Observable<DiscoverResults> getSearchResults(
            // api key in gradle file
            @Query("api_key") String apiKey,
            @Query("query") String query);

    // detailed tv show search
    @GET("/3/tv/{show_id}")
    Observable<TVShowDetailed> getTVShowDetailed(
            @Path(value = "show_id",encoded = true)String showId,
            // api key in gradle file
            @Query("api_key") String apiKey);

    // external ids for a tv show
    @GET("/3/tv/{show_id}/external_ids")
    Observable<ExternalIdsTvShow> getTVShowExternalIds(
            @Path(value = "show_id",encoded = true)String showId,
            @Query("api_key") String apiKey);

    // recommendations for a tv show
    @GET("/3/tv/{show_id}/recommendations")
    Observable<DiscoverResults> getRecommendations(
            @Path(value = "show_id",encoded = true)String showId,
            // api key in gradle file
            @Query("api_key") String apiKey,
            @Query("page") String pageNumber);

    //credits for a tv show
    @GET("/3/tv/{show_id}/credits")
    Observable<Credits> getCredits(
            @Path(value = "show_id",encoded = true)String showId,
            // api key in gradle file
            @Query("api_key") String apiKey,
            @Query("page") String pageNumber);

    //season details for a tv show
    @GET("/3/tv/{show_id}/season/{season}")
    Observable<Season> getSeason(
            @Path(value = "show_id",encoded = true)String showId,
            @Path(value = "season",encoded = true)String season,
            // api key in gradle file
            @Query("api_key") String apiKey,
            @Query("page") String pageNumber);

    @GET("/3/tv/{show_id}/season/{season_number}/episode/{episode_number}/external_ids")
    Observable<ExternalIdsTvShow> getEpisodeExternalIds(
            @Path(value = "show_id",encoded = true)String showId,
            @Path(value = "season_number",encoded = true)String season,
            @Path(value = "episode_number",encoded = true)String episode,
            @Query("api_key") String apiKey);

    // external ids for an episode
    @GET("/3/person/{person_id}/external_ids")
    Observable<ExternalIds> getExternalIds(
            @Path(value = "person_id",encoded = true)String personId,
            @Query("api_key") String apiKey);

    @GET("/3/person/{person_id}")
    Observable<Actor> getActorDetails(
            @Path(value = "person_id",encoded = true)String personId,
            @Query("api_key") String apiKey);

    @GET("/3/person/{person_id}/tv_credits")
    Observable<ActorTVCredits> getActorTVCredits(
            @Path(value = "person_id",encoded = true)String personId,
            @Query("api_key") String apiKey);


}
