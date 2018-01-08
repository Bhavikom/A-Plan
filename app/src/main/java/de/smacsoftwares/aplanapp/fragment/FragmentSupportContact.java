package de.smacsoftwares.aplanapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;


public class FragmentSupportContact extends Fragment implements View.OnClickListener
{
    private Menu menu;
    ArrayList<Bitmap> arraylistBitmap = new ArrayList<>();
    FragmentSetting activity;
    Context context;
    Bitmap bitmap1,bitmap2,bitmap3;
    View view;
    private static final int RESULT_LOAD_IMAGE1 = 1;
    private static final int RESULT_LOAD_IMAGE2 = 2;
    private static final int RESULT_LOAD_IMAGE3 = 3;
    ImageView imageViewScreenshot1, imageViewScreenshot2, imageviewScreenshot3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_support_contact, container, false);
        context = getActivity();
        initControl();
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        return view;
    }
    //    this method intialize control and class all initial work
    public void initControl()
    {
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        imageViewScreenshot1 = (ImageView) view.findViewById(R.id.imageviewscreenshot1);
        imageViewScreenshot2 = (ImageView) view.findViewById(R.id.imageviewscreenshot2);
        imageviewScreenshot3 = (ImageView) view.findViewById(R.id.imageviewscreenshot3);

        imageViewScreenshot1.setOnClickListener(this);
        imageViewScreenshot2.setOnClickListener(this);
        imageviewScreenshot3.setOnClickListener(this);
        getActivity().setTitle(getResources().getString(R.string.contect ));

        Bitmap bit = BitmapFactory.decodeResource(context.getResources(), R.drawable.date_icon);
        imageViewScreenshot1.setImageBitmap(bit);
        imageViewScreenshot2.setImageBitmap(bit);
        imageviewScreenshot3.setImageBitmap(bit);

        arraylistBitmap = new ArrayList<>();

        bitmap1=((BitmapDrawable)imageViewScreenshot1.getDrawable()).getBitmap();
        bitmap2=((BitmapDrawable)imageViewScreenshot2.getDrawable()).getBitmap();
        bitmap3=((BitmapDrawable)imageviewScreenshot3.getDrawable()).getBitmap();
    }
    @Override
    public void onClick(View v)
    {
        if(v==imageViewScreenshot1){
            Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent1, RESULT_LOAD_IMAGE1);
        }
        if(v==imageViewScreenshot2){
            Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent1, RESULT_LOAD_IMAGE2);
        }
        if(v==imageviewScreenshot3){
            Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent2, RESULT_LOAD_IMAGE3);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.toolbar_menu_item, menu);
        this.menu = menu;
        menu.findItem(R.id.actionEdit).setVisible(false);
        menu.findItem(R.id.actionDefault).setVisible(false);
        menu.findItem(R.id.actionSend).setVisible(true);
        menu.findItem(R.id.actionprofile).setVisible(false);
        menu.findItem(R.id.actionSupport).setVisible(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (item.getItemId())
        {
            case R.id.actionSend:

                return true;
            case R.id.actionSupport:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE1 && resultCode == getActivity().RESULT_OK && null != data)
        {
            Uri selectedImg = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor1 = getActivity().getContentResolver().query(selectedImg,
                    filePathColumn, null, null, null);
            cursor1.moveToFirst();
            int columnIndex = cursor1.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor1.getString(columnIndex);

            imageViewScreenshot1.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            bitmap1=((BitmapDrawable)imageViewScreenshot1.getDrawable()).getBitmap();
            cursor1.close();
        }
        else if (requestCode == RESULT_LOAD_IMAGE2 && resultCode == getActivity().RESULT_OK && null != data)
        {
            Uri selectedImg = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor2 = getActivity().getContentResolver().query(selectedImg,
                    filePathColumn, null, null, null);
            cursor2.moveToFirst();
            int columnIndex = cursor2.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor2.getString(columnIndex);

            imageViewScreenshot2.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            bitmap2=((BitmapDrawable)imageViewScreenshot2.getDrawable()).getBitmap();
            cursor2.close();
        }
        else if (requestCode == RESULT_LOAD_IMAGE3 && resultCode == getActivity().RESULT_OK && null != data)
        {
            Uri selectedImg = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor3 = getActivity().getContentResolver().query(selectedImg,
                    filePathColumn, null, null, null);
            cursor3.moveToFirst();
            int columnIndex = cursor3.getColumnIndex(filePathColumn[0]);

            String picturePath = cursor3.getString(columnIndex);

            imageviewScreenshot3.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            bitmap3=((BitmapDrawable)imageviewScreenshot3.getDrawable()).getBitmap();
            cursor3.close();
        }
    }
}



