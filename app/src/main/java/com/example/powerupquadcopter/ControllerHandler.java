package com.example.powerupquadcopter;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.security.Key;

public class ControllerHandler {

    public static boolean btnA, btnB, btnX, btnY,   //  primary buttons
                        btnStart, btnSelect,        //  those 2 middle ones
                        btnL, btnR, btnU, btnD,     //  directional pad (d-pad)
                        btnTL, btnTR;               //  L/R trigger bumpers (not the triggers)
    public static double joyX, joyY, trigL, trigR;

    public static void initialize() {
        //  reset all variables
        btnA = btnB = btnX = btnY = btnStart = btnSelect = btnL = btnR =
                btnU = btnD = btnTL = btnTR = false;
        joyX = joyY = trigL = trigR = 0;
    }

    //  this function handles joystick events and is called by the Main Activity
    public static boolean motionEventHandler(MotionEvent ev) {
        if( (ev.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK){
            joyX = ev.getAxisValue(MotionEvent.AXIS_HAT_X);
            joyY = ev.getAxisValue(MotionEvent.AXIS_HAT_Y);
            trigL = ev.getAxisValue(MotionEvent.AXIS_LTRIGGER);
            trigR = ev.getAxisValue(MotionEvent.AXIS_RTRIGGER);
            return true;
        }
        return false;
    }

    //  this function handles key events and is called by the Main Activity
    public static boolean dispatchEventHandler(KeyEvent ev) {
        if((ev.getSource() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
        {
            switch(ev.getKeyCode()) {
                case KeyEvent.KEYCODE_BUTTON_A:
                    btnA = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_B:
                    btnB = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_X:
                    btnX = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_Y:
                    btnY = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_L1:
                    btnTL = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R1:
                    btnTR = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    btnL = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    btnR = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:
                    btnU = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    btnD = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    public static boolean isControllerConnected() {
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            //  Verify that the device has both gamepad buttons and control sticks
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    && ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                return true;
            }
        }
        return true;
    }

}
