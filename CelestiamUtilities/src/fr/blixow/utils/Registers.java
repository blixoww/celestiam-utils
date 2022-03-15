package fr.blixow.utils;

import fr.blixow.auto.AutoMessage;
import fr.blixow.repair.RepairManager;
import fr.blixow.reward.RewardManager;
import fr.blixow.rtp.RtpManager;

public class Registers {

	public static RtpManager rtpManager;
	public static RepairManager repairManager;
	public static RewardManager rewardManager;
	public static AutoMessage autoMessage;
	public static Registers registers;

	public static Registers getRegisters() {
		return registers;
	}
	
	public static RtpManager getRtpManager() {
		return rtpManager;
	}

	public static RepairManager getRepairManager() {
		return repairManager;
	}

	public static RewardManager getRewardManager() {
		return rewardManager;
	}
	
	public static AutoMessage getAutoMessageManager() {
		return autoMessage;
	}
	
}
