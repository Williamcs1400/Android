package com.william.requisicoeshttp.api;

import com.william.requisicoeshttp.model.CEP;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/json/")
    Call<CEP> getCEP(@Path("cep") String cep);

}
