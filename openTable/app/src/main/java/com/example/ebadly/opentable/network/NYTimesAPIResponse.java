package com.example.ebadly.opentable.network;

import com.example.ebadly.opentable.dataModels.Movie;
import java.util.List;

public class NYTimesAPIResponse {

    private String status;
    private String copyright;
    private boolean has_more;
    private int num_results;
    private List<Movie> results;

    public NYTimesAPIResponse(String status, String copyright, boolean has_more, int num_results, List<Movie> results) {
        this.status = status;
        this.copyright = copyright;
        this.has_more = has_more;
        this.num_results = num_results;
        this.results = results;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getCopyright() { return copyright; }

    public void setCopyright(String copyright) { this.copyright = copyright; }

    public boolean isHas_more() { return has_more; }

    public void setHas_more(boolean has_more) { this.has_more = has_more; }

    public int getNum_results() { return num_results; }

    public void setNum_results(int num_results) { this.num_results = num_results; }

    public List<Movie> getResults() { return results; }

    public void setResults(List<Movie> results) { this.results = results; }
}
