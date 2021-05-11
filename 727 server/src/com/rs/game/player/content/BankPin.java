package com.rs.game.player.content;

import com.rs.Settings;
import com.rs.game.player.Player;

public class BankPin {
    private transient Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }


    public void openBankPin() {
        if (player.getTemporaryAttributtes().get("open_bank") != null) {
            if (player.startpin == false) {
                player.getInterfaceManager().sendInterface(13);
                player.getPackets().sendInterface(true, 13, 5, 759);
                player.getPackets().sendConfigByFile(1010, -1);
                player.getPackets().sendIComponentText(13, 27, "Bank of " + Settings.SERVER_NAME);
                player.startpin = true;
            } else {
                player.getInterfaceManager().sendInterface(13);
                player.getPackets().sendConfigByFile(1010, -1);
                player.getPackets().sendIComponentText(13, 27, "Bank of " + Settings.SERVER_NAME);
            }
        } else {
            if (player.startpin == false) {
                player.getInterfaceManager().sendInterface(13);
                player.getPackets().sendInterface(true, 13, 5, 759);
                player.getPackets().sendConfigByFile(1010, -1);
                player.getPackets().sendIComponentText(13, 27, "Bank of " + Settings.SERVER_NAME);
                player.startpin = true;
            } else {
                player.getInterfaceManager().sendInterface(13);
                player.getPackets().sendConfigByFile(1010, -1);
                player.getPackets().sendIComponentText(13, 27, "Bank of " + Settings.SERVER_NAME);
            }
        }
    }

    public void openPinSettings() {
        if (player.getSetPin() == true) {
            player.getInterfaceManager().sendInterface(14);
            player.getPackets().sendGlobalConfig(98, 1);
        } else if (player.getSetPin() == false) {
            player.getInterfaceManager().sendInterface(14);
            player.getPackets().sendGlobalConfig(98, 0);
        }
    }

    public void setPlayerBankPin() {
        player.getPackets().sendIComponentText(14, 32, "Do you really wish to set a PIN on your bank account?");
        player.getPackets().sendIComponentText(14, 34, "Yes, I really want a bank PIN. I will never forget it!");
        player.getPackets().sendIComponentText(14, 36, "No, I might forget it!");
        player.getInterfaceManager().sendInterface(14);
        player.getPackets().sendGlobalConfig(98, -1);
    }

    public void handleButtons(int interfaceId, int componentId) {
        if (interfaceId == 759) {
            switch (componentId) {
                case 4:// 0
                case 8:// 1
                case 12:// 2
                case 16:// 3
                case 20:// 4
                case 24:// 5
                case 28:// 6
                case 32:// 7
                case 36:// 8
                case 40:// 9
                    if (player.getTemporaryAttributtes().get("setting_bank_pin") != null) {
                        if (player.getPin()[0] == 0) {
                            player.getPin()[0] = componentId;
                        } else if (player.getPin()[1] == 0) {
                            player.getPin()[1] = componentId;
                        } else if (player.getPin()[2] == 0) {
                            player.getTemporaryAttributtes().put("last_pin_number", player);
                            player.getPin()[2] = componentId;
                        }
                    }
                    if (player.getTemporaryAttributtes().get("confirm_bank_pin") != null) {
                        if (player.getConfirmPin()[0] == 0) {
                            player.getConfirmPin()[0] = componentId;
                        } else if (player.getConfirmPin()[1] == 0) {
                            player.getConfirmPin()[1] = componentId;
                        } else if (player.getConfirmPin()[2] == 0) {
                            player.getTemporaryAttributtes().put("confirm_last", componentId);
                            player.getConfirmPin()[2] = componentId;
                        }
                    }
                    if (player.getTemporaryAttributtes().get("open_bank") != null) {
                        if (player.getOpenBankPin()[0] == 0) {
                            player.getOpenBankPin()[0] = componentId;
                        } else if (player.getOpenBankPin()[1] == 0) {
                            player.getOpenBankPin()[1] = componentId;
                        } else if (player.getOpenBankPin()[2] == 0) {
                            player.getTemporaryAttributtes().put("open_last", componentId);
                            player.getOpenBankPin()[2] = componentId;
                        }
                    }
                    if (player.getTemporaryAttributtes().get("change_pin") != null) {
                        if (player.getChangeBankPin()[0] == 0) {
                            player.getChangeBankPin()[0] = componentId;
                        } else if (player.getChangeBankPin()[1] == 0) {
                            player.getChangeBankPin()[1] = componentId;
                        } else if (player.getChangeBankPin()[2] == 0) {
                            player.getTemporaryAttributtes().put("change_last", componentId);
                            player.getChangeBankPin()[2] = componentId;
                        }
                    }
                    break;
            }
        } else if (interfaceId == 13) {
            switch (componentId) {
                case 6:// 0
                case 7:// 1
                case 8:// 2
                case 9:// 3
                case 10:// 4
                case 11:// 5
                case 12:// 6
                case 13:// 7
                case 14:// 8
                case 15:// 9
                    if (player.getTemporaryAttributtes().get("last_pin_number") != null) {
                        if (player.getPin()[3] == 0) {
                            player.getTemporaryAttributtes().remove("settings_bank_pin");
                            player.getTemporaryAttributtes().put("confirm_bank_pin", player);
                            player.getPin()[3] = componentId;
                            player.getInterfaceManager().sendInterface(13);
                            player.getPackets().sendConfigByFile(1010, -1);
                            player.getPackets().sendGameMessage("Please enter your pin again for confirmation.");
                            player.getTemporaryAttributtes().remove("last_pin_number");
                        }
                    }
                    if (player.getTemporaryAttributtes().get("confirm_last") != null) {
                        if (player.getConfirmPin()[3] == 0) {
                            player.getConfirmPin()[3] = componentId;
                        }
                        if (player.getPin()[0] != player.getConfirmPin()[0]
                                || player.getPin()[1] != player.getConfirmPin()[1]
                                || player.getPin()[2] != player.getConfirmPin()[2]
                                || player.getPin()[3] != player.getConfirmPin()[3]) {
                            if (player.getTemporaryAttributtes().get("delete_pin") != null) {
                                for (int i = 0; i < 4; i++) {
                                    player.getConfirmPin()[i] = 0;
                                }
                                player.closeInterfaces();
                                player.getPackets().sendGameMessage("The confirmation pin you entered did not match the original.");
                                player.getTemporaryAttributtes().remove("delete_pin");
                            } else {
                                for (int i = 0; i < 4; i++) {
                                    player.getPin()[i] = 0;
                                    player.getConfirmPin()[i] = 0;
                                }
                                player.closeInterfaces();
                                player.getPackets().sendGameMessage("The confirmation pin you entered did not match the original.");
                                player.getTemporaryAttributtes().remove("confirm_last");
                                player.getTemporaryAttributtes().remove("confirm_bank_pin");
                            }
                            return;
                        } else {
                            player.closeInterfaces();
                            if (player.getTemporaryAttributtes().get("delete_pin") != null) {
                                player.getPackets().sendGameMessage("You have successfully removed your bank pin.");
                                player.getTemporaryAttributtes().remove("confirm_last");
                                player.getTemporaryAttributtes().remove("confirm_bank_pin");
                                player.getTemporaryAttributtes().remove("delete_pin");
                                for (int i = 0; i < 4; i++) {
                                    player.getPin()[i] = 0;
                                    player.getConfirmPin()[i] = 0;
                                }
                                player.setPin = false;
                                return;
                            } else {
                                for (int i = 0; i < 4; i++) {
                                    player.getConfirmPin()[i] = 0;
                                }
                                player.getPackets().sendGameMessage("You have successfully set your bank pin.");
                                player.getTemporaryAttributtes().remove("confirm_last");
                                player.getTemporaryAttributtes().remove("confirm_bank_pin");
                                player.getTemporaryAttributtes().remove("delete_pin");
                                player.setPin = true;
                                return;
                            }
                        }
                    }
                    if (player.getTemporaryAttributtes().get("open_last") != null) {
                        if (player.getOpenBankPin()[3] == 0) {
                            player.getOpenBankPin()[3] = componentId;
                        }
                        if (player.getPin()[0] != player.getOpenBankPin()[0]
                                || player.getPin()[1] != player.getOpenBankPin()[1]
                                || player.getPin()[2] != player.getOpenBankPin()[2]
                                || player.getPin()[3] != player.getOpenBankPin()[3]) {
                            for (int i = 0; i < 4; i++) {
                                player.getOpenBankPin()[i] = 0;
                            }
                            player.closeInterfaces();
                            player.getPackets().sendGameMessage("The bank pin you entered was not correct.");
                            player.getTemporaryAttributtes().remove("open_last");
                            player.getTemporaryAttributtes().remove("open_bank");
                            player.openPin = false;
                            player.hasPinOpenedToday = false;
                        } else {
                            player.closeInterfaces();
                            for (int i = 0; i < 4; i++) {
                                player.getOpenBankPin()[i] = 0;
                            }
                            player.getPackets().sendGameMessage("You have entered your bank pin successfully.");
                            player.getTemporaryAttributtes().remove("open_last");
                            player.getTemporaryAttributtes().remove("open_bank");
                            player.openPin = true;
                            player.hasPinOpenedToday = true;
                            player.lastOpenedWithPin = System.currentTimeMillis();
                            player.getBank().openBank();
                        }
                    }
                    if (player.getTemporaryAttributtes().get("change_last") != null) {
                        if (player.getChangeBankPin()[3] == 0) {
                            player.getChangeBankPin()[3] = componentId;
                        }
                        if (player.getPin()[0] != player.getChangeBankPin()[0]
                                || player.getPin()[1] != player.getChangeBankPin()[1]
                                || player.getPin()[2] != player.getChangeBankPin()[2]
                                || player.getPin()[3] != player.getChangeBankPin()[3]) {
                            for (int i = 0; i < 4; i++) {
                                player.getChangeBankPin()[i] = 0;
                            }
                            player.closeInterfaces();
                            player.getPackets().sendGameMessage("The bank pin you entered was not correct.");
                            player.getTemporaryAttributtes().remove("change_last");
                            player.getTemporaryAttributtes().remove("change_pin");
                        } else {
                            for (int i = 0; i < 4; i++) {
                                player.getPin()[i] = 0;
                                player.getChangeBankPin()[i] = 0;
                            }
                            player.getTemporaryAttributtes().put("setting_bank_pin", player);
                            player.getPackets().sendGameMessage("Please enter a new bank pin number.");
                            player.getTemporaryAttributtes().remove("change_last");
                            player.getTemporaryAttributtes().remove("change_pin");
                            player.getInterfaceManager().sendInterface(13);
                            player.getPackets().sendConfigByFile(1010, -1);
                            player.setPin = false;
                        }
                    }
                    break;
            }
        } else if (interfaceId == 14) {
            System.out.println("Comp ID: " + componentId);
            switch (componentId) {
                case 18:
                    if (player.getSetPin() == true) {
                        player.getTemporaryAttributtes().put("change_pin", player);
                        openBankPin();
                        return;
                    }
                    setPlayerBankPin();
                    break;
                case 33:
                    player.getTemporaryAttributtes().put("setting_bank_pin", player);
                    for (int i = 0; i < 4; i++) {
                        player.getPin()[i] = 0;
                        player.getConfirmPin()[i] = 0;
                    }
                    openBankPin();
                    break;
                case 35:
                    player.closeInterfaces();
                    break;
                case 19:
                    player.getTemporaryAttributtes().put("delete_pin", player);
                    player.getTemporaryAttributtes().put("confirm_bank_pin", player);
                    openBankPin();
                    break;
            }
        }
    }

}
