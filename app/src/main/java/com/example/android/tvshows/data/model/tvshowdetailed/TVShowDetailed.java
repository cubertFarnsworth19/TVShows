
package com.example.android.tvshows.data.model.tvshowdetailed;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TVShowDetailed {

    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("created_by")
    @Expose
    private List<CreatedBy> createdBy = null;
    @SerializedName("episode_run_time")
    @Expose
    private List<Integer> episodeRunTime = null;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = null;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("in_production")
    @Expose
    private Boolean inProduction;
    @SerializedName("languages")
    @Expose
    private List<String> languages = null;
    @SerializedName("last_air_date")
    @Expose
    private String lastAirDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("networks")
    @Expose
    private List<Network> networks = null;
    @SerializedName("number_of_episodes")
    @Expose
    private Integer numberOfEpisodes;
    @SerializedName("number_of_seasons")
    @Expose
    private Integer numberOfSeasons;
    @SerializedName("origin_country")
    @Expose
    private List<String> originCountry = null;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("production_companies")
    @Expose
    private List<ProductionCompany> productionCompanies = null;
    @SerializedName("seasons")
    @Expose
    private List<Season> seasons = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<CreatedBy> getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(List<CreatedBy> createdBy) {
        this.createdBy = createdBy;
    }

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getInProduction() {
        return inProduction;
    }

    public void setInProduction(Boolean inProduction) {
        this.inProduction = inProduction;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    public Integer getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(Integer numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public Integer getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Integer numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public List<String> getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(List<String> originCountry) {
        this.originCountry = originCountry;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getVoteAverageString(){
        String strVoteAverage = "";
        if(voteAverage>0){
            strVoteAverage = voteAverage == 10.0 ? "10" : voteAverage.toString();
        }
        return strVoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }


    //additional methods
    public Integer getSeasonsListSize(){
        return seasons.size();
    }

    public int getFirstAirDateYear(){
        return firstAirDate!=null ? Integer.valueOf(firstAirDate.substring(0,4)) : -1;

    }

    public String getFirstAirDateYearString(){
        if (firstAirDate!=null && firstAirDate.length()>=4)
            return firstAirDate.substring(0,4);
        return "";
    }

    public int getFirstAirDateMonth(){

        return firstAirDate!=null ? Integer.valueOf(firstAirDate.substring(5,7)) : -1;
    }

    public int getFirstAirDateDay(){

        return firstAirDate!=null ? Integer.valueOf(firstAirDate.substring(8)) : -1;
    }

    public int getLastAirDateYear(){

        return lastAirDate!=null ? Integer.valueOf(lastAirDate.substring(0,4)) : -1;
    }

    public int getLastAirDateMonth(){

        return lastAirDate!=null ? Integer.valueOf(lastAirDate.substring(5,7)) : -1;
    }

    public int getLastAirDateDay(){

        return lastAirDate!=null ? Integer.valueOf(lastAirDate.substring(8)) : -1;
    }

    public boolean isActionAdventure(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10759) return true;
        return false;
    }

    public boolean isAnimation(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==16) return true;
        return false;
    }

    public boolean isComedy(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==35) return true;
        return false;
    }

    public boolean isCrime(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==80) return true;
        return false;
    }

    public boolean isDocumentary(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==99) return true;
        return false;
    }

    public boolean isDrama(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==18) return true;
        return false;
    }

    public boolean isFamily(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10751) return true;
        return false;
    }

    public boolean isKids(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10762) return true;
        return false;
    }

    public boolean isMystery(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==9648) return true;
        return false;
    }

    public boolean isNews(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10763) return true;
        return false;
    }

    public boolean isReality(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10764) return true;
        return false;
    }

    public boolean isSciFiFantasy(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10765) return true;
        return false;
    }

    public boolean isSoap(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10766) return true;
        return false;
    }

    public boolean isTalk(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10767) return true;
        return false;
    }

    public boolean isWarPolitics(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==10768) return true;
        return false;
    }

    public boolean isWestern(){
        for(int i=0;i<genres.size();i++)
            if(genres.get(i).getId()==37) return true;
        return false;
    }

    public int numberOfCreators(){
        return createdBy.size();
    }
}
