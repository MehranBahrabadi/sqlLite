package com.example.sqllite;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FlowerAdapter extends ArrayAdapter<Flower> {
    private Activity activity;
    private List<Flower> flowerList;
    private FlowerDbHelper dbHelper;
    public FlowerAdapter(Activity activity, @NonNull List<Flower> flowerList) {
        super(activity, R.layout.flower_list_item, flowerList);
        this.activity = activity;
        this.flowerList = flowerList;
        this.dbHelper = new FlowerDbHelper(activity);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.flower_list_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.fill(position);
        return  convertView;
    }

    public class ViewHolder{
        ImageView flowerImage , iconMore;
        TextView flowerName,flowerCategory,flowerPrice;

        public ViewHolder(View view) {
            flowerImage = view.findViewById(R.id.flower_image);
            flowerName = view.findViewById(R.id.flower_name);
            flowerCategory = view.findViewById(R.id.flower_category);
            flowerPrice = view.findViewById(R.id.flower_price);
            iconMore = view.findViewById(R.id.icon_more);
        }

        public void fill(int position){
            Flower flower = flowerList.get(position);
            flowerName.setText(flower.getName());
            flowerPrice.setText(flower.getPrice() + " $");
            flowerCategory.setText(flower.getCategory());
            String photoName = flower.getPhoto();
            if(photoName.contains(".")){
                photoName = photoName.substring(0,photoName.lastIndexOf('.'));
            }
            int imageResId = activity.getResources().getIdentifier(photoName,"drawable",
                    activity.getApplicationContext().getPackageName());
            flowerImage.setImageResource(imageResId);

            // icon more
            PopupMenu popup = new PopupMenu(iconMore.getContext() , iconMore);
            popup.inflate(R.menu.flower_popup_menu);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    int id = menuItem.getItemId();
                    if(id == R.id.popup_option_delete){
                        dbHelper.deleteFlower(flower.getProductId());
                        flowerList.remove(position);
                        notifyDataSetChanged();
                    }
                    else if(id == R.id.popup_option_edit){
                        showUpdateDialog(flower);
                    }
                    return false;
                }


            });
            iconMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup.show();
                }
            });

        }
        private void showUpdateDialog(Flower flower) {
            Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.update);
            changeDialogSize(dialog);
            Button btnSubmit = dialog.findViewById(R.id.btn_submit);
            EditText inputName = dialog.findViewById(R.id.input_name);
            EditText inputCategory = dialog.findViewById(R.id.input_category);
            EditText inputPrice = dialog.findViewById(R.id.input_price);
            EditText inputInstructions = dialog.findViewById(R.id.input_instructions);

            // fill inputs
            inputName.setText(flower.getName());
            inputCategory.setText(flower.getCategory());
            inputInstructions.setText(flower.getInstructions());
            inputPrice.setText(String.valueOf(flower.getPrice()));
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pr = inputPrice.getText().toString().trim();
                    Double price = pr.isEmpty() ? 0.0 : Double.valueOf(pr);
                    flower.setName(inputName.getText().toString().trim());
                    flower.setCategory(inputCategory.getText().toString().trim());
                    flower.setInstructions(inputInstructions.getText().toString().trim());
                    flower.setPrice(price);
                    ContentValues cv = flower.getContentValuesForDb();
                    cv.remove(Flower.KEY_ID);   // optional
                    dbHelper.update(flower.getProductId() , cv);
                    Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        private Point getScreenSize(Activity activity){
            Point size = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(size);
            return size;
        }

        private void changeDialogSize(Dialog dialog){
            Point scrSize = getScreenSize(activity);
            if(dialog.getWindow() != null){
                dialog.getWindow().setLayout((int) (0.9*scrSize.x), ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        }
    }
}
