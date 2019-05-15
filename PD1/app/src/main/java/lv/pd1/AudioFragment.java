package lv.pd1;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AudioFragment extends Fragment implements View.OnClickListener  {
    private ListView listView;
    private CustomListAdapter adapter;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private static String mCurrentAudioPath;
    boolean mStartRecording = true;

    private TextView audioText;
    private Button recordButton = null;
    private MediaRecorder recorder = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    public AudioFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_audio, container, false);

        requestPermissions( permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        audioText = (TextView) view.findViewById(R.id.audioText);
        listView = (ListView) view.findViewById(R.id.listviewID);

        ArrayList<String> nameArray = new ArrayList<String>();
        adapter = new CustomListAdapter(this.getActivity(), nameArray);
        listView.setAdapter(adapter);

        recordButton = (Button) view.findViewById(R.id.audioButton);
        recordButton.setOnClickListener(this);

        return view;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        recorder = new MediaRecorder();

        mCurrentAudioPath = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        fileName = "AUDIO_" + timeStamp;
        mCurrentAudioPath += fileName;

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(mCurrentAudioPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        audioText.setText(fileName);

        adapter.addName(fileName);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }


    public void onClick(View view) {
        Toast.makeText(this.getActivity(), "record an audio", Toast.LENGTH_LONG).show();

        onRecord(mStartRecording);
        if (mStartRecording) {
            recordButton.setText("Pārtraukt ierakstu");
        } else {
            recordButton.setText("Ierakstīt");
        }
        mStartRecording = !mStartRecording;

        }

    @Override
    /**
     * Delete all audio files
     */
    public void onDestroy() {
        super.onDestroy();
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
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
    }


