package com.example.powerupquadcopter;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class ControllerHandler {

    public static boolean btnA, btnB, btnX, btnY,   //  primary buttons
                        btnStart, btnSelect,        //  those 2 middle ones
                        btnL, btnR, btnU, btnD,     //  directional pad (d-pad)
                        btnBL, btnBR,               //  L/R trigger bumpers (above triggers)
                        btnTL, btnTR,               //  L/R triggers
            btnJL, btnJR;               //  Joystick buttons
    public static double joyLX, joyLY, joyRX, joyRY;

    public static void initialize() {
        //  reset all variables
        btnA = btnB = btnX = btnY = btnStart = btnSelect = btnL = btnR =
                btnU = btnD = btnBL = btnBR = btnTL = btnTR = false;
        joyLX = joyLY = joyRX = joyRY = 0;
    }

    //  this function handles joystick events and is called by the Main Activity
    public static boolean motionEventHandler(MotionEvent ev) {
        if( (ev.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK){
            joyLX = ev.getAxisValue(MotionEvent.AXIS_X);
            joyLY = ev.getAxisValue(MotionEvent.AXIS_Y);
            joyRX = ev.getAxisValue(MotionEvent.AXIS_Z);
            joyRY = -ev.getAxisValue(MotionEvent.AXIS_RZ);
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
                    btnBL = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R1:
                    btnBR = ev.getAction() == KeyEvent.ACTION_DOWN;
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
                case KeyEvent.KEYCODE_BUTTON_L2:
                    btnTL = ev.getAction() == KeyEvent.ACTION_DOWN;
                    break;
                case KeyEvent.KEYCODE_BUTTON_R2:
                     btnTR = ev.getAction() == KeyEvent.ACTION_DOWN;
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

    public static byte[] getButtonPacket() {
        boolean[] btnArray = new boolean[16];
        btnArray[15] = btnTL;
        btnArray[14] = btnTR;
        btnArray[13] = btnBL;
        btnArray[12] = btnBR;
        btnArray[11] = btnStart;
        btnArray[10] = btnSelect;
        btnArray[9] = btnJL;
        btnArray[8] = btnJR;
        btnArray[7] = btnA;
        btnArray[6] = btnB;
        btnArray[5] = btnX;
        btnArray[4] = btnY;
        btnArray[3] = btnU;
        btnArray[2] = btnR;
        btnArray[1] = btnD;
        btnArray[0] = btnL;

        byte one = 0, two = 0;
        for(int i = 0; i < 8; i++) {
            if(btnArray[i+8]) one |= 1 << i;
            if(btnArray[i]) two |= 1 << i;
        }

        return new byte[] {one, two};
    }

    public static byte[] getAxesPacket() {
        byte[] packet
        btnArray[0] 
    }

}
