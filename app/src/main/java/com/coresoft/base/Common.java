package com.coresoft.base;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.coresoft.database.SysDBHelper;
import com.coresoft.rfidread.DisplayActivity;
import com.coresoft.rfidread.R;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Common {
	/**
     * Converts the byte array to HEX string.
     * 
     * @param buffer
     *            the buffer.
     * @return the HEX string.
     */
	public static String toHexString(byte[] buffer) {

        String bufferString = "";

        if (buffer != null) {

            for (int i = 0; i < buffer.length; i++) {

                String hexChar = Integer.toHexString(buffer[i] & 0xFF);
                if (hexChar.length() == 1) {
                    hexChar = "0" + hexChar;
                }

                bufferString += hexChar.toUpperCase(Locale.US) + " ";
            }
        }

        return bufferString;
    }

    /**
     * Converts the integer to HEX string.
     * 
     * @param i
     *            the integer.
     * @return the HEX string.
     */
    public static String toHexString(int i) {

        String hexString = Integer.toHexString(i);

        if (hexString.length() % 2 == 1) {
            hexString = "0" + hexString;
        }

        return hexString.toUpperCase(Locale.US);
    }

    /**
     * Converts the HEX string to byte array.
     * 
     * @param hexString
     *            the HEX string.
     * @return the number of bytes.
     */
    public static int toByteArray(String hexString, byte[] byteArray) {

        char c = 0;
        boolean first = true;
        int length = 0;
        int value = 0;
        int i = 0;

        for (i = 0; i < hexString.length(); i++) {

            c = hexString.charAt(i);
            if ((c >= '0') && (c <= '9')) {
                value = c - '0';
            } else if ((c >= 'A') && (c <= 'F')) {
                value = c - 'A' + 10;
            } else if ((c >= 'a') && (c <= 'f')) {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {

                    byteArray[length] = (byte) (value << 4);

                } else {

                    byteArray[length] |= value;
                    length++;
                }

                first = !first;
            }

            if (length >= byteArray.length) {
                break;
            }
        }

        return length;
    }

    /**
     * Converts the HEX string to byte array.
     * 
     * @param hexString
     *            the HEX string.
     * @return the byte array.
     */
    public static byte[] toByteArray(String hexString) {

        byte[] byteArray = null;
        int count = 0;
        char c = 0;
        int i = 0;

        boolean first = true;
        int length = 0;
        int value = 0;

        // Count number of hex characters
        for (i = 0; i < hexString.length(); i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a'
                    && c <= 'f') {
                count++;
            }
        }

        byteArray = new byte[(count + 1) / 2];
        for (i = 0; i < hexString.length(); i++) {

            c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else if (c >= 'A' && c <= 'F') {
                value = c - 'A' + 10;
            } else if (c >= 'a' && c <= 'f') {
                value = c - 'a' + 10;
            } else {
                value = -1;
            }

            if (value >= 0) {

                if (first) {

                    byteArray[length] = (byte) (value << 4);

                } else {

                    byteArray[length] |= value;
                    length++;
                }

                first = !first;
            }
        }

        return byteArray;
    }

    /**
     * Decrypts the data using AES.
     * 
     * @param key
     *            the key.
     * @param input
     *            the input buffer.
     * @return the output buffer.
     * @throws GeneralSecurityException
     *             if there is an error in the decryption process.
     */
    public static byte[] aesDecrypt(byte key[], byte[] input)
            throws GeneralSecurityException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[16]);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        return cipher.doFinal(input);
    }
    
    /**
     * Decrypts the data using Triple DES.
     * 
     * @param key
     *            the key.
     * @param input
     *            the input buffer.
     * @return the output buffer.
     * @throws GeneralSecurityException
     *             if there is an error in the decryption process.
     */
    public static byte[] tripleDesDecrypt(byte[] key, byte[] input)
            throws GeneralSecurityException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/CBC/NoPadding");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(new byte[8]);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipher.doFinal(input);
    }
    
    
    public static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";


        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }
    
    public static String flipHexStr(String s){
        StringBuilder  result = new StringBuilder();
        for (int i = 0; i <=s.length()-2; i=i+2) {
            result.append(new StringBuilder(s.substring(i,i+2)).reverse());
        }
        return result.reverse().toString();
    }

    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String Utf8toGBK(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String GBKtoUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("GBK"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String getPath(Context context, Uri uri) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static ArrayList<String> GetStringSegmentPart(Context mContext, RFIDSegmentPart rfid)
    {
        ArrayList<String> Segment = new ArrayList<String>();
        Segment.add(rfid.cRFID);
        Segment.add(mContext.getResources().getString(R.string.nfc_read_shengchengdangwei_str));
        Segment.add(mContext.getResources().getString(R.string.nfc_read_gongchengmingcheng_str));
        Segment.add(rfid.cStyteGP);
        Segment.add(rfid.cShengchanID);
        Segment.add(rfid.cFenkuaihao);
        Segment.add(rfid.cZBScode);
        Segment.add(rfid.cJHcode);
//        Segment.add(rfid.dShijiDate.toGMTString());
        Segment.add(rfid.iBanc);
        Segment.add("");
        Segment.add(rfid.cGMnode);
        Segment.add("");
        Segment.add(rfid.cJBcode);
        Segment.add(rfid.cZYname);
        Segment.add(rfid.dRYdate.toString());
        Segment.add(rfid.dCYdate.toString());
        Segment.add("");
        Segment.add("");
        Segment.add(rfid.cChicode);
        Segment.add(rfid.dRCdate.toString());
        Segment.add(rfid.dCCdate.toString());
        Segment.add("");
        Segment.add("");
        Segment.add("");
        Segment.add("");
        Segment.add("");
        return Segment;
    }

    public static ArrayList<String> GetStringSegment(RFIDSegment rfid)
    {
        ArrayList<String> Segment = new ArrayList<String>();

        Segment.add(rfid.cShengchanID);
        Segment.add(rfid.cRKcode);
        Segment.add(rfid.cShengchanID);
        Segment.add(rfid.cStyteGP);
        Segment.add(rfid.cGMnode);
        Segment.add(rfid.dJihuaDate.toString());
        Segment.add(rfid.dShijiDate.toString());
        Segment.add(String.valueOf(rfid.iBanc));
        Segment.add(rfid.cZYname);
        Segment.add(rfid.dRYdate.toString());
        Segment.add(rfid.dCYdate.toString());
        return Segment;
    }

    public static ArrayList<String> GetStringReport(RFIDReport rfid){
        ArrayList<String> Segment = new ArrayList<String>();
        Segment.add(String.valueOf(rfid.SP_pid));
        Segment.add(rfid.c_UUID);
        Segment.add(rfid.ReportID);
        Segment.add(rfid.creportname);
        Segment.add(rfid.ReportDate.toString());
        Segment.add(rfid.creportstyte);
        Segment.add(rfid.cinstitution);
        Segment.add(rfid.cBGresult);
        Segment.add(rfid.cBGjieguo);
        Segment.add(rfid.Ckaishihuan);
        Segment.add(rfid.Cjiezhihuan);
        return Segment;
    }

    public static ArrayList<String> GetStringReportFile(RFIDReportFile rfid){
        ArrayList<String> Segment = new ArrayList<String>();
        Segment.add(String.valueOf(rfid.Img_pid));
        Segment.add(String.valueOf(rfid.SP_pid));
        Segment.add(rfid.ReportID);
        Segment.add(rfid.ReportDate.toString());
        Segment.add(rfid.cjianyanjigou);
        Segment.add(rfid.cfilehouzui);
        Segment.add(rfid.c_Report_UUID);
        Segment.add(rfid.c_UUID);
        return Segment;
    }

    public static void StartLogout(Context context,IntentDef.OnLogUserReportListener Listener){

        new AlertDialog.Builder(context)
        .setTitle(context.getString(R.string.toast_hit_logout))
        .setMessage(context.getString(R.string.toast_hit_logout_info))
        .setPositiveButton(context.getString(R.string.hit_quit),  null)
        .setNegativeButton(R.string.hit_ok , new DialogInterfaceLogUser(Listener) {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SysDBHelper.SetLoginInState(false);
                if(mOnLogUserReportListener != null){
                    mOnLogUserReportListener.OnLogUserReport(false);
                }
            }
        })
        .show();
    }

    static class DialogInterfaceLogUser implements DialogInterface.OnClickListener{

        public IntentDef.OnLogUserReportListener mOnLogUserReportListener = null;

        public DialogInterfaceLogUser(IntentDef.OnLogUserReportListener Listener){
            mOnLogUserReportListener = Listener;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
