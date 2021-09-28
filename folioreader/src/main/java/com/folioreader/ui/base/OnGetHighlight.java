package com.folioreader.ui.base;

import com.folioreader.model.HighlightImpl;

import java.util.ArrayList;

/**
 * Created by gautam on 10/10/17.
 */

public interface OnGetHighlight {

    void onFinished(ArrayList<HighlightImpl> list);
}
