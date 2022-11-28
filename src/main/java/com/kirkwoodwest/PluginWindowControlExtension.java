package com.kirkwoodwest;

import com.bitwig.extension.controller.api.*;
import com.bitwig.extension.controller.ControllerExtension;

import java.util.ArrayList;

public class PluginWindowControlExtension extends ControllerExtension {


  private Signal settingOpenWindows;
  private Signal settingCloseWindows;
  private ArrayList<Device> deviceList = new ArrayList<Device>();
   private Signal popUpBrowser;
   private PopupBrowser browesr;
   private Browser deviceBrowser;


  protected PluginWindowControlExtension(final PluginWindowControlExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  private int NUM_DEVICES = 12;
  private int NUM_LAYERS = 3;

  @Override
  public void init() {
    final ControllerHost host = getHost();

    // TODO: Perform your driver initialization here.
    // For now just show a popup notification for verification that it is running.
    host.showPopupNotification("Plugin Window Control Initialized");

    CursorTrack cursorTrack = host.createCursorTrack("Cursor Track", "Cursor Track", 0, 0, true);
    DeviceBank  deviceBank  = cursorTrack.createDeviceBank(NUM_DEVICES);
    for (int i = 0; i < NUM_DEVICES; i++) {
      Device device = deviceBank.getDevice(i);
      processDevice(device);

       DeviceBank db = device.createSiblingsDeviceBank(NUM_DEVICES);
      for (int j = 0; j < NUM_DEVICES; j++) {
       Device d2 = db.getItemAt(j);
       processDevice(d2);
      }
    }


    settingOpenWindows = host.getDocumentState().getSignalSetting("Open", "Window Management", "Open Plugin Windows");
    settingCloseWindows = host.getDocumentState().getSignalSetting("Close", "Window Management", "Close Plugin Windows");
    settingOpenWindows.addSignalObserver(() -> showPluginWindows(true));
    settingCloseWindows.addSignalObserver(() -> showPluginWindows(false));

//    popUpBrowser = host.getDocumentState().getSignalSetting("PopUp Browser", "Browser Management", "PopUp Browser");
//    popUpBrowser.addSignalObserver(() ->popupBrowser());
//    browesr =       getHost().createPopupBrowser();
  //  deviceBrowser = deviceList.get(0).createDeviceBrowser(20,20);

  }

   private void popupBrowser() {

   }

   private void processDevice(Device  device ) {
     addDevice(device);
     DeviceLayerBank layerBank = device.createLayerBank(NUM_LAYERS);
     for (int j = 0; j < NUM_LAYERS; j++) {
        DeviceBank layerDeviceBank = layerBank.getItemAt(j).createDeviceBank(NUM_DEVICES);

        for (int k = 0; k < NUM_DEVICES; k++) {
           Device device2 = layerDeviceBank.getItemAt(k);
           addDevice(device2);
        }
     }

     DeviceBank deviceChain = device.deviceChain().createDeviceBank(NUM_DEVICES);

     for (int a = 0; a < NUM_LAYERS; a++) {
        Device device2 = deviceChain.getItemAt(a);
        addDevice(device2);

        DeviceLayerBank layerBank2 = device2.createLayerBank(NUM_LAYERS);
        for (int d = 0; d < NUM_LAYERS; d++) {
           DeviceBank layerDeviceBank = layerBank2.getItemAt(d).createDeviceBank(NUM_DEVICES);
           for (int k = 0; k < NUM_DEVICES; k++) {
              Device device3 = layerDeviceBank.getDevice(k);
              addDevice(device3);
           }
        }
     }

  }

  private void showPluginWindows(boolean b) {
    for (Device device : deviceList) {
      if (device.isPlugin().get()) {
        device.isWindowOpen().set(b);
        getHost().println("Devices: " + device.name().get());
      }
    }
  }


  private void addDevice(Device device) {
    device.isPlugin().markInterested();
    device.isWindowOpen().markInterested();
    device.name().markInterested();
    deviceList.add(device);
  }

  @Override
  public void exit() {
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    getHost().showPopupNotification("Plugin Window Control Exited");
  }

  @Override
  public void flush() {
    // TODO Send any updates you need here.
  }


}
