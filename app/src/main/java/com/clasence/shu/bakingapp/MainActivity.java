package com.clasence.shu.bakingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.clasence.shu.bakingapp.adapters.RecipeAdapter;
import com.clasence.shu.bakingapp.idlingresource.SimpleIdlingResource;
import com.clasence.shu.bakingapp.models.RecipeHelper;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,LoaderManager.LoaderCallbacks<String>,RecipeAdapter.CustomRecyclerOnClick {

    //define recycler
    private RecyclerView recyclerView;

    //progress bar
    private ProgressBar progressBar;

    //holds any http error
    private String errorMessage="";

    //loader id
    private static int MY_LOADER_ID=100;

    //recipe list and adapter definition
    private ArrayList<RecipeHelper> recipeList;
    private  RecipeAdapter recipeAdapter;

    //recycler manager
    RecyclerView.LayoutManager mLayoutManager;

    public static final String SELECTED_RECIPE = "selected_recipe";
    private static final String RECIPES_LIST = "recipeList";
    private static final String LAYOUT_MAN_STATE = "lay_man_state";
    private static boolean isTablet=false;



    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        progressBar = (ProgressBar) findViewById(R.id.pbload);


        //verify if view if in tablet mode
        if(findViewById(R.id.tabletRecycler)!=null){
            isTablet=true;
        }

        if(isTablet){

            //if in tablet mode, set grid to 3
            recyclerView = (RecyclerView) findViewById(R.id.tabletRecycler);
            mLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        else {
            recyclerView = (RecyclerView) findViewById(R.id.homeRecycler);
            mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, 5, true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }



        //restore state if already saved
        if(savedInstanceState!=null && savedInstanceState.containsKey(RECIPES_LIST)){

            recipeList =  savedInstanceState.getParcelableArrayList(RECIPES_LIST);
            recipeAdapter = new RecipeAdapter(MainActivity.this,recipeList,this);
            recyclerView.setAdapter(recipeAdapter);
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(LAYOUT_MAN_STATE));

        }else {

            recipeList = new ArrayList<>();
            recipeAdapter = new RecipeAdapter(this,recipeList,this);
            recyclerView.setAdapter(recipeAdapter);

            if (CheckInternetConnection.isNetworkAvailable(getApplicationContext())) {

                if (getSupportLoaderManager().getLoader(MY_LOADER_ID) == null) {
                    getSupportLoaderManager().initLoader(MY_LOADER_ID, null, MainActivity.this).forceLoad();
                } else {
                    getSupportLoaderManager().restartLoader(MY_LOADER_ID, null, MainActivity.this).forceLoad();
                }
            } else {
                showDialog(getResources().getString(R.string.message), getResources().getString(R.string.noint), 1);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save parcelable movie list and the position of the current list item
        if(recipeList.size()>0) {
            outState.putParcelableArrayList(RECIPES_LIST, recipeList);
            outState.putParcelable(LAYOUT_MAN_STATE,mLayoutManager.onSaveInstanceState());
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.favs) {
            final Intent intent = new Intent(this, FavouriteActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.share) {
        } else if (id == R.id.exit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(MainActivity.this) {
            HttpURLConnection urlConnection;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progressBar.setVisibility(View.VISIBLE);
               recipeList.clear();
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public String loadInBackground() {
                try {
                    URL url = new URL(getString(R.string.url));
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    //set timeouts to 10s
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setReadTimeout(10000);


                    InputStream is =new BufferedInputStream(urlConnection.getInputStream());

                    //handle url redirects
                    if (!url.getHost().equals(urlConnection.getURL().getHost())) {
                        throw new IOException(getString(R.string.url_redirect_done));
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder response = new StringBuilder();
                    for (String line; (line = br.readLine()) != null; ) {
                        response.append(line + "\n");
                    }


                    return response.toString();
                } catch (SocketTimeoutException e) {
                    errorMessage = getString(R.string.connectiontimeout);
                    e.printStackTrace();
                }
                catch (IOException ee){
                    errorMessage = getString(R.string.unabletoconnect);
                }catch(Exception eee){

                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        //dismiss progress bar
        progressBar.setVisibility(View.GONE);


        //if no error message, movie data was gotten
        if(errorMessage.equalsIgnoreCase("")) {
            Log.i("response",data);
            try {
                JSONArray jsonArray = new JSONArray(data);
                if(jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++) {
                        Log.i("response"+i,data);
                        JSONObject recipes = jsonArray.getJSONObject(i);

                        String id = recipes.getString("id");
                        String name = recipes.getString("name");
                        String ingredients = recipes.getString("ingredients");
                        String steps = recipes.getString("steps");
                        String image = recipes.getString("image");
                        RecipeHelper recipeHelper = new RecipeHelper(-1,id,name,ingredients,steps,image);
                        recipeList.add(recipeHelper);

                    }

                    //notify adapter to update
                    recipeAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else{

            //show error msg
            showDialog(getString(R.string.message),errorMessage,1);
            errorMessage="";

        }

        getSupportLoaderManager().destroyLoader(MY_LOADER_ID);

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }



    /**
     * Function shows a dismissable dialog to the user with important information
     * @param title {@link String} title of dialog
     * @param body {@link String} body of dialog
     * @param type int indicates the action to perform on dialog click
     * */

    private void showDialog(final String title, String body, final int type) {
        //use libary to beautify alertdialog
        final NiftyDialogBuilder dialogBuilder= NiftyDialogBuilder.getInstance(MainActivity.this);
        dialogBuilder.withTitle(title)
                .withIcon(android.R.drawable.ic_dialog_alert)
                .withMessage(body)
                .withMessageColor(getResources().getColor(R.color.colorWhite))
                .withTitleColor(getResources().getColor(R.color.colorWhite))
                .withDialogColor(getResources().getColor(R.color.colorPrimary))
                .withDividerColor(getResources().getColor(R.color.colorWhite2))
                .withButton1Text("cancel")                                      //def gone
                .withButton2Text("Reload")                                  //def gone
                .isCancelableOnTouchOutside(true)
                .withEffect(Effectstype.RotateLeft)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        if(type==1) {
                            if (CheckInternetConnection.isNetworkAvailable(getApplicationContext())) {

                                if(getSupportLoaderManager().getLoader(MY_LOADER_ID)==null){
                                    getSupportLoaderManager().initLoader(MY_LOADER_ID, null, MainActivity.this).forceLoad();
                                }else{
                                    getSupportLoaderManager().restartLoader(MY_LOADER_ID, null, MainActivity.this).forceLoad();
                                }
                            } else {
                                showDialog(getResources().getString(R.string.message), getResources().getString(R.string.noint), 1);
                            }
                        }
                    }
                })
                .show();
    }

    @Override
    public void onCustomClick(int position) {
        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(SELECTED_RECIPE,recipeList.get(position));
        startActivity(intent);

    }
}
