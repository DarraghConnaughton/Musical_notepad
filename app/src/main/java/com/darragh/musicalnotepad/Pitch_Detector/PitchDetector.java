package com.darragh.musicalnotepad.Pitch_Detector;

import android.view.View;
import android.widget.TextView;

import com.darragh.musicalnotepad.Cluster.ClusterNode;
import com.darragh.musicalnotepad.Cluster.kMeans;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchDetector {
    public static Thread t;
    private AudioDispatcher dispatcher;
    private static ArrayList<String> list,note;
    private static ArrayList<Integer> note_length;

    public AudioDispatcher getDispatcher(){
        return dispatcher;
    }

    public PitchDetector(){
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
    }
    public Thread getThread(){
        return t;
    }

    public void recordAudio(){
        list = new ArrayList<>();
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent audioEvent) {
                final float pitchInHz = result.getPitch();
                System.out.println(hz_to_note(pitchInHz,1));
                list.add(hz_to_note(pitchInHz,1));
            }
        };

        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(p);
        t = new Thread(dispatcher, "Audio Dispatcher");
        t.start();
    }

    private static void concatenate(){
        ArrayList<String> updatedNotes = new ArrayList<>();
        ArrayList<Integer> updatedLengths = new ArrayList<>();
        int temp_length;
        for(int i=0; i<note.size()-1; i++){
            if(note.get(i).equals(note.get(i+1))) {
                updatedNotes.add(note.get(i));
                updatedLengths.add(note_length.get(i) + note_length.get(i + 1));
                i++;
            } else {
                updatedNotes.add(note.get(i));
                updatedLengths.add(note_length.get(i));
            }
        }
        note=new ArrayList<>(updatedNotes);
        note_length=new ArrayList<>(updatedLengths);
    }

    private void processAudioInput(){
        note = new ArrayList<>();
        note_length = new ArrayList<>();
        String currentNote="";
        int currentLength=0;
        int array_Size= list.size();
        for(int i=0; i<array_Size; i++){
            currentLength++;
            if(i==0){
                currentNote=list.get(i);
            } else if(!currentNote.equals(list.get(i))) {
                if(currentLength>2){
                    note.add(currentNote);
                    note_length.add(currentLength);
                }
                currentLength=0;
                currentNote = list.get(i);
            }
            else if(i==(array_Size-1)){
                note.add(currentNote);
                note_length.add(currentLength);
            }
        }
    }

    public String stopRecording(TextView outputDisplay,KeySignature keySignature, int timeSignature){
        dispatcher.stop();
        outputDisplay.setVisibility(View.VISIBLE);
        processAudioInput();
        concatenate();
        if(note.size()>1){
            return kMeans.main(fillCluster(note,note_length),keySignature,timeSignature);
        }
        return "";
    }

    public static ArrayList<ClusterNode> fillCluster(ArrayList<String> note, ArrayList<Integer> note_length){
        ArrayList<ClusterNode> clusterNodeArrayList = new ArrayList<ClusterNode>();
        for(int i=0; i<note.size(); i++){
            clusterNodeArrayList.add(new ClusterNode(i,note_length.get(i),note.get(i)));
        }
        return clusterNodeArrayList;
    }



    //YOU MIGHT NEED TO DO SOMETHING ABOUT THESE HARDCODED VALUES.
    public String hz_to_note(float frequency,int octave){
        if(frequency>538.808f && frequency<=1077.616f){
            float temp_frequency;
            for(float i=2.0f; i<10.0f; i++){
                temp_frequency = frequency/i;
                if(temp_frequency>=261.626f && temp_frequency<=538.808f){
                    octave = (int)i;
                    frequency = frequency/i;
                    break;
                }
            }
        }
        else if(frequency>=1077.616f){
            float temp_frequency;
            for(float i=4.0f; i<10.0f; i++){
                temp_frequency = frequency/i;
                if(temp_frequency>=254.284f && temp_frequency<=538.808f){
                    octave = (int)(i-1.0f);
                    frequency = frequency/i;
                    break;
                }
            }
        }
        if(frequency>=254.284f && frequency<=269.4045f){
            return ("C" + octave);
        } else if(frequency>269.4045f && frequency<=285.424f){
            return ("_D" + octave);
        } else if(frequency>285.424f && frequency<=302.396f){
            return ("D" + octave);
        } else if(frequency>302.396f && frequency<=320.3775f){
            return ("_E" + octave);
        } else if(frequency>320.3775f && frequency<=339.428f){
            return ("E" + octave);
        } else if(frequency>339.428f && frequency<=359.611f){
            return ("F" + octave);
        } else if(frequency>359.611f && frequency<=380.9945f){
            return ("_G" + octave);
        } else if(frequency>380.9945f && frequency<=403.65f){
            return ("G" + octave);
        } else if(frequency>403.65f && frequency<=427.6525f){
            return ("_A" + octave);
        } else if(frequency>427.6525f && frequency<=453.082f){
            return ("A" + octave);
        } else if(frequency>453.082f && frequency<=480.0236f){
            return ("_B" + octave);
        } else if(frequency>480.0236f && frequency<=508.567f){
            return ("B" + octave);
        } else if(frequency>508.567f && frequency<=538.808f){
            return ("C" + (octave+1));
        } else
            return "SPACE";
    }
}
