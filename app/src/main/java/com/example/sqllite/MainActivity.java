package com.example.sqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FlowerDbHelper dbHelper;
    ListView listView;
    SearchView searchView;
    List<Flower> flowerList = new ArrayList<>();
    FlowerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new FlowerDbHelper(this);
        listView=findViewById(R.id.list_view);
        searchView = findViewById(R.id.search_view);
        refreshDisplay();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                flowerList = dbHelper.getFlowers(Flower.KEY_NAME + " LIKE '%" + query +"%'",null);
//                refreshDisplay();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                dbHelper.getFlowers(flowerList , Flower.KEY_NAME + " LIKE '%" + newText +"%'",null);
                 adapter.notifyDataSetChanged();
                return false;
            }
        });

        //test
/*        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.i("database", "database opened: ");
        if(db != null && db.isOpen()){
            db.close();
            Log.i("database", "database closed: ");

        }*/
    }
    private void importJson() {
        InputStream input = getResources().openRawResource(R.raw.flowers_json);
        List<Flower> flowers = FlowerJsonParser.parseJson(input);
        Log.i("JsonParser", "FlowerJsonParser : Returned : " + flowers.size() + " items.");
        for (Flower flower : flowers) {
            dbHelper.insertFlower(flower);
        }
    }
    private void refreshDisplay(){
        if (flowerList == null ) flowerList = new ArrayList<>();
        adapter = new FlowerAdapter(this,flowerList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("import flowers.json").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                importJson();
                return false;
            }
        });
        menu.add("Get All Flowers").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                flowerList = dbHelper.getAllFlowers();
                refreshDisplay();
                return false;
            }
        });
        MenuItem itemFancy = menu.add("Fancy");
        itemFancy.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemFancy.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                flowerList=dbHelper.getFlowers(null,null,Flower.KEY_PRICE + " DESC");
                refreshDisplay();
                return false;
            }
        });
        MenuItem itemCheap = menu.add("Cheap");
        itemCheap.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        itemCheap.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                flowerList=dbHelper.getFlowers(null,null,Flower.KEY_PRICE + " ASC");
                refreshDisplay();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}