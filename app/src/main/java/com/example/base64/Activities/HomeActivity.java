package com.example.base64.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.base64.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Button action;
    public static int FILE_CODE= 2;
    public static int GALLERY_CODE= 3;
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        action = findViewById(R.id.action);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertScreen cdd = new AlertScreen(HomeActivity.this);
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if (resultCode==RESULT_OK){
                    String path = data.getData().getPath();
                    Log.d("mymy", "onActivityResult:" + data);
                    textView.setText(path);


                }
                break;

            case 3:
                if(resultCode==RESULT_OK){
                    Uri selectedImageUri = data.getData();
                    Log.d("pathpath", "onActivityResult: " + data);
                    String picturePath = getPath(getApplicationContext(), selectedImageUri);
                    String name = picturePath.substring(picturePath.lastIndexOf("/")+1);
                    textView.setText(name);


                    Log.d("Picture Path",picturePath);

                    Bitmap bmp = null;
                    ByteArrayOutputStream bos = null;
                    byte[] bt = null;
                    String encodeString = null;
                    try{
                        bmp = BitmapFactory.decodeFile(picturePath);
                        bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        bt = bos.toByteArray();
                        encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
                        Log.d("base69", "onActivityResult: "+encodeString);
                        Api(encodeString,name);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }



        }
    }
    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close();
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }
    private void Api(String encodeString,String fileName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", "1001");
            jsonObject.put("name", "himnshu");
            jsonObject.put("userName","hip,mm");
            jsonObject.put("bio", "mutthal");
            jsonObject.put("filename",fileName );
            jsonObject.put("base64", encodeString);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://hotelshridadaji.in/ReachMe_API/api/?p=editProfile", jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("ll", "" + response);
                        try {
                            if (response.getString("code").equalsIgnoreCase("200") && response.getString("status").equalsIgnoreCase("success")) {

                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("TAG", "onErrorResponse: Error " + error);
                //  Toast.makeText(loader_home_screen.getContext(), error.toString(), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authkey", "asdf");
                return params;
            }

        };
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

}


