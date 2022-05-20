package com.andrewbfang.bop;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;
/**
 * Created by Doug on 6/13/13.
 */
public class ConstrainedNumberPicker extends NumberPicker {

    public ConstrainedNumberPicker(Context context) {
        super(context);
    }

    public ConstrainedNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
    }

    public ConstrainedNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
    }

    private void processAttributeSet(AttributeSet attrs) {
        //This method reads the parameters given in the xml file and sets the properties according to it
        this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max", 0));
    }
}
