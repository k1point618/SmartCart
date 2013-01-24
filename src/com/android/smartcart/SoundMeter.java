package com.android.smartcart;

import java.io.IOException;

import android.media.MediaRecorder;

public class SoundMeter {
        static final private double EMA_FILTER = 0.6;

        private MediaRecorder mRecorder = null;
        private double mEMA = 0.0;

        public void start() {
                if (mRecorder == null) {
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile("/dev/null"); 
                    try {
						mRecorder.prepare();
					} catch (IOException e) {
						e.printStackTrace();
					}
                    mRecorder.start();
                    mEMA = 0.0;
                }
        }
        
        public void stop() {
                if (mRecorder != null) {
                        mRecorder.stop();       
                        mRecorder.release();
                        mRecorder = null;
                }
        }
        
        public double getAmplitude() {
                if (mRecorder != null)
                        return  (mRecorder.getMaxAmplitude()/2700.0);
                else
                        return 0;

        }

        public double getAmplitudeEMA() {
                double amp = getAmplitude();
                mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
                return mEMA;
        }
}