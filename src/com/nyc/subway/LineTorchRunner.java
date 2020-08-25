package com.nyc.subway;

import java.util.*;

public class LineTorchRunner {
    public static void main(String[] args) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                /**
                 * http://web.mta.info/status/serviceStatus.txt
                 */
            	HttpUrlConnectionToMTA.doPostOrGet(LineUtils.MTA_SERVICE_STATUS_URL, "");
            }
        };
        Timer timer = new Timer("MTATimer");// create a new Timer
        timer.scheduleAtFixedRate(timerTask, 10, 10000);// this line starts the timer at the same time its executed
    }
}
