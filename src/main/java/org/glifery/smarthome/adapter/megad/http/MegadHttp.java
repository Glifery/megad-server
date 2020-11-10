package org.glifery.smarthome.adapter.megad.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.glifery.smarthome.application.configuration.MegadConfig;
import org.glifery.smarthome.application.port.MegadGatewayInterface;
import org.glifery.smarthome.domain.model.megad.MegadId;
import org.glifery.smarthome.domain.model.megad.Operation;
import org.glifery.smarthome.domain.model.megad.PortAction;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class MegadHttp implements MegadGatewayInterface {
    private MegadConfig megadConfig;
    private Map<String, MegadHttpApi> apis;

    public MegadHttp(MegadConfig megadConfig) {
        this.megadConfig = megadConfig;
        this.apis = megadConfig.getControllers()
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        controller -> createApi(controller.getValue())
                ));
    }

    public String sendCommand(MegadId megadId, Operation operation) throws IOException {
        MegadHttpApi megadHttpApi = getApiById(megadId);
        Response<String> response = megadHttpApi.storeProfile(megadConfig.getCommon().getPassword(), operation.toString()).execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.message());
        }

        return response.body();
    }

    private MegadHttpApi getApiById(MegadId megadId) throws RuntimeException {
        return Optional.ofNullable(apis.get(megadId.toString())).orElseThrow(() -> new RuntimeException(String.format("Api for %s not found", megadId)));
    }

    private MegadHttpApi createApi(MegadConfig.ControllerConfig config) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(config.getHost())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(MegadHttpApi.class);
    }
}
