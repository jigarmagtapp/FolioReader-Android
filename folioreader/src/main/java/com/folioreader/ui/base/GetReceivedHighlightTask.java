package com.folioreader.ui.base;

import android.os.AsyncTask;
import com.folioreader.model.HighlightImpl;
import com.folioreader.model.sqlite.HighLightTable;

import java.util.ArrayList;

/**
 * Background task to save received highlights.
 * <p>
 * Created by gautam on 10/10/17.
 */
public class GetReceivedHighlightTask extends AsyncTask<ArrayList<HighlightImpl>, ArrayList<HighlightImpl>, ArrayList<HighlightImpl>> {

    private OnGetHighlight onGetHighlight;
    private String bookID;

    public GetReceivedHighlightTask(OnGetHighlight onGetHighlight, String bookID) {
        this.onGetHighlight = onGetHighlight;
        this.bookID = bookID;
    }

    @SafeVarargs
    @Override
    protected final ArrayList<HighlightImpl> doInBackground(ArrayList<HighlightImpl>... list) {
        return HighLightTable.getAllHighlights(bookID);
    }

    @Override
    protected void onPostExecute(ArrayList<HighlightImpl> list) {
        super.onPostExecute(list);
        onGetHighlight.onFinished(list);
    }
}
