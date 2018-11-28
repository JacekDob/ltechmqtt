package com.ex.ltech.led.connetion;

import android.graphics.Color;
import com.company.NetSDK.CtrlType;
import com.ex.ltech.hongwai.RcConstant;
import com.ex.ltech.hongwai.StringUtil;
import com.ex.ltech.hongwai.vo.InnerRcVo;
import com.ex.ltech.hongwai.vo.MyRcDevice;
import com.ex.ltech.hongwai.vo.NonIrDevice;
import com.ex.ltech.hongwai.vo.SceneVo;
import com.ex.ltech.hongwai.vo.TkPanelChannelVo;
import com.ex.ltech.hongwai.vo.WirelessSensorVo;
import com.ex.ltech.led.MyApp;
import com.ex.ltech.led.acti.Main;
import com.ex.ltech.led.utils.StringUtils;
import com.ex.ltech.led.vo.CmdVos;
import com.ex.ltech.led.vo.RepeatDayVo;
import com.ex.ltech.onepiontfive.main.MyBusiness;
import com.ex.ltech.onepiontfive.main.vo.GeoSetting;
import com.zhuhai.ltech.R;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class CmdDateBussiness {
    public static final int OLD_K1K2_MAX_COUNT = 16;
    public static final int OLD_K1K2_MAX_NUM = 15;
    public static int cmdCount;
    private static CmdDateBussiness instance;
    private static String userIdHexString;
    private int frontModeNum;
    private byte pwd12 = (byte) 0;
    private byte pwd34 = (byte) 0;
    private int realModeNum;
    String temptest = "30000B16E9010100003C";

    public static CmdDateBussiness instance() {
        if (instance == null) {
            synchronized (CmdDateBussiness.class) {
                if (instance == null) {
                    instance = new CmdDateBussiness();
                    userIdHexString = Integer.toHexString(MyApp.getApp().getAppid()).toUpperCase();
                    for (int i = userIdHexString.length(); i < 8; i++) {
                        userIdHexString = "0" + userIdHexString;
                    }
                }
            }
        }
        return instance;
    }

    private CmdDateBussiness() {
    }

    public void pwdToHex(String pwd) {
        String ii = pwd.substring(0, 2);
        String kk = pwd.substring(2);
        this.pwd12 = (byte) strToSixteen(ii);
        this.pwd34 = (byte) strToSixteen(kk);
    }

    public int strToSixteen(String str) {
        return Integer.valueOf(str, 16).intValue();
    }

    public int tenToSixteen(int ten) {
        return Integer.valueOf(Integer.toHexString(ten), 16).intValue();
    }

    public byte[] getColorTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int timeModeType, int brightness, int r, int g, int b, int gray) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 12, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) timgSwich, (byte) timeModeType, (byte) 0, (byte) tenToSixteen(brightness), (byte) tenToSixteen(r), (byte) tenToSixteen(g), (byte) tenToSixteen(b), (byte) tenToSixteen(gray), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getColorTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int timeModeType, int brightness, int r, int g, int b, int gray, byte[] id) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 16, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) timgSwich, (byte) timeModeType, (byte) 0, (byte) tenToSixteen(brightness), (byte) tenToSixteen(r), (byte) tenToSixteen(g), (byte) tenToSixteen(b), (byte) tenToSixteen(gray), id[0], id[1], id[2], id[3], (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getTimingOffCmd(String seletedDaysNumLists, int timgingOder, String hour, String min) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 4, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getCtSceneTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int sceneIndex, int jianBianmin, byte[] id) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 15, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) timgSwich, (byte) 18, (byte) sceneIndex, (byte) 0, (byte) 0, (byte) -31, (byte) tenToSixteen(jianBianmin), id[0], id[1], id[2], id[3], (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getCtColorTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int jianBianmin, int brightness, int c, int w, byte[] id) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 15, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) timgSwich, (byte) 17, (byte) tenToSixteen(brightness), (byte) tenToSixteen(c), (byte) tenToSixteen(w), (byte) -31, (byte) tenToSixteen(jianBianmin), id[0], id[1], id[2], id[3], (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getCtTimingOffCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int speed, byte[] id) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 15, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) 2, (byte) 18, (byte) 1, (byte) 0, (byte) 0, (byte) tenToSixteen(speed), (byte) tenToSixteen(0), (byte) tenToSixteen(0), id[0], id[1], id[2], id[3]};
        cmd[23] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getCtOffTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int jianBianmin, int brightness, int c, int w, byte[] id) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 15, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) timgSwich, (byte) 25, (byte) tenToSixteen(brightness), (byte) tenToSixteen(c), (byte) tenToSixteen(w), (byte) -31, (byte) tenToSixteen(jianBianmin), id[0], id[1], id[2], id[3], (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getYaoKongTiming(int xuHao, byte[] ykData, String seletedDaysNumLists, String hour, String min, int timgSwitch, byte[] id, boolean yaokongSwich) {
        byte[] cmd = new byte[(ykData.length + 19)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 20;
        cmd[7] = (byte) (ykData.length + 10);
        cmd[8] = (byte) xuHao;
        cmd[9] = (byte) toHex(seletedDaysNumLists);
        cmd[10] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[11] = (byte) tenToSixteen(Integer.parseInt(min));
        cmd[12] = (byte) timgSwitch;
        cmd[13] = id[0];
        cmd[14] = id[1];
        cmd[15] = id[2];
        cmd[16] = id[3];
        cmd[13] = (byte) (yaokongSwich ? 1 : 0);
        cmd[14] = id[0];
        cmd[15] = id[1];
        cmd[16] = id[2];
        cmd[17] = id[3];
        for (int i = 0; i < ykData.length; i++) {
            cmd[i + 18] = ykData[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        System.out.println("testYaokong     send" + StringUtils.btye2Str(id));
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getRcTiming(int xuHao, byte[] ykData, byte[] channelRcGeBit, String seletedDaysNumLists, String hour, String min, int timgSwitch, byte[] id, boolean yaokongSwich) {
        System.out.println("红外发射器");
        byte[] cmd = new byte[((channelRcGeBit.length > 0 ? 4 : 0) + (channelRcGeBit.length + (ykData.length + 20)))];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 20;
        cmd[7] = (byte) ((channelRcGeBit.length > 0 ? 4 : 0) + (channelRcGeBit.length + (ykData.length + 11)));
        cmd[8] = (byte) xuHao;
        cmd[9] = (byte) (channelRcGeBit.length > 0 ? 3 : 2);
        cmd[10] = (byte) toHex(seletedDaysNumLists);
        cmd[11] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[12] = (byte) tenToSixteen(Integer.parseInt(min));
        cmd[13] = (byte) timgSwitch;
        cmd[14] = (byte) (yaokongSwich ? 1 : 0);
        cmd[15] = id[0];
        cmd[16] = id[1];
        cmd[17] = id[2];
        cmd[18] = id[3];
        int i;
        if (channelRcGeBit.length == 0) {
            for (i = 0; i < ykData.length; i++) {
                cmd[i + 19] = ykData[i];
            }
        } else {
            cmd[19] = (byte) ykData.length;
            for (i = 0; i < ykData.length; i++) {
                cmd[i + 20] = ykData[i];
            }
            if (channelRcGeBit.length > 0) {
                cmd[ykData.length + 20] = (byte) -6;
                cmd[ykData.length + 21] = (byte) -81;
                cmd[ykData.length + 22] = (byte) channelRcGeBit.length;
                for (i = 0; i < channelRcGeBit.length; i++) {
                    cmd[(i + 23) + channelRcGeBit.length] = channelRcGeBit[i];
                }
            }
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        System.out.println("       Timing     cmd        " + StringUtil.byte2Hexstr(cmd));
        return cmd;
    }

    public byte[] getMotorIrTiming(int xuHao, String seletedDaysNumLists, String hour, String min, int timgSwitch, byte[] motorCode, int gradientMins, boolean isEffect) {
        int i;
        System.out.println("       Motor        ");
        String dataLenthHex = Integer.toHexString(motorCode.length + 10).toUpperCase();
        for (i = dataLenthHex.length(); i < 4; i++) {
            dataLenthHex = "0" + dataLenthHex;
        }
        byte[] cmd = new byte[(motorCode.length + 20)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 80;
        cmd[7] = (byte) Integer.parseInt(dataLenthHex.substring(0, 2), 16);
        cmd[8] = (byte) Integer.parseInt(dataLenthHex.substring(2, 4), 16);
        cmd[9] = (byte) xuHao;
        cmd[10] = (byte) 2;
        cmd[11] = (byte) toHex(seletedDaysNumLists);
        cmd[12] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[13] = (byte) tenToSixteen(Integer.parseInt(min));
        cmd[14] = (byte) timgSwitch;
        cmd[15] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[16] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[17] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[18] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        for (i = 0; i < motorCode.length; i++) {
            cmd[i + 19] = motorCode[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        System.out.println("       getNonIrTiming     cmd        " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] getNonIrTiming(int xuHao, String seletedDaysNumLists, String hour, String min, int timgSwitch, NonIrDevice nonIrDevice, int gradientMins, int operateType) {
        int i;
        byte[] nonDeviceDataContent = new byte[0];
        int timingType = 1;
        switch (nonIrDevice.mType) {
            case 10:
                timingType = 1;
                nonDeviceDataContent = new byte[]{(byte) nonIrDevice.nonIrDeviceId, (byte) RcConstant.getK1K2RfSelectedLoadOnoff(nonIrDevice)};
                break;
            case 11:
                timingType = 1;
                nonDeviceDataContent = new byte[3];
                nonDeviceDataContent[0] = (byte) nonIrDevice.nonIrDeviceId;
                nonDeviceDataContent[1] = (byte) RcConstant.getK1K2RfSelectedLoadOnoff(nonIrDevice);
                break;
            case 12:
            case 133:
                timingType = 3;
                nonDeviceDataContent = new byte[11];
                nonDeviceDataContent[0] = (byte) 2;
                nonDeviceDataContent[1] = (byte) nonIrDevice.nonIrDeviceId;
                nonDeviceDataContent[2] = (byte) operateType;
                nonDeviceDataContent[3] = (byte) (nonIrDevice.onOff ? 1 : 0);
                nonDeviceDataContent[4] = (byte) nonIrDevice.irCt1Brt;
                nonDeviceDataContent[5] = (byte) nonIrDevice.irCt1W;
                nonDeviceDataContent[6] = (byte) nonIrDevice.irCt1C;
                nonDeviceDataContent[7] = (byte) 0;
                nonDeviceDataContent[8] = (byte) 0;
                if (gradientMins > 0) {
                    nonDeviceDataContent[9] = (byte) -31;
                } else {
                    nonDeviceDataContent[9] = (byte) -32;
                }
                nonDeviceDataContent[10] = (byte) gradientMins;
                break;
            case 23:
            case 134:
                timingType = 3;
                nonDeviceDataContent = new byte[11];
                nonDeviceDataContent[0] = (byte) 1;
                nonDeviceDataContent[1] = (byte) nonIrDevice.nonIrDeviceId;
                nonDeviceDataContent[2] = (byte) operateType;
                nonDeviceDataContent[3] = (byte) (nonIrDevice.onOff ? 1 : 0);
                nonDeviceDataContent[4] = (byte) -1;
                nonDeviceDataContent[5] = (byte) nonIrDevice.irCt1Brt;
                nonDeviceDataContent[6] = (byte) nonIrDevice.irCt1C;
                nonDeviceDataContent[7] = (byte) 0;
                nonDeviceDataContent[8] = (byte) 0;
                if (gradientMins > 0) {
                    nonDeviceDataContent[9] = (byte) -31;
                } else {
                    nonDeviceDataContent[9] = (byte) -32;
                }
                nonDeviceDataContent[10] = (byte) gradientMins;
                break;
            case 24:
                timingType = 6;
                nonDeviceDataContent = new byte[6];
                nonDeviceDataContent[0] = (byte) 1;
                nonDeviceDataContent[1] = (byte) nonIrDevice.nonIrDeviceId;
                nonDeviceDataContent[2] = (byte) 2;
                StringBuilder sb = new StringBuilder("0000");
                for (i = 3; i > -1; i--) {
                    if (((TkPanelChannelVo) nonIrDevice.tkPanelChannelVos.get(i)).isOnOff()) {
                        sb.append("1");
                    } else {
                        sb.append("0");
                    }
                }
                nonDeviceDataContent[3] = (byte) Integer.parseInt(sb.toString(), 2);
                sb = new StringBuilder("0000");
                for (i = 3; i > -1; i--) {
                    if (((TkPanelChannelVo) nonIrDevice.tkPanelChannelVos.get(i)).isSelected()) {
                        sb.append("1");
                    } else {
                        sb.append("0");
                    }
                }
                nonDeviceDataContent[4] = (byte) Integer.parseInt(sb.toString(), 2);
                nonDeviceDataContent[5] = (byte) 0;
                break;
            case 26:
            case 132:
                timingType = 3;
                nonDeviceDataContent = new byte[11];
                nonDeviceDataContent[0] = (byte) 4;
                nonDeviceDataContent[1] = (byte) nonIrDevice.nonIrDeviceId;
                nonDeviceDataContent[2] = (byte) operateType;
                if (nonIrDevice.irCt1ModeNum != -1) {
                    nonDeviceDataContent[3] = (byte) nonIrDevice.irCt1ModeNum;
                } else {
                    nonDeviceDataContent[3] = (byte) (nonIrDevice.onOff ? 1 : 0);
                }
                nonDeviceDataContent[4] = (byte) nonIrDevice.irCt1Brt;
                if (nonIrDevice.irCt1ModeNum != -1) {
                    nonDeviceDataContent[5] = (byte) 0;
                    nonDeviceDataContent[6] = (byte) 0;
                    nonDeviceDataContent[7] = (byte) 0;
                } else {
                    nonDeviceDataContent[5] = (byte) nonIrDevice.irCt1R;
                    nonDeviceDataContent[6] = (byte) nonIrDevice.irCt1G;
                    nonDeviceDataContent[7] = (byte) nonIrDevice.irCt1B;
                }
                nonDeviceDataContent[8] = (byte) 0;
                if (gradientMins > 0) {
                    nonDeviceDataContent[9] = (byte) -31;
                } else {
                    nonDeviceDataContent[9] = (byte) -32;
                }
                nonDeviceDataContent[10] = (byte) gradientMins;
                break;
        }
        String dataLenthHex = Integer.toHexString(nonDeviceDataContent.length + 10).toUpperCase();
        for (i = dataLenthHex.length(); i < 4; i++) {
            dataLenthHex = "0" + dataLenthHex;
        }
        byte[] cmd = new byte[(nonDeviceDataContent.length + 20)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 80;
        cmd[7] = (byte) Integer.parseInt(dataLenthHex.substring(0, 2), 16);
        cmd[8] = (byte) Integer.parseInt(dataLenthHex.substring(2, 4), 16);
        cmd[9] = (byte) xuHao;
        cmd[10] = (byte) timingType;
        cmd[11] = (byte) toHex(seletedDaysNumLists);
        cmd[12] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[13] = (byte) tenToSixteen(Integer.parseInt(min));
        cmd[14] = (byte) timgSwitch;
        cmd[15] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[16] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[17] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[18] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        for (i = 0; i < nonDeviceDataContent.length; i++) {
            cmd[i + 19] = nonDeviceDataContent[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        System.out.println("       getNonIrTiming     cmd        " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] getMBrankTiming(byte[] nonDeviceDataContent, int xuHao, String seletedDaysNumLists, String hour, String min, int timgSwitch, int dType) {
        int i;
        System.out.println("m1 ........m8.....m16s      " + StringUtils.btye2Str3(nonDeviceDataContent));
        String dataLenthHex = Integer.toHexString(nonDeviceDataContent.length + 10).toUpperCase();
        for (i = dataLenthHex.length(); i < 4; i++) {
            dataLenthHex = "0" + dataLenthHex;
        }
        byte[] cmd = new byte[(nonDeviceDataContent.length + 20)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 80;
        cmd[7] = (byte) Integer.parseInt(dataLenthHex.substring(0, 2), 16);
        cmd[8] = (byte) Integer.parseInt(dataLenthHex.substring(2, 4), 16);
        cmd[9] = (byte) xuHao;
        cmd[10] = (byte) (dType == 22 ? 5 : 4);
        cmd[11] = (byte) toHex(seletedDaysNumLists);
        cmd[12] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[13] = (byte) tenToSixteen(Integer.parseInt(min));
        cmd[14] = (byte) timgSwitch;
        cmd[15] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[16] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[17] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[18] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        for (i = 0; i < nonDeviceDataContent.length; i++) {
            cmd[i + 19] = nonDeviceDataContent[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        System.out.println("       getNonIrTiming     cmd        " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] getRcTimingTwice(int xuHao, byte[] ykData, byte[] channelRcGeBit, String seletedDaysNumLists, String hour, String min, int timgSwitch, byte[] id, boolean yaokongSwich) {
        int i;
        byte[] cmd = new byte[((channelRcGeBit.length > 0 ? 3 : 0) + (channelRcGeBit.length + (ykData.length + 20)))];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 20;
        cmd[7] = (byte) ((channelRcGeBit.length > 0 ? 3 : 0) + (ykData.length + 11));
        cmd[8] = (byte) xuHao;
        cmd[9] = (byte) (channelRcGeBit.length > 0 ? 3 : 2);
        cmd[10] = (byte) toHex(seletedDaysNumLists);
        cmd[11] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[12] = (byte) tenToSixteen(Integer.parseInt(min));
        cmd[13] = (byte) timgSwitch;
        cmd[14] = id[0];
        cmd[15] = id[1];
        cmd[16] = id[2];
        cmd[17] = id[3];
        cmd[14] = (byte) (yaokongSwich ? 1 : 0);
        cmd[15] = id[0];
        cmd[16] = id[1];
        cmd[17] = id[2];
        cmd[18] = id[3];
        for (i = 0; i < ykData.length; i++) {
            cmd[i + 19] = ykData[i];
        }
        if (channelRcGeBit.length > 0) {
            cmd[ykData.length + 19] = (byte) -6;
            cmd[ykData.length + 20] = (byte) -81;
            cmd[ykData.length + 21] = (byte) channelRcGeBit.length;
            for (i = 0; i < channelRcGeBit.length; i++) {
                cmd[(i + 22) + channelRcGeBit.length] = channelRcGeBit[i];
            }
        }
        cmd[cmd.length - 1] = (byte) -21;
        System.out.println("       Timing     cmd        " + xuHao);
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getModeTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, String modeSeltedList, int speed) {
        cmd = new byte[21];
        handModeNumSeleted(modeSeltedList);
        cmd[15] = (byte) this.realModeNum;
        cmd[16] = (byte) this.frontModeNum;
        cmd[17] = (byte) tenToSixteen(speed);
        cmd[18] = (byte) tenToSixteen(0);
        cmd[19] = (byte) tenToSixteen(0);
        cmd[20] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getModeTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, String modeSeltedList, int speed, byte[] id) {
        cmd = new byte[25];
        handModeNumSeleted(modeSeltedList);
        cmd[15] = (byte) this.realModeNum;
        cmd[16] = (byte) this.frontModeNum;
        cmd[17] = (byte) tenToSixteen(speed);
        cmd[18] = (byte) tenToSixteen(0);
        cmd[19] = (byte) tenToSixteen(0);
        cmd[20] = id[0];
        cmd[21] = id[1];
        cmd[22] = id[2];
        cmd[23] = id[3];
        cmd[24] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getOffTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int speed) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 12, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) 2, (byte) 18, (byte) 1, (byte) 0, (byte) 0, (byte) tenToSixteen(speed), (byte) tenToSixteen(0), (byte) tenToSixteen(0), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getOffTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int speed, byte[] id) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 16, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) 2, (byte) 18, (byte) 1, (byte) 0, (byte) 0, (byte) tenToSixteen(speed), (byte) tenToSixteen(0), (byte) tenToSixteen(0), id[0], id[1], id[2], id[3], (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getPlugsTimingCmd(String seletedDaysNumLists, int timgingOder, String starthour, String startmin, String endhour, String endmin, boolean timgSwich, boolean isPlug, byte[] id) {
        byte[] cmd = new byte[24];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 40;
        cmd[7] = (byte) 15;
        cmd[8] = (byte) timgingOder;
        cmd[9] = (byte) (isPlug ? 36 : 37);
        cmd[10] = (byte) (starthour.length() > 0 ? 209 : 208);
        cmd[11] = (byte) toHex(seletedDaysNumLists);
        cmd[12] = starthour.length() > 0 ? (byte) tenToSixteen(Integer.parseInt(starthour)) : (byte) 0;
        cmd[13] = startmin.length() > 0 ? (byte) tenToSixteen(Integer.parseInt(startmin)) : (byte) 0;
        cmd[14] = (byte) (endhour.length() > 0 ? 225 : CtrlType.SDK_CTRL_RAID);
        cmd[15] = (byte) toHex(seletedDaysNumLists);
        if (endhour.length() > 0 && starthour.length() > 0) {
            StringBuilder sb;
            if (Integer.parseInt(starthour) > Integer.parseInt(endhour)) {
                sb = new StringBuilder();
                sb.append("0").append(seletedDaysNumLists.substring(2, 8)).append(seletedDaysNumLists.substring(1, 2));
                cmd[15] = (byte) toHex(sb.toString());
            }
            if (Integer.parseInt(starthour) == Integer.parseInt(endhour) && Integer.parseInt(startmin) > Integer.parseInt(endmin)) {
                sb = new StringBuilder();
                sb.append("0").append(seletedDaysNumLists.substring(2, 8)).append(seletedDaysNumLists.substring(1, 2));
                cmd[15] = (byte) toHex(sb.toString());
            }
        }
        cmd[16] = endhour.length() > 0 ? (byte) tenToSixteen(Integer.parseInt(endhour)) : (byte) 0;
        cmd[17] = endmin.length() > 0 ? (byte) tenToSixteen(Integer.parseInt(endmin)) : (byte) 0;
        if (timgSwich) {
            cmd[18] = (byte) 1;
        } else {
            cmd[18] = (byte) 0;
        }
        cmd[19] = id[0];
        cmd[20] = id[1];
        cmd[21] = id[2];
        cmd[22] = id[3];
        cmd[23] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("getPlugsTimingCmd     " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getOffDeviceCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int speed, byte[] uId) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 16, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) 0, (byte) 18, (byte) 1, (byte) 0, (byte) 0, (byte) tenToSixteen(speed), (byte) tenToSixteen(0), (byte) tenToSixteen(0), uId[0], uId[1], uId[2], uId[3], (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getOffDeviceCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, int timgSwich, int speed) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 20, (byte) 12, (byte) timgingOder, (byte) toHex(seletedDaysNumLists), (byte) tenToSixteen(Integer.parseInt(hour)), (byte) tenToSixteen(Integer.parseInt(min)), (byte) 0, (byte) 18, (byte) 1, (byte) 0, (byte) 0, (byte) tenToSixteen(speed), (byte) tenToSixteen(0), (byte) tenToSixteen(0), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getOneZeroFiveDeviceInfoCmd() {
        ArrayList<Integer> datas = new ArrayList();
        datas.add(Integer.valueOf(90));
        datas.add(Integer.valueOf(165));
        datas.add(Integer.valueOf(255));
        datas.add(Integer.valueOf(1));
        datas.add(Integer.valueOf(17));
        datas.add(Integer.valueOf(255));
        datas.add(Integer.valueOf(255));
        datas.add(Integer.valueOf(255));
        datas.add(Integer.valueOf(255));
        datas.add(Integer.valueOf(24));
        datas.add(Integer.valueOf(1));
        datas.add(Integer.valueOf(0));
        datas.add(Integer.valueOf(1));
        MyBusiness myBusiness = new MyBusiness(MyApp.getApp());
        myBusiness.addCheckSumData(datas);
        datas.add(Integer.valueOf(22));
        return myBusiness.getCmdData(datas);
    }

    public byte[] getDeviceInfoCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 33, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getDeviceOnOffInfoCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 37, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getLittleLedOnOffInfoCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 39, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getPlugsOnOffInfoCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 48, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getHeartBeatCmd() {
        return new byte[]{(byte) 102, (byte) -69, (byte) -69, (byte) -69, (byte) -69, (byte) -69, (byte) -69, (byte) -69, (byte) -69, (byte) -21};
    }

    public byte[] getSaveDeviceNameCmd(String dName) {
        byte[] nameByte = dName.getBytes();
        int count = nameByte.length + 9;
        byte[] cmd = new byte[count];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 32;
        cmd[7] = (byte) nameByte.length;
        for (int i = 0; i < nameByte.length; i++) {
            cmd[i + 8] = nameByte[i];
        }
        cmd[count - 1] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public static void demoChangeStringToHex(String inputString, byte[] cmd) {
        String s = inputString;
        for (int i = 0; i < s.length(); i++) {
            byte[] ba = s.substring(i, i + 1).getBytes();
            String tmpHex = Integer.toHexString(ba[0] & 255);
            System.out.print("r10x" + tmpHex.toUpperCase());
            cmd[i + 8] = Byte.decode("0x" + tmpHex.toUpperCase()).byteValue();
            if (ba.length == 2) {
                Integer.toHexString(ba[1] & 255);
            }
        }
        System.out.print("eeeeeeeeeeeeeeeee-------------------------demoChangeStringToHex");
    }

    public byte[] getColorCmd(int type, int brightness, int r, int g, int b, int gray) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) tenToSixteen(cmdCount), (byte) -63, this.pwd12, this.pwd34, (byte) 17, (byte) tenToSixteen(6), (byte) tenToSixteen(type), (byte) tenToSixteen(brightness), (byte) tenToSixteen(r), (byte) tenToSixteen(g), (byte) tenToSixteen(b), (byte) tenToSixteen(gray), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        Main.lastSendCmd = Main.colorCmd;
        System.out.println("getColorCmd======" + StringUtil.byte2Hexstr(cmd));
        return cmd;
    }

    public byte[] getCtColorCmd(int type, int brightness, int c, int w) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) tenToSixteen(cmdCount), (byte) -63, this.pwd12, this.pwd34, (byte) 17, (byte) 4, (byte) tenToSixteen(type), (byte) tenToSixteen(brightness), (byte) tenToSixteen(c), (byte) tenToSixteen(w), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getReqCtSceneCmd(boolean front) {
        byte b = (byte) 1;
        System.out.println("getReqCtSceneCmd======");
        byte[] cmd = new byte[10];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) tenToSixteen(cmdCount);
        cmd[3] = (byte) -63;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 34;
        cmd[7] = (byte) 1;
        if (front) {
            b = (byte) 0;
        }
        cmd[8] = b;
        cmd[9] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getLearnPanelNRcCmd() {
        System.out.println("getReqCtSceneCmd======");
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) tenToSixteen(cmdCount), (byte) -63, this.pwd12, this.pwd34, (byte) 60, (byte) 1, (byte) 1, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getCtSceneColorCmd(int index, String name, int brightness, int c, int w) {
        System.out.println("getCtSceneColorCmd======" + brightness);
        byte[] nameB = name.getBytes();
        byte[] cmd = new byte[(nameB.length + 16)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) tenToSixteen(cmdCount);
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 18;
        cmd[7] = (byte) (nameB.length + 7);
        cmd[8] = (byte) tenToSixteen(index);
        cmd[9] = (byte) -127;
        cmd[10] = (byte) nameB.length;
        for (int i = 11; i < nameB.length + 11; i++) {
            cmd[i] = nameB[i - 11];
        }
        cmd[(nameB.length + 12) - 1] = (byte) -126;
        cmd[(nameB.length + 13) - 1] = (byte) brightness;
        cmd[(nameB.length + 14) - 1] = (byte) c;
        cmd[(nameB.length + 15) - 1] = (byte) w;
        cmd[(nameB.length + 16) - 1] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("brightness======" + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] getOutletColorCmd(int type, int brightness, int r, int g, int b, int w) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) tenToSixteen(cmdCount), (byte) -63, this.pwd12, this.pwd34, (byte) 17, (byte) 6, (byte) -46, (byte) tenToSixteen(r), (byte) tenToSixteen(g), (byte) tenToSixteen(b), (byte) -86, (byte) tenToSixteen(255), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getAllOnOffCmd(int onOff) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -63, this.pwd12, this.pwd34, (byte) 21, (byte) tenToSixteen(1), (byte) tenToSixteen(onOff), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getCallDeviceUpdataStatusCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -63, this.pwd12, this.pwd34, (byte) 61, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] checkRgbwDeviceVersionCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -63, this.pwd12, this.pwd34, (byte) 62, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] sendUpdataWifiCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -63, this.pwd12, this.pwd34, (byte) 63, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getPlugOnOffCmd(int onOff) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -63, this.pwd12, this.pwd34, (byte) 36, (byte) tenToSixteen(1), (byte) tenToSixteen(onOff), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getCustomModeCmd(int modeIndex, int changeMode, int runStatus, byte speed, int brt, int colorCount, List<Integer> colors) {
        System.out.println("getCustomModeCmd       " + speed + "      brt   " + brt + "           colorCount      " + colorCount);
        int byteCount = (colors.size() * 3) + 16;
        byte[] cmd = new byte[byteCount];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 18;
        cmd[7] = (byte) 31;
        cmd[8] = (byte) 33;
        cmd[9] = (byte) modeIndex;
        cmd[10] = (byte) changeMode;
        cmd[11] = (byte) runStatus;
        cmd[12] = speed;
        cmd[13] = (byte) brt;
        cmd[14] = (byte) colorCount;
        for (int i = 0; i < colorCount; i++) {
            int color = ((Integer) colors.get(i)).intValue();
            if (color == MyApp.getApp().getResources().getColor(R.color.gray)) {
                color = -16777216;
            }
            cmd[(i * 3) + 15] = (byte) tenToSixteen(Color.red(color));
            cmd[(i * 3) + 16] = (byte) tenToSixteen(Color.green(color));
            cmd[(i * 3) + 17] = (byte) tenToSixteen(Color.blue(color));
        }
        cmd[byteCount - 1] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getMoreSeletedModeCmd(String seletedNumListStr, List<Byte> speeds, int onOff) {
        byte[] cmd = new byte[29];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 18;
        cmd[7] = (byte) 20;
        cmd[8] = (byte) 32;
        cmd[9] = (byte) onOff;
        handModeNumSeleted(seletedNumListStr);
        cmd[10] = (byte) this.realModeNum;
        cmd[11] = (byte) this.frontModeNum;
        for (int i = 12; i < 28; i++) {
            if (i - 12 < speeds.size()) {
                cmd[i] = ((Byte) speeds.get(i - 12)).byteValue();
            } else {
                cmd[i] = (byte) 0;
            }
        }
        cmd[28] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        Main.lastSendCmd = Main.modeCmd;
        return cmd;
    }

    public byte[] getDeviceTimingCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 58, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getQeuryIrTimingOrder() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 82, (byte) 1, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getCtrlCtOtherTimingCmd(int xuHao, String type) {
        byte[] cmd = new byte[11];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 59;
        cmd[7] = (byte) 2;
        if (type.equals("del")) {
            cmd[8] = (byte) 2;
        }
        if (type.equals("on")) {
            cmd[8] = (byte) 1;
        }
        if (type.equals("off")) {
            cmd[8] = (byte) 0;
        }
        cmd[9] = (byte) xuHao;
        cmd[10] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("getCtrlCtOtherTimingCmd       " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getSynTimeToDeviceCmd(int frontYear, int realYear, int month, int day, int xinQi, int hour, int min, int sec) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 22, (byte) 8, (byte) frontYear, (byte) realYear, (byte) month, (byte) day, (byte) xinQi, (byte) hour, (byte) min, (byte) sec, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println(StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getOneZeroFiveMusicCmd(int dType, int roomNum, int dIndex, int musValue) {
        byte[] cmd = new byte[19];
        cmd[0] = (byte) 90;
        cmd[1] = (byte) -91;
        cmd[2] = (byte) -1;
        cmd[3] = (byte) 1;
        cmd[4] = (byte) 17;
        cmd[5] = (byte) -1;
        cmd[6] = (byte) -1;
        cmd[7] = (byte) -1;
        cmd[8] = (byte) -1;
        cmd[9] = (byte) 3;
        cmd[10] = (byte) 4;
        cmd[11] = (byte) dType;
        cmd[12] = (byte) roomNum;
        cmd[13] = (byte) dIndex;
        cmd[14] = (byte) musValue;
        cmd[15] = (byte) 1;
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            sum += cmd[i];
        }
        String hexString = Integer.toHexString(sum);
        if (hexString.length() == 3) {
            hexString = "0" + hexString;
        }
        if (hexString.length() == 2) {
            hexString = RcConstant.NON_FACTORY_RESET_STATUS + hexString;
        }
        String low = hexString.substring(hexString.length() - 2, hexString.length());
        String hei = hexString.substring(0, 2);
        cmd[16] = (byte) Integer.valueOf(low, 16).intValue();
        cmd[17] = (byte) Integer.valueOf(hei, 16).intValue();
        cmd[18] = (byte) 16;
        return cmd;
    }

    public byte[] getMusicCmdT(boolean musSwich, int musValue1, int musValue2, int musValue3) {
        byte[] cmd = new byte[13];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 19;
        cmd[7] = (byte) 4;
        cmd[8] = musSwich ? CmdVos.musOn : CmdVos.musOff;
        cmd[9] = (byte) tenToSixteen(musValue1);
        cmd[10] = (byte) tenToSixteen(musValue2);
        cmd[11] = (byte) tenToSixteen(musValue3);
        cmd[12] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        Main.lastSendCmd = Main.musicCmd;
        return cmd;
    }

    public byte[] subZero(byte[] dd) {
        byte[] data = null;
        for (int i = dd.length - 1; i > 0; i--) {
            if (dd[i] != (byte) 0) {
                data = new byte[(i + 1)];
                for (int k = 0; k < i + 1; k++) {
                    data[k] = dd[k];
                }
                return data;
            }
        }
        return data;
    }

    public byte[] hongWaiTest(byte[] data) {
        byte[] dd = data;
        if (dd == null) {
            return new byte[0];
        }
        byte[] cmd = new byte[(dd.length + 9)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 40;
        cmd[7] = (byte) dd.length;
        for (int i = 0; i < dd.length; i++) {
            cmd[i + 8] = dd[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        System.out.println("13326660525    hongWaiTestIrCode            " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] learnedRcCode(byte[] data) {
        byte[] dd = data;
        if (dd == null) {
            return new byte[0];
        }
        byte[] cmd = new byte[(dd.length + 8)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 43;
        for (int i = 0; i < dd.length; i++) {
            cmd[i + 7] = dd[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        boolean bb = "".equals(this.temptest);
        System.out.println("13326660525                " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getTimingRcParmas(byte[] ykData, int xuHao) {
        byte[] dd = ykData;
        byte[] cmd = new byte[(dd.length + 11)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 20;
        cmd[7] = (byte) (dd.length + 2);
        cmd[8] = (byte) xuHao;
        cmd[9] = (byte) 1;
        for (int i = 0; i < dd.length; i++) {
            cmd[i + 10] = dd[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        System.out.println("getTimingRcParmas      " + StringUtils.btye2Str2(cmd));
        cmdCount++;
        return cmd;
    }

    public byte[] getTimingRcParmasTwice(byte[] ykData, int xuHao) {
        byte[] dd = ykData;
        byte[] cmd = new byte[(dd.length + 11)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 20;
        cmd[7] = (byte) (dd.length + 2);
        cmd[8] = (byte) xuHao;
        cmd[9] = (byte) 1;
        for (int i = 0; i < dd.length; i++) {
            cmd[i + 10] = dd[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        System.out.println("getTimingRcParmas      " + StringUtils.btye2Str2(cmd));
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] hongWaiId(byte[] data) {
        byte[] dd = data;
        if (dd == null) {
            return new byte[0];
        }
        byte[] cmd = new byte[(dd.length + 9)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 42;
        cmd[7] = (byte) dd.length;
        for (int i = 0; i < dd.length; i++) {
            cmd[i + 8] = dd[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        System.out.println("13326660525    id            " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] irVersion() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 45, (byte) 0, (byte) -21};
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] irLearnDevice(int nonIrDeviceId, int learnType) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, CmdVos.musOff, (byte) 6, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) nonIrDeviceId, (byte) learnType, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] onOffK1RFAndK2RF(int nonIrDeviceId, int onOff) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, CmdVos.musOff, (byte) 6, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) nonIrDeviceId, (byte) onOff, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] ctrlMotor(byte[] motorCodeLib) {
        byte[] cmd = new byte[(motorCodeLib.length + 10)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 68;
        cmd[7] = (byte) 0;
        cmd[8] = (byte) motorCodeLib.length;
        for (int i = 0; i < motorCodeLib.length; i++) {
            cmd[i + 9] = motorCodeLib[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] getIr$LedRefersh(int dOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 54, (byte) 1, (byte) dOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] getIr$TkPanelRefersh(int dOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 88, (byte) 1, (byte) dOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] controlIrCtLight(int nonIrDeviceId, int controlType, boolean onOff, int brt, int c, int w) {
        System.out.println("int nonIrDevic eId = " + nonIrDeviceId + "    int controlType = " + controlType + "   boolean onOff=" + onOff + " int brt = " + brt + " int c" + c + " int w= " + w);
        byte[] cmd = new byte[22];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 51;
        cmd[7] = (byte) 13;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 2;
        cmd[13] = (byte) nonIrDeviceId;
        cmd[14] = (byte) controlType;
        cmd[15] = (byte) (onOff ? 1 : 0);
        cmd[16] = (byte) brt;
        cmd[17] = (byte) w;
        cmd[18] = (byte) c;
        cmd[19] = (byte) 0;
        cmd[20] = (byte) 0;
        cmd[21] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] controlIrRgbLight(int nonIrDeviceId, int controlType, boolean onOff, int brt, int r, int g, int b) {
        byte[] cmd = new byte[22];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 51;
        cmd[7] = (byte) 13;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 4;
        cmd[13] = (byte) nonIrDeviceId;
        cmd[14] = (byte) controlType;
        cmd[15] = (byte) (onOff ? 1 : 0);
        cmd[16] = (byte) brt;
        cmd[17] = (byte) r;
        cmd[18] = (byte) g;
        cmd[19] = (byte) b;
        cmd[20] = (byte) 0;
        cmd[21] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] controlIrRgbLightMode(int nonIrDeviceId, int controlType, int modeNum, int brt) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 51, (byte) 13, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) 4, (byte) nonIrDeviceId, (byte) controlType, (byte) modeNum, (byte) brt, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] controlIrRgbLightDiyModeEdit(int nonIrDeviceId, int modeNum, String name, int state, int type, int speed, int brt, int[] color, int colorSize) {
        int i;
        byte[] cmd = new byte[69];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 56;
        cmd[7] = (byte) 60;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 4;
        cmd[13] = (byte) nonIrDeviceId;
        cmd[14] = (byte) modeNum;
        try {
            byte[] temp = name.getBytes("utf-8");
            for (i = 0; i < 24; i++) {
                if (i < temp.length) {
                    cmd[i + 15] = temp[i];
                } else {
                    cmd[i + 15] = (byte) 0;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cmd[39] = (byte) state;
        cmd[40] = (byte) type;
        cmd[41] = (byte) speed;
        cmd[42] = (byte) brt;
        cmd[43] = (byte) colorSize;
        for (i = 0; i < color.length; i++) {
            cmd[(i * 3) + 44] = (byte) Color.red(color[i]);
            cmd[(i * 3) + 45] = (byte) Color.green(color[i]);
            cmd[(i * 3) + 46] = (byte) Color.blue(color[i]);
        }
        cmd[(color.length * 3) + 44] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] queryIrRgbLightDiyData(int dataType, int modeNum) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 57, (byte) 2, (byte) dataType, (byte) modeNum, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525           ddd     " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] queryRgbStaticColor() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 61, (byte) 2, (byte) 2, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525           ddd     " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] setStaticColor(int num, int red, int green, int blue, int brt, String name) {
        byte[] cmd = new byte[39];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 61;
        cmd[7] = (byte) 30;
        cmd[8] = (byte) 1;
        cmd[9] = (byte) num;
        cmd[10] = (byte) red;
        cmd[11] = (byte) green;
        cmd[12] = (byte) blue;
        cmd[13] = (byte) brt;
        try {
            int i;
            byte[] temp = name.getBytes("utf-8");
            for (i = 0; i < temp.length; i++) {
                cmd[i + 14] = temp[i];
            }
            for (i = 0; i < 24 - temp.length; i++) {
                cmd[(temp.length + 14) + i] = (byte) 0;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cmd[38] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525           ddd     " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] controlIrDimLight(NonIrDevice nonIrDevice, int controlType) {
        int i = 1;
        byte[] cmd = new byte[22];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 51;
        cmd[7] = (byte) 13;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 1;
        cmd[13] = (byte) nonIrDevice.nonIrDeviceId;
        cmd[14] = (byte) controlType;
        if (!nonIrDevice.onOff) {
            i = 0;
        }
        cmd[15] = (byte) i;
        cmd[16] = (byte) nonIrDevice.irCt1Brt;
        cmd[17] = (byte) nonIrDevice.irCt1W;
        cmd[18] = (byte) nonIrDevice.irCt1C;
        cmd[19] = (byte) 0;
        cmd[20] = (byte) 0;
        cmd[21] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] controlIrDimLight(int nonIrDeviceId, int controlType, boolean onOff, int brt) {
        int i = 1;
        System.out.println("int nonIrDevic eId = " + nonIrDeviceId + "    int controlType = " + controlType + "   boolean onOff=" + onOff + " int brt = " + brt);
        byte[] cmd = new byte[22];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 51;
        cmd[7] = (byte) 13;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 1;
        cmd[13] = (byte) nonIrDeviceId;
        cmd[14] = (byte) controlType;
        if (!onOff) {
            i = 0;
        }
        cmd[15] = (byte) i;
        cmd[16] = (byte) -1;
        cmd[17] = (byte) brt;
        cmd[18] = (byte) -1;
        cmd[19] = (byte) 0;
        cmd[20] = (byte) 0;
        cmd[21] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str3(cmd));
        return cmd;
    }

    public byte[] controlIrMBankLedLight(int dType, int CtrlType, int nonIrDeviceId, boolean onOff, int brt, int r, int g, int b, int w, int modeNum, int speed) {
        System.out.println("controlIrMBankLedLight dType = " + dType + "    CtrlType = " + CtrlType + "   nonIrDeviceId=" + nonIrDeviceId + " onOff = " + onOff + " brt" + brt + " r= " + r + " g= " + g + " b= " + b + " w= " + w + " modeNum= " + modeNum + " speed= " + speed);
        byte[] cmd = new byte[24];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 71;
        cmd[7] = (byte) 15;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) dType;
        cmd[13] = (byte) nonIrDeviceId;
        cmd[14] = (byte) CtrlType;
        cmd[15] = (byte) (onOff ? 1 : 0);
        cmd[16] = (byte) brt;
        cmd[17] = (byte) r;
        cmd[18] = (byte) g;
        cmd[19] = (byte) b;
        cmd[20] = (byte) w;
        cmd[21] = (byte) modeNum;
        cmd[22] = (byte) speed;
        cmd[23] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] controlIrTkPanel(int CtrlType, NonIrDevice nonIrDevice, int sceneOder) {
        int i;
        byte[] cmd = new byte[19];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 85;
        cmd[7] = (byte) 10;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 1;
        cmd[13] = (byte) nonIrDevice.nonIrDeviceId;
        cmd[14] = (byte) CtrlType;
        StringBuilder sb = new StringBuilder("0000");
        for (i = 3; i > -1; i--) {
            if (((TkPanelChannelVo) nonIrDevice.tkPanelChannelVos.get(i)).isOnOff()) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        System.out.println(sb.toString());
        cmd[15] = (byte) Integer.parseInt(sb.toString(), 2);
        sb = new StringBuilder("0000");
        for (i = 3; i > -1; i--) {
            if (((TkPanelChannelVo) nonIrDevice.tkPanelChannelVos.get(i)).isSelected()) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        cmd[16] = (byte) Integer.parseInt(sb.toString(), 2);
        cmd[17] = (byte) sceneOder;
        cmd[18] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] tkPanelAssociateLamp(int CtrlType, NonIrDevice nonIrDevice, int lampId, int panelSceneId, int appSceneId) {
        byte[] cmd = new byte[20];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 87;
        cmd[7] = (byte) 11;
        cmd[8] = (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16);
        cmd[9] = (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16);
        cmd[10] = (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16);
        cmd[11] = (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16);
        cmd[12] = (byte) 1;
        cmd[13] = (byte) nonIrDevice.nonIrDeviceId;
        cmd[14] = (byte) CtrlType;
        StringBuilder sb = new StringBuilder("0000");
        for (int i = 3; i > -1; i--) {
            if (((TkPanelChannelVo) nonIrDevice.tkPanelChannelVos.get(i)).isSelected()) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        cmd[15] = (byte) Integer.parseInt(sb.toString(), 2);
        cmd[16] = (byte) lampId;
        cmd[17] = (byte) panelSceneId;
        cmd[18] = (byte) appSceneId;
        cmd[19] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("tkPanelAssociateLamp                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] getTkPanelAssociatedLamp(int dId) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 89, (byte) 1, (byte) dId, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] controlIrM16sBankLedLight(int nonIrDeviceId, int keyValue, boolean isLongClick) {
        System.out.println("controlIrMBankLedLight nonIrDeviceId = " + nonIrDeviceId + "    keyValue = " + keyValue + "   isLongClick=" + isLongClick);
        byte[] cmd = new byte[12];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 74;
        cmd[7] = (byte) 3;
        cmd[8] = (byte) nonIrDeviceId;
        cmd[9] = (byte) keyValue;
        cmd[10] = (byte) (isLongClick ? 255 : 0);
        cmd[11] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] saveIrMBankLedLight(int dType, int CtrlType, int nonIrDeviceId, int onOff, int brt, int r, int g, int b, int w, int modeNum, int speed) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 71, (byte) 15, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) dType, (byte) nonIrDeviceId, (byte) CtrlType, (byte) onOff, (byte) brt, (byte) r, (byte) g, (byte) b, (byte) w, (byte) modeNum, (byte) speed, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] irLearnLight(int nonIrDeviceId, int learnType, int learnStatus) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 48, (byte) 7, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) learnType, (byte) nonIrDeviceId, (byte) learnStatus, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryIr$Devices() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 53, (byte) 1, (byte) 0, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] resetIr$Devices(int did) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 55, (byte) 1, (byte) did, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryIr$LightOder() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 52, (byte) 1, (byte) 0, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryIr$LedLightOder(int type) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 72, (byte) 1, (byte) type, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryIr$TkPanelOder() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 84, (byte) 1, (byte) 1, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryIr$PanelOder(int panelType) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 69, (byte) 1, (byte) panelType, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] delIrPanel(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 70, (byte) 1, (byte) ir$deviceOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] delIrLight(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 49, (byte) 6, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) 3, (byte) ir$deviceOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] learnKeySwitch(int learnState) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 104, (byte) 1, (byte) learnState, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] associateScene(int ir$deviceOder, int channelNum, int sceneNum) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 105, (byte) 4, (byte) 1, (byte) ir$deviceOder, (byte) channelNum, (byte) sceneNum, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] associateDevice(int ir$deviceOder, int channelNum, int deviceType, int deviceNum, int function) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 105, (byte) 6, (byte) 2, (byte) ir$deviceOder, (byte) channelNum, (byte) deviceType, (byte) deviceNum, (byte) function, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] cancelAssociate(int ir$deviceOder, int channelNum) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 105, (byte) 3, (byte) 3, (byte) ir$deviceOder, (byte) channelNum, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] deleteKeySwitch(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 105, (byte) 3, (byte) 4, (byte) ir$deviceOder, (byte) 0, (byte) -21};
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] queryKeySwitchFunction(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 105, (byte) 3, (byte) 5, (byte) ir$deviceOder, (byte) 0, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryWirelessSenorOrder(int learnState) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 98, (byte) 1, (byte) learnState, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] learnWirelessSensor(int ir$deviceOder, int sensorType) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 99, (byte) 2, (byte) sensorType, (byte) ir$deviceOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] deleteWirelessSensor(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 100, (byte) 1, (byte) ir$deviceOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] queryWirelessSensorState(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 101, (byte) 3, (byte) 2, (byte) ir$deviceOder, (byte) 0, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] setWirelessSensorState(int ir$deviceOder, WirelessSensorVo vo) {
        int i;
        int i2 = 0;
        byte[] cmd = new byte[((vo.getCtlData() == null ? 0 : vo.getCtlData().length) + 21)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = (byte) 0;
        cmd[5] = (byte) 0;
        cmd[6] = (byte) 101;
        cmd[7] = (byte) ((vo.getCtlData() == null ? 0 : vo.getCtlData().length) + 12);
        cmd[8] = (byte) 1;
        cmd[9] = (byte) ir$deviceOder;
        cmd[10] = (byte) vo.getSensorType();
        cmd[11] = (byte) vo.getStartWeek();
        cmd[12] = (byte) vo.getStartHour();
        cmd[13] = (byte) vo.getStartMin();
        cmd[14] = (byte) vo.getEndHour();
        cmd[15] = (byte) vo.getEndMin();
        if (vo.isOnOff()) {
            i = 1;
        } else {
            i = 0;
        }
        cmd[16] = (byte) i;
        cmd[17] = (byte) vo.getTriggerType();
        cmd[18] = (byte) vo.getDelayTime();
        cmd[19] = (byte) (vo.getCtlData() == null ? 0 : vo.getCtlData().length);
        if (vo.getCtlData() != null) {
            for (int i3 = 0; i3 < vo.getCtlData().length; i3++) {
                cmd[i3 + 20] = vo.getCtlData()[i3];
            }
        }
        if (vo.getCtlData() != null) {
            i2 = vo.getCtlData().length;
        }
        cmd[i2 + 20] = (byte) -21;
        cmdCount++;
        return cmd;
    }

    public byte[] conctrolIrM16sBankLisght(int ir$deviceOder, int function) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 74, (byte) 3, (byte) ir$deviceOder, (byte) function, (byte) 0, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] setIrM16sBankLisght(int ir$deviceOder, int functionType, String functionNum) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 75, (byte) 4, (byte) ir$deviceOder, (byte) functionType, (byte) Integer.parseInt(functionNum.substring(0, 2), 16), (byte) Integer.parseInt(functionNum.substring(2, 4), 16), (byte) -21};
        cmdCount++;
        System.out.println("13326660525                " + StringUtils.btye2Str2(cmd));
        return cmd;
    }

    public byte[] delIrMBankLight(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 73, (byte) 1, (byte) ir$deviceOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] delIrTkPanel(int ir$deviceOder) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, (byte) 0, (byte) 0, (byte) 86, (byte) 6, (byte) Integer.parseInt(userIdHexString.substring(0, 2), 16), (byte) Integer.parseInt(userIdHexString.substring(2, 4), 16), (byte) Integer.parseInt(userIdHexString.substring(4, 6), 16), (byte) Integer.parseInt(userIdHexString.substring(6, 8), 16), (byte) 1, (byte) ir$deviceOder, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] TiminghongWaiId(byte[] data) {
        byte[] dd = data;
        if (dd == null) {
            return new byte[0];
        }
        byte[] cmd = new byte[(dd.length + 9)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 42;
        cmd[7] = (byte) dd.length;
        for (int i = 0; i < dd.length; i++) {
            cmd[i + 8] = dd[i];
        }
        cmd[cmd.length - 1] = (byte) -21;
        cmdCount++;
        boolean bb = "".equals(this.temptest);
        return cmd;
    }

    public byte[] hongWaiLearn() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 41, (byte) 1, (byte) 0, (byte) -21};
        cmdCount++;
        return cmd;
    }

    public byte[] tttttttttttt(boolean musSwich, int musValue1, int musValue2, int musValue3) {
        byte[] cmd = new byte[13];
        cmd[9] = (byte) tenToSixteen(musValue1);
        return cmd;
    }

    public byte[] getMusicCmd(boolean musSwich, int musValue) {
        byte[] cmd = new byte[11];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 19;
        cmd[7] = (byte) 2;
        cmd[8] = musSwich ? CmdVos.musOn : CmdVos.musOff;
        cmd[9] = (byte) tenToSixteen(musValue);
        cmd[10] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        Main.lastSendCmd = Main.musicCmd;
        return cmd;
    }

    public void handModeNumSeleted(String seletedNumListStr) {
        this.frontModeNum = toHex(seletedNumListStr.substring(0, 8));
        this.realModeNum = toHex(seletedNumListStr.substring(8));
    }

    public int toHex(String s) {
        return Integer.valueOf(Integer.toHexString(Integer.parseInt(s, 2)), 16).intValue();
    }

    public byte[] getOutletSwich(String deviceType, boolean musSwich) {
        byte[] cmd = new byte[10];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        if (deviceType.equals("usb")) {
            cmd[6] = (byte) 35;
        } else {
            cmd[6] = (byte) 36;
        }
        cmd[7] = (byte) 1;
        cmd[8] = musSwich ? (byte) 97 : (byte) 98;
        cmd[9] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getOutletSwich(boolean musSwich) {
        byte b = (byte) 1;
        byte[] cmd = new byte[10];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 41;
        cmd[7] = (byte) 1;
        if (!musSwich) {
            b = (byte) 0;
        }
        cmd[8] = b;
        cmd[9] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getOutletLed(boolean musSwich, int stylePosi, int brt) {
        byte b = (byte) 1;
        byte[] cmd = new byte[12];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 38;
        cmd[7] = (byte) 3;
        if (!musSwich) {
            b = (byte) 2;
        }
        cmd[8] = b;
        cmd[9] = (byte) stylePosi;
        cmd[10] = (byte) brt;
        cmd[11] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] synProgram2DeviceCmd(byte[] data, byte fileFramesH1, byte fileFramesH2, byte fileFramesH3, byte fileFramesH4, byte oderH1, byte oderH2, byte oderH3, byte oderH4, byte crc32H1, byte crc32H2, byte crc32H3, byte crc32H4) {
        byte[] cmd = new byte[(data.length + 9)];
        cmd[0] = (byte) 16;
        cmd[1] = (byte) -103;
        cmd[2] = (byte) 0;
        cmd[3] = (byte) data.length;
        cmd[4] = (byte) (data.length + 9);
        cmd[5] = (byte) data.length;
        for (int i = 0; i < data.length; i++) {
            cmd[i + 7] = data[i];
        }
        cmd[(data.length + 9) - 2] = this.pwd12;
        cmd[(data.length + 9) - 1] = (byte) 22;
        return cmd;
    }

    public byte[] getSynTimeToOutletCmd(int frontYear, int realYear, int month, int day, int xinQi, int hour, int min, int sec) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 38, (byte) 8, (byte) frontYear, (byte) realYear, (byte) month, (byte) day, (byte) xinQi, (byte) hour, (byte) min, (byte) sec, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getOutletTimingCmd(String seletedDaysNumLists, int timgingOder, String hour, String min, String timgOnOffType, String deviceType) {
        byte[] cmd = new byte[15];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 37;
        cmd[7] = (byte) 6;
        cmd[8] = (byte) timgingOder;
        cmd[9] = (byte) toHex(seletedDaysNumLists);
        cmd[10] = (byte) tenToSixteen(Integer.parseInt(hour));
        cmd[11] = (byte) tenToSixteen(Integer.parseInt(min));
        if (timgOnOffType.equals("on")) {
            cmd[12] = (byte) 1;
        }
        if (timgOnOffType.equals("off")) {
            cmd[12] = (byte) 0;
        }
        if (timgOnOffType.equals("timingOff")) {
            cmd[12] = (byte) 2;
        }
        if (deviceType.equals("usb")) {
            cmd[13] = (byte) 35;
        } else {
            cmd[13] = (byte) 36;
        }
        cmd[14] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getSceneTitleCmd() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 92, (byte) 1, (byte) -1, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getStartSceneCmd(int sceneId) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 93, (byte) 1, (byte) sceneId, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getDelSceneCmd(int sceneId) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 94, (byte) 3, (byte) 1, (byte) sceneId, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getSceneInfoCmd(int sceneId) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 92, (byte) 1, (byte) sceneId, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getSaveSceneInfoCmd(SceneVo sceneVo, int step, byte[] sceneStepDataFeild) {
        int i;
        byte[] temp;
        byte[] cmd = new byte[((sceneStepDataFeild.length + 77) + 12)];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 90;
        String dataLenth = Integer.toHexString(cmd.length - 10).toUpperCase();
        for (i = dataLenth.length(); i < 4; i++) {
            dataLenth = "0" + dataLenth;
        }
        cmd[7] = (byte) Integer.parseInt(dataLenth.substring(0, 2), 16);
        cmd[8] = (byte) Integer.parseInt(dataLenth.substring(2, 4), 16);
        cmd[9] = (byte) sceneVo.getId();
        cmd[10] = (byte) (step + 1);
        cmd[11] = (byte) (sceneVo.getInnerRcVos().size() - 1);
        int dType = ((InnerRcVo) sceneVo.getInnerRcVos().get(step)).getmType();
        cmd[12] = (byte) getGatewayDistinguishDeviceType(dType);
        cmd[13] = (byte) dType;
        String did = Long.toHexString(((InnerRcVo) sceneVo.getInnerRcVos().get(step)).getBindedDid());
        for (i = did.length(); i < 18; i++) {
            did = "0" + did;
        }
        cmd[14] = (byte) Integer.parseInt(did.substring(0, 2), 16);
        cmd[15] = (byte) Integer.parseInt(did.substring(2, 4), 16);
        cmd[16] = (byte) Integer.parseInt(did.substring(4, 6), 16);
        cmd[17] = (byte) Integer.parseInt(did.substring(6, 8), 16);
        cmd[18] = (byte) Integer.parseInt(did.substring(8, 10), 16);
        cmd[19] = (byte) Integer.parseInt(did.substring(10, 12), 16);
        cmd[20] = (byte) Integer.parseInt(did.substring(12, 14), 16);
        cmd[21] = (byte) Integer.parseInt(did.substring(14, 16), 16);
        cmd[22] = (byte) Integer.parseInt(did.substring(16, 18), 16);
        cmd[23] = (byte) 0;
        cmd[24] = (byte) 0;
        cmd[25] = (byte) 0;
        cmd[26] = (byte) ((InnerRcVo) sceneVo.getInnerRcVos().get(step)).getSpaceTime();
        try {
            temp = ((InnerRcVo) sceneVo.getInnerRcVos().get(step)).getName().getBytes("utf-8");
            for (i = 0; i < 36; i++) {
                if (i < temp.length) {
                    cmd[i + 27] = temp[i];
                } else {
                    cmd[i + 27] = (byte) 0;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            temp = ((InnerRcVo) sceneVo.getInnerRcVos().get(step)).getStatus().getBytes("utf-8");
            for (i = 0; i < 24; i++) {
                if (i < temp.length) {
                    cmd[i + 63] = temp[i];
                } else {
                    cmd[i + 63] = (byte) 0;
                }
            }
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        cmd[87] = (byte) sceneStepDataFeild.length;
        System.arraycopy(sceneStepDataFeild, 0, cmd, 88, sceneStepDataFeild.length);
        cmd[cmd.length - 1] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] geChangeSceneNameCmd(int sceneId, String name) {
        byte[] cmd = new byte[37];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 96;
        cmd[7] = (byte) 28;
        cmd[8] = (byte) 1;
        cmd[9] = (byte) sceneId;
        cmd[10] = (byte) 1;
        cmd[11] = (byte) 0;
        for (int i = 0; i < name.getBytes().length; i++) {
            cmd[i + 12] = name.getBytes()[i];
        }
        cmd[36] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] geChangeDeviceNameCmd(MyRcDevice device) {
        byte[] cmd = new byte[49];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 96;
        cmd[7] = (byte) 40;
        cmd[8] = (byte) 2;
        cmd[9] = (byte) 0;
        cmd[10] = (byte) getGatewayDistinguishDeviceType(device.mType);
        cmd[11] = (byte) device.nonIrDevice.nonIrDeviceId;
        for (int i = 0; i < device.mName.getBytes().length; i++) {
            cmd[i + 12] = device.mName.getBytes()[i];
        }
        cmd[48] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("设备名称获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public int getGatewayDistinguishDeviceType(int dType) {
        int i = 1;
        if (dType >= 1 && dType <= 8) {
            return 0;
        }
        if (dType >= 10 && dType <= 11) {
            return 1;
        }
        if (dType >= 14 && dType <= 21) {
            return 4;
        }
        if (dType == 22) {
            return 4;
        }
        int i2;
        if (dType == 12) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (((dType == 132 ? 1 : 0) | ((((i2 | (dType == 23 ? 1 : 0)) | (dType == 26 ? 1 : 0)) | (dType == 133 ? 1 : 0)) | (dType == 134 ? 1 : 0))) != 0) {
            return 3;
        }
        int i3;
        if (dType == 13) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        if (dType != 25) {
            i = 0;
        }
        if ((i | i3) != 0) {
            return 2;
        }
        if (dType == 24) {
            return 6;
        }
        if (dType >= 27 && dType <= 29) {
            return 7;
        }
        if (dType == 30) {
            return 8;
        }
        if (dType == 34) {
            return 10;
        }
        return 1;
    }

    public byte[] getIr$DeviceNameCmd(int dType) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 97, (byte) 1, (byte) getGatewayDistinguishDeviceType(dType), (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getIr$LightIconSetCmd(int lightId, int iconId) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 107, (byte) 3, (byte) 1, (byte) lightId, (byte) iconId, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] getIr$LightGroupCmd(int dType) {
        byte[] cmd = new byte[16];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 108;
        cmd[7] = (byte) 7;
        cmd[8] = (byte) dType;
        cmd[9] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("设备名称获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getIrFactoryResetStatusCmd(int dType) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 106, (byte) 2, (byte) dType, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("设备名称获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getSynIrAllLightGroup() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 108, (byte) 3, (byte) 3, (byte) 2, (byte) -1, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("设备名称获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getSynIrLightGroupNum() {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 108, (byte) 2, (byte) 4, (byte) 0, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("设备名称获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getSynIrLightGroup(int num) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 108, (byte) 3, (byte) 3, (byte) 1, (byte) num, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("单个组获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getDeleteIrLightGroup(int num) {
        byte[] cmd = new byte[]{(byte) 102, (byte) -69, (byte) cmdCount, (byte) -64, this.pwd12, this.pwd34, (byte) 108, (byte) 2, (byte) 2, (byte) num, (byte) -21};
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("单个组获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getCreateIrLightGroup(int groupNum, int groupType, int selectedDeviceFrontBit, int selectedDeviceRealBit, String groupName) {
        byte[] cmd = new byte[52];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 108;
        cmd[7] = (byte) 43;
        cmd[8] = (byte) 1;
        cmd[9] = (byte) groupNum;
        cmd[10] = (byte) groupType;
        cmd[11] = (byte) selectedDeviceFrontBit;
        cmd[12] = (byte) selectedDeviceRealBit;
        cmd[13] = (byte) 0;
        cmd[14] = (byte) 0;
        for (int i = 0; i < groupName.getBytes().length; i++) {
            cmd[i + 15] = groupName.getBytes()[i];
        }
        cmd[51] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("设备名称获取 " + StringUtils.btye2Str(cmd));
        return cmd;
    }

    public byte[] getIrGeoSetting(boolean isGohome) {
        int i = 1;
        byte[] cmd = new byte[12];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 109;
        cmd[7] = (byte) 3;
        cmd[8] = (byte) 2;
        if (!isGohome) {
            i = 2;
        }
        cmd[9] = (byte) i;
        cmd[10] = (byte) 0;
        cmd[11] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        return cmd;
    }

    public byte[] setIrGeoSetting(GeoSetting setting) {
        int i;
        String seletedDay2Hex = "";
        List<RepeatDayVo> repeatDayVos = setting.getRepeatDayVos();
        if (((RepeatDayVo) repeatDayVos.get(0)).isSeleted()) {
            seletedDay2Hex = "01111111";
        } else {
            for (int i2 = 0; i2 < repeatDayVos.size(); i2++) {
                if (((RepeatDayVo) repeatDayVos.get(i2)).isSeleted()) {
                    seletedDay2Hex = seletedDay2Hex + 1;
                } else {
                    seletedDay2Hex = seletedDay2Hex + 0;
                }
            }
        }
        System.out.println("repeatData set: " + seletedDay2Hex);
        byte[] cmd = new byte[19];
        cmd[0] = (byte) 102;
        cmd[1] = (byte) -69;
        cmd[2] = (byte) cmdCount;
        cmd[3] = (byte) -64;
        cmd[4] = this.pwd12;
        cmd[5] = this.pwd34;
        cmd[6] = (byte) 109;
        cmd[7] = (byte) 10;
        cmd[8] = (byte) 1;
        if (setting.getName().equalsIgnoreCase(MyApp.getApp().getString(R.string.home_immediately))) {
            i = 1;
        } else {
            i = 2;
        }
        cmd[9] = (byte) i;
        if (setting.isStart()) {
            i = 1;
        } else {
            i = 0;
        }
        cmd[10] = (byte) i;
        cmd[11] = (byte) setting.getTouchSceneIndex();
        cmd[12] = (byte) Integer.parseInt(seletedDay2Hex, 2);
        if (setting.isFullTime()) {
            i = 1;
        } else {
            i = 0;
        }
        cmd[13] = (byte) i;
        cmd[14] = (byte) Integer.parseInt(setting.getStartTime().split(":")[0]);
        cmd[15] = (byte) Integer.parseInt(setting.getStartTime().split(":")[1]);
        cmd[16] = (byte) Integer.parseInt(setting.getEndTime().split(":")[0]);
        cmd[17] = (byte) Integer.parseInt(setting.getEndTime().split(":")[1]);
        cmd[18] = (byte) -21;
        if (cmdCount > 255) {
            cmdCount = 0;
        }
        cmdCount++;
        System.out.println("setIrGeoSetting " + StringUtils.btye2Str3(cmd));
        return cmd;
    }
}
