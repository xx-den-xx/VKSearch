package ru.bda.vksearch.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import ru.bda.vksearch.model.SearchAnswer;
import ru.bda.vksearch.view.MainActivityView;

/**
 * email: denbelobaba@gmail.com
 *
 * @author Belobaba Denis
 */

public class MainActivityPresenter {

    private MainActivityView view;
    private int offset = 0;
    private int count = 20;
    private boolean isNewLoad = true;

    public MainActivityPresenter(MainActivityView view) {
        this.view = view;
    }

    public void startAuthorized(Activity runningActivity) {
        VKSdk.login(runningActivity, new String[] {});
    }

    private VKRequest.VKRequestListener requestListener = new VKRequest.VKRequestListener() {
        @Override
        public void onComplete(VKResponse response) {
            super.onComplete(response);
            offset = offset + count;
            String jsonString = response.json.toString();
            Gson gson = new Gson();
            SearchAnswer answer = gson.fromJson(jsonString, SearchAnswer.class);
            view.loadRecyclerData(answer, isNewLoad);
            view.stopProgress();
        }

        @Override
        public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
            super.attemptFailed(request, attemptNumber, totalAttempts);
        }

        @Override
        public void onError(VKError error) {
            super.onError(error);
            view.stopProgress();
            view.authError(error);
        }

        @Override
        public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
            super.onProgress(progressType, bytesLoaded, bytesTotal);
            view.startProgress();
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                view.authComplete(res);
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                view.authError(error);
            }
        }));
    }

    public void vkRequestFirst(String search) {
        offset = 0;
        isNewLoad = true;
        vkRequest(search, isNewLoad);
    }

    public void vkRequest(String search, boolean isNewLoad) {
        this.isNewLoad = isNewLoad;
        view.startProgress();
        VKRequest request = VKApi.users().search(VKParameters.from(
                VKApiConst.Q, search,
                VKApiConst.SORT, 0,
                VKApiConst.OFFSET, offset,
                VKApiConst.COUNT, count,
                VKApiConst.FIELDS, "id, first_name, last_name, city, country, photo_50, photo_100" +
                        "photo_200_orig, photo_200, photo_400_orig, photo_max, photo_max_orig, online"));
        request.executeWithListener(requestListener);
    }
}
