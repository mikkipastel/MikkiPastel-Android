package co.meappdev.mikkipastel.manager;

import co.meappdev.mikkipastel.DAO.MikkiBlog;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    //@GET("posts?fetchImages=true&orderBy=published&key=AIzaSyBHCzo2LyvwWc4utLoONgiuDCO96yMFF2U&fetchBodies=false")
    @GET("posts?fetchImages=true&maxResults=60&orderBy=published&key=AIzaSyBHCzo2LyvwWc4utLoONgiuDCO96yMFF2U&fetchBodies=false")
    Call<MikkiBlog> loadContentList();
}
