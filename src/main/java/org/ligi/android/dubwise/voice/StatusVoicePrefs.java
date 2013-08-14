package org.ligi.android.dubwise.voice;

import android.media.AudioManager;


public class StatusVoicePrefs {
	public int getStreamEnum() {
		return new int[] { AudioManager.STREAM_ALARM,AudioManager.STREAM_NOTIFICATION,AudioManager.STREAM_MUSIC,AudioManager.STREAM_SYSTEM,AudioManager.STREAM_VOICE_CALL }[0];
	}

	public boolean isVoiceEnabled() {
		return true;
	}

	public boolean isConnectionInfoEnabled() {
		return true;
	}

	public boolean isVoiceRCLostEnabled() {
		return true;
	}

	public boolean isVoiceNaviErrorEnabled() {
		return true;
	}

	public boolean isVoiceVoltsEnabled() {
		return true;
	}

	public boolean isVoiceCurrentEnabled() {
		return true;
	}

	public boolean isVoiceUsedCapacityEnabled() {
		return true;
	}

	public int getPauseTimeInMS() {
		return 5000000;
	}

	public boolean isVoiceMaxAltEnabled() {
		return false;
	}

	public boolean isMaxSpeedEnabled() {
		return false;
	}

	public boolean isSpeedEnabled() {
		return true;
	}

	public boolean isVoiceSatelitesEnabled() {
		return true;
	}

	public boolean isFlightTimeEnabled() {
		return true;
	}

	public boolean isDistance2TargetEnabled() {
		return false;
	}

	public boolean isDistance2HomeEnabled() {
		return false;
	}

	public boolean isVoiceAltEnabled() {
		return true;
	}
	
	
}
