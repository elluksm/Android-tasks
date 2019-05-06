package lv.pd1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CameraFragment extends Fragment  implements View.OnClickListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView thumbnailView;
    String mCurrentPhotoPath;
    private CameraFragment.ImageAdapter adapter;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        thumbnailView = (ImageView) view.findViewById(R.id.thumbView);
        adapter = new ImageAdapter(this.getActivity());

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         public void onItemClick(AdapterView<?> parent, View v,
         int position, long id) {
         Toast.makeText( getActivity(), "" + position,
          Toast.LENGTH_SHORT).show();
           }
          });

        Button b = (Button) view.findViewById(R.id.button);
        b.setOnClickListener(this);

        return view;

    }

    @Override
    /**
     * Delete all images
     */
    public void onDestroy() {
        super.onDestroy();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        this.traverse(storageDir);
    }

    public void traverse (File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    traverse(file);
                } else {
                    // Delete files
                    file.delete();
                }
            }
        }
    }

    /**
     * Run a functiopn on button click, invoke devices camera
     * @param view
     */
    public void onClick(View view) {
        // Just show information that this is working, nothing more
        Toast.makeText(this.getActivity(), "catch an image", Toast.LENGTH_LONG).show();

        // Invoke camera, available on device. Intent calls for available camera applications
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // Create image filename
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // File was created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "lv.pd1.android.fileprovider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("data", photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            thumbnailView.setImageBitmap(myBitmap);
            adapter.addThisBitmap(myBitmap);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Save image taken by camera inside external directory for every application that has permission to view images
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        // References to our images
        ArrayList<Bitmap> mThumbs = new ArrayList<Bitmap>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public void addThisBitmap(Bitmap newmap) {
            mThumbs.add(newmap);
        }

        public int getCount() {
            return mThumbs.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(500, 500));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(mThumbs.get(position));
            return imageView;
        }
    }
}
