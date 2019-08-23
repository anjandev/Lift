//  <Lift: A free software weight lifting tracking app>
//  Copyright (C) <2019> <Anjandev Momi>
//  email: anjan@momi.ca
//
//  Lift is free software: you can redistribute it and/or modify it under the terms of the
//  GNU General Public License as published by the Free Software Foundation, either version 3 of
//  the License, or (at your option) any later version.
//
//  Lift is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
//  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//  See the GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License along with this program.
//  If not, see <https://www.gnu.org/licenses/>.

package ca.momi.lift;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.view.View;

public class WorkTimer {

    CountDownTimer timer;
    ToneGenerator tone;

    public WorkTimer(final View view, WorkTimer currentWork){

        if (currentWork != null) {
            currentWork.timer.cancel();
            currentWork.tone.release();
        }

        final long[] rest = (new AssignedExcers(MainActivity.program)).rest();
        final long MILISECONDS_TO_MINUTES = 1000 *60;
        final int BEEP_TIME = 400;
        final ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
        tone = toneG;


        timer = new CountDownTimer(rest[1], rest[0]) {
            @Override
            public void onTick(long millisUntilFinished) {
                Snackbar.make(view, "Rest 3 minutes if that was hard. Rest 1.5 minutes if that was easy. " +
                        "You have rested " + ((float)(rest[1]-millisUntilFinished)/ (float) MILISECONDS_TO_MINUTES)
                        + " Minutes", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, BEEP_TIME);
            }

            @Override
            public void onFinish() {
                Snackbar.make(view, "You have fully rested " + rest[1]/MILISECONDS_TO_MINUTES + " minutes", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, BEEP_TIME);
            }
        };

        timer.start();
    }
}
