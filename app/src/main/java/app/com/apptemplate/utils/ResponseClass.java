package app.com.apptemplate.utils;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by steve on 29/04/2015.
 */
public class ResponseClass {
    public String responseStatus;
    public String responseText;
    public List<?> responseData;

    public ResponseClass(ArrayList<?> dataArray)
    {
        responseData=dataArray;
    }

}
