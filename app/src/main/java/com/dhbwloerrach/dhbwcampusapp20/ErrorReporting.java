package com.dhbwloerrach.dhbwcampusapp20;

import android.app.Activity;
import android.widget.Toast;

public class ErrorReporting {
    private static ErrorReporting errorReporting;
    private Activity context;

    public static void Initialize(Activity context)
    {
        errorReporting= new ErrorReporting(context);
    }

    public static void NewContext(Activity context)
    {
        if(errorReporting==null)
            Initialize(context);
        errorReporting._NewContext(context);
    }

    public static void NewError(int Error)
    {
        errorReporting._NewError(Error);
    }

    public ErrorReporting(Activity context)
    {
        this.context=context;
    }

    private void _NewContext(Activity context)
    {
        this.context=context;
    }

    private void _NewError(final int Error)
    {
        if(context!=null)
        context.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, Errors.Errors[Error],Toast.LENGTH_LONG).show();
            }
        });
    }

    public static abstract class Errors
    {
        public static final int NETWORK=0, XML=1, OFFLINE=2,Video=3;
        private static final int[] Errors={R.string.ErrorR_Network,R.string.ErrorR_XML,R.string.ErrorR_OFFLINE,R.string.ErrorR_Video};
    }
}
