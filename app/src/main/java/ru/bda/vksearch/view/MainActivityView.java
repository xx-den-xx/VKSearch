package ru.bda.vksearch.view;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKError;

import ru.bda.vksearch.model.SearchAnswer;

/**
 * email: denbelobaba@gmail.com
 *
 * @author Belobaba Denis
 */

public interface MainActivityView{
    void authComplete(VKAccessToken res);
    void authError(VKError error);
    void startProgress();
    void stopProgress();
    void loadRecyclerData(SearchAnswer answer, boolean isNewLoad);
}
