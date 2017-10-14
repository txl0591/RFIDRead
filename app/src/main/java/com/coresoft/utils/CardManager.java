package com.coresoft.utils;

import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;

public class CardManager {
	public static String[][] TECHLISTS;
	public static IntentFilter[] FILTERS;

	static
	{
		try
		{
			TECHLISTS = new String[][]
			{
			{ IsoDep.class.getName() },
			{ NfcV.class.getName() },
			{ NfcF.class.getName() }, 
			{MifareClassic.class.getName()},
			{Ndef.class.getName()},
			{NdefFormatable.class.getName()}};

			FILTERS = new IntentFilter[]
			{ new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED, "*/*"),
					new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED, "*/*"),
					new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED, "*/*")};
		} catch (Exception e)
		{
		}
	}
}
