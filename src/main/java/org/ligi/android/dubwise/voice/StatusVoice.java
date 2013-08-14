package org.ligi.android.dubwise.voice;
/**************************************************************************
 *                                                                           

 * Author:  Marcus -LiGi- Bueschleb   
 *
 * Project URL:
 *  http://mikrokopter.de/ucwiki/en/DUBwise
 * 
 * License:
 *  http://creativecommons.org/licenses/by-nc-sa/2.0/de/ 
 *  (Creative Commons / Non Commercial / Share Alike)
 *  Additionally to the Creative Commons terms it is not allowed
 *  to use this project in _any_ violent manner! 
 *  This explicitly includes that lethal Weapon owning "People" and 
 *  Organizations (e.g. Army & Police) 
 *  are not allowed to use this Project!
 *
 **************************************************************************/

/**
 * 
 * Class to use TTS for presenting the user some status information
 *      
 * @author ligi
 *
 */
public class StatusVoice { /* implements OnInitListener, Runnable,
		OnUtteranceCompletedListener {

	private TextToSpeech mTts;
	private boolean init_done = false;
	private HashMap<String, String> voice_params;

	private MKCommunicator mk;// = MKProvider.getMK();
	private int last_nc_flags=-1;
	private int last_fc_flags=-1;

	private boolean told_range_limit=false;
	private boolean told_mc_mode=false;
	
	private boolean told_tr_mode=false;

	private boolean told_ch_mode=false;
	private boolean told_free_mode=false;
	private boolean told_ph_mode=false;
	
	private boolean told_rclost=false;
	
	private final static int sleep = 10;
	private int pause_timeout=50000;

	private String last_spoken;
	private int play_pos = 0;

	private String last_version_str = "";

	private boolean running;
	
	
	private int last_spoken_voltage=-1;
	private int last_spoken_current=-1;
	private int last_spoken_watts=-1;	
	private int last_spoken_used_capacity=-1;
	private int last_spoken_alt=-1;
	private int last_spoken_distance2home=-1;
	private int last_spoken_distance2target=-1;
	private int last_spoken_max_alt=-1;
	private int last_spoken_flight_time=-1;
	
	private int last_spoken_wp=-1;
	
	private boolean last_told_engine_state=false;
	private boolean last_calibrating_state=false;
	private boolean last_flying_state=false;
	
	private int battery_low_voltage=100;
	
	private StatusVoicePrefs prefs;
	
	public StatusVoice(MKCommunicator mk,Application app,StatusVoicePrefs prefs) {
		this.mk=mk;
		this.prefs=prefs;
		mTts = new TextToSpeech(app, this);
		running=true;	
	}
	
	@Override
	public void onInit(int arg0) {
		
		mTts.setLanguage(Locale.US);

		voice_params = new HashMap<String, String>();
		//MKParamsGeneratedDefinitionsToStringsvoice_params.put("VOICE", "MALE");

		mTts.setSpeechRate(2f);
		//voice_params.put(TextToSpeech.Engine.KE,String.valueOf(AudioManager.STREAM_NOTIFICATION ));//	String.valueOf(prefs.getStreamEnum())/);
	
		voice_params.put(TextToSpeech.Engine.KEY_PARAM_STREAM,String.valueOf(AudioManager.STREAM_NOTIFICATION )//	String.valueOf(prefs.getStreamEnum())/);
		mTts.setOnUtteranceCompletedListener(this);
		init_done = true;
		new Thread(this).start();
		this.onUtteranceCompleted("start");
	}

	public void stop() {
		running=false;
	}
	
	private String getVersionAsString() {
		return "" + mk.version.major + "."
				+ mk.version.minor + "'"
				+ (char) ('a' + mk.version.patch) +"'"; // the '' s are that the ivona voice doesn't try to sepeak a unit make a unit  
	}
	
	@Override
	public void run() {
		while (running) {

			// Log.i("DUBwise","mk"+mk.ready() + "  init"+ init_done);
			
			if (prefs.isVoiceEnabled()&&mk.ready() && init_done) {
				
			if (!mTts.isSpeaking()) { // no breaking news
				String what2speak="";
				//Log.i("DUBwise","not speaking");
				// breaking news section
				if ((!last_version_str.equals(getVersionAsString()))
						&& (prefs.isConnectionInfoEnabled())) {
		
							last_version_str = getVersionAsString();
							if (mk.is_mk())
								what2speak+="Connected to Flight Control Version "
										+ last_version_str;
							else if (mk.is_navi())
								what2speak+="Connected to Navigation Control Version "
										+ last_version_str;
							else if (mk.is_mk3mag())
								what2speak+="Connected to Mikrokopter Compas Version "
										+ last_version_str;
							else if (mk.is_fake())
								what2speak+=
										"Connected to a Simulated connection Version "
												+ last_version_str;
							else
								what2speak+="Connected to the unknown "
										+ last_version_str;
					}
				
				else if (mk.is_navi()&&mk.gps_position.NCFlags!=last_nc_flags)	{
					last_nc_flags=mk.gps_position.NCFlags;

					if (mk.gps_position.isFreeModeEnabled()) {
						if (!told_free_mode)
							what2speak+=" Free Mode Enabled. ";
						told_free_mode=true;
					}
					else
						told_free_mode=false;
					
					if (mk.gps_position.isPositionHoldEnabled()) {
						if (!told_ph_mode)
							what2speak+=" Position Hold Enabled. ";
						told_ph_mode=true;
					}
					else
						told_ph_mode=false;
					
					if (mk.gps_position.isComingHomeEnabled()) {
						if (!told_ch_mode)
							what2speak+=" Coming Home Enabled ";
						told_ch_mode=true;
					}
					else
						told_ch_mode=false;

					if (mk.gps_position.isTargetReached())	{
						if (!told_tr_mode)
							what2speak+=" Target Reached. ";
						told_tr_mode=true;
					}
					else
						told_tr_mode=false;

					
					if (mk.gps_position.isManualControlEnabled()) {
						if (!told_mc_mode)
							what2speak+=" Manual Control Enabled. ";
						told_mc_mode=true;
					}
					else
						told_mc_mode=false;

					if (mk.gps_position.isRangeLimitReached()) 	{
						if (!told_range_limit)
							what2speak+=" Range Limit Reached. ";
						told_range_limit=true;
					}
					else
						told_range_limit=false;


				}
				else if (what2speak.equals("")&&mk.is_navi()&&mk.gps_position.FCFlags!=last_fc_flags)
				{
					
					last_fc_flags=mk.gps_position.FCFlags;

					if (mk.gps_position.areEnginesOn() && !last_told_engine_state) {
						last_told_engine_state=true;
						
						what2speak+=" Engines are on ";
					}
					else if (!mk.gps_position.areEnginesOn() && last_told_engine_state) { 
						what2speak+=" Engines are off ";
						last_told_engine_state=false;
						
					}
					
					if (mk.gps_position.isFlying() &&  !last_flying_state) {
						last_flying_state=true;
						what2speak+=" UFO is flying ";
 					} else {
 						last_flying_state=mk.gps_position.isFlying();
 					}
						
					

					if (mk.gps_position.isLowBat() && VesselData.battery.getVoltage()<battery_low_voltage) {
							what2speak+=" Warning Voltage low at " + VesselData.battery.getVoltage() + "Volts";
							battery_low_voltage=VesselData.battery.getVoltage();
					}
						
					if (mk.gps_position.isCalibrating() && !last_calibrating_state) {
						last_calibrating_state=true;
						what2speak+=" UFO is Calibrating ";
					} else {
						last_calibrating_state=false;
					}
					
				
				}
				else if (mk.is_navi()&&mk.gps_position.WayPointIndex>0&&last_spoken_wp!=mk.gps_position.WayPointIndex)	{
					what2speak+=" Flying to waypoint " + mk.gps_position.WayPointIndex;
					last_spoken_wp=mk.gps_position.WayPointIndex;
				}
								
				if ((prefs.isVoiceRCLostEnabled())&&what2speak.equals("")) {
					if (mk.SenderOkay()<190) {
						if (!told_rclost) what2speak+=" RC Signal lost";
						told_rclost=true;
						}
					else {
						told_rclost=false;
					}
				}
				
				if (((pause_timeout--)<0)&&what2speak.equals("")) {
					
					switch ((play_pos++) % 14) {
						case 0:

							if (mk.is_navi() && mk.hasNaviError()
									&& prefs.isVoiceNaviErrorEnabled()) {
								what2speak+=
										" Navigation Control has the following error "
												+ mk.getNaviErrorString();
					
							}
					
							break;
						case 1:
	
							if ((VesselData.battery.getVoltage() != -1)
								 && (VesselData.battery.getVoltage() != last_spoken_voltage)
								 && prefs.isVoiceVoltsEnabled()) {
								
								last_spoken_voltage=VesselData.battery.getVoltage();
								what2speak+=" Battery at " + VesselData.battery.getVoltage() / 10.0	+ " Volts.";
					
							}
							break;
	
						case 2:
							if ((VesselData.battery.getCurrent() != -1)
								&& VesselData.battery.getCurrent() != last_spoken_current
								&& prefs.isVoiceCurrentEnabled()) {
								last_spoken_current=VesselData.battery.getCurrent();
								what2speak+=" Consuming " + VesselData.battery.getCurrent() / 10.0
										+ " Ampire";
								
							}
							break;
	
						case 3:
							int watts=(VesselData.battery.getCurrent() * VesselData.battery.getVoltage());
							if ((VesselData.battery.getCurrent() != -1)
								&& watts!=last_spoken_watts	
								&& prefs.isVoiceCurrentEnabled()) {
								last_spoken_watts=watts;
								what2speak+=" Consuming "
										+ (VesselData.battery.getCurrent() * VesselData.battery.getVoltage()) / 100
										+ " Wats";
							}
	
							break;
	
						case 4:
	
							if (   (VesselData.battery.getUsedCapacity() != -1)
									&& last_spoken_used_capacity!=VesselData.battery.getUsedCapacity()
									&& mk.isFlying() // consumes in while parking and that voice out is annoying									
									&& prefs.isVoiceUsedCapacityEnabled()) {
										last_spoken_used_capacity=VesselData.battery.getUsedCapacity();
										what2speak+=" Consumed " + VesselData.battery.getUsedCapacity()
										+ " milli Ampire hours";
	
							}
							break;
	
						case 5:
	
							if ((mk.getAlt() != -1)
								&& last_spoken_alt!=mk.getAlt()	
								&& (prefs.isVoiceAltEnabled())) {
								last_spoken_alt=mk.getAlt();
								what2speak+=" Current height is " + mk.getAlt() / 10
										+ " meters.";
	
							}
							
							break;
							
						case 6:	
							if ((mk.gps_position.Distance2Home >0)
								&& (last_spoken_distance2home!=mk.gps_position.Distance2Home)
									&& (prefs.isDistance2HomeEnabled())) {
								last_spoken_distance2home=mk.gps_position.Distance2Home;
								what2speak+=" Distance to Home " + mk.gps_position.Distance2Home/10
										+ " meters.";
	
							}
							break;
	
						case 7:	
							if ((mk.gps_position.Distance2Target >0)
								&& (last_spoken_distance2target!=mk.gps_position.Distance2Target)
									&& (prefs.isDistance2TargetEnabled())) {
								last_spoken_distance2target=mk.gps_position.Distance2Target;
								what2speak+=" Distance to Target " + mk.gps_position.Distance2Target/10
										+ " meters.";
	
							}
							break;
							
						case 8:
							if ((mk.getFlyingTime()>0)
								&& (mk.getFlyingTime()!=last_spoken_flight_time)
								&& (prefs.isFlightTimeEnabled())) {
										last_spoken_flight_time=mk.getFlyingTime();
										what2speak+=" Flight time";
										if ((mk.getFlyingTime()/60)!=0)
											what2speak+=" " + mk.getFlyingTime()/60 + " minutes ";
										
										
										if ((mk.getFlyingTime()%60)!=0)
											what2speak+=" " + mk.getFlyingTime()%60 + " seconds. ";
										
										
							}
							break;
					
							
						case 9:
							if ((mk.is_navi() || mk.is_fake())
									&& (prefs.isVoiceSatelitesEnabled())) {
								what2speak+= " " + mk.SatsInUse()
										+ " Satelites are used for GPS.";
	
							}
	
							break;
	
						case 10:
							if ((mk.is_navi() || mk.is_fake())
									&& (prefs.isSpeedEnabled())) {
								what2speak+= " Current speed " + formatSpeedValue(mk.gps_position.GroundSpeed)
										+ " kilometer per hour.";
	
							}
	
							break;
	
						case 11:
							if ((mk.is_navi() || mk.is_fake())
									&& (prefs.isMaxSpeedEnabled())) {
								what2speak+= " Max speed " + formatSpeedValue(mk.stats.max_speed)
										+ " kilometer per hour. ";
	
							}
							break;
	
						case 12:
	
							if ((mk.stats.max_alt != -1)
								&& (last_spoken_max_alt!=mk.stats.max_alt)
								&& (prefs.isVoiceMaxAltEnabled())) {
								
								last_spoken_max_alt=mk.stats.max_alt;
								what2speak+=" Max height was " + mk.stats.max_alt / 10
										+ " meters.";
	
							}
							
							break;
	
						case 13:
							pause_timeout=prefs.getPauseTimeInMS()/sleep;
							break;
					} // switch
				}

				if (!what2speak.equals("")) {
					last_spoken=what2speak;
					Log.i("DUBwiseStatusVoice","" +
							" " + what2speak );
					mTts.speak(what2speak, TextToSpeech.QUEUE_ADD, voice_params);
				 }
				}
			}
			try {
				Thread.sleep(sleep);
				// Log.i("DUBwise" ,""+ mTts.isSpeaking());
			} catch (InterruptedException e) {
			}
		}
	}


	public String getLastSpoken() {
		return last_spoken;
	}
	public int getPauseTimeout() {
		return pause_timeout;
	}
	
	public void setPauseTimeout(int new_timeout) {
		pause_timeout=new_timeout;
	}
	
	public String formatSpeedValue(int speed) {
		return "" + ((int)(speed*0.36)/10) + "." +((int)(speed*0.36)%10);
	}
	
	@Override
	public void onUtteranceCompleted(String arg0) {
		Log.i("DUBwiseStatusVoice", "onuterancecomplete");
	}
                             */

}
