package com.kirkwoodwest;

import com.bitwig.extension.controller.api.*;
import com.bitwig.extension.controller.ControllerExtension;

import java.util.ArrayList;

public class PluginWindowControlExtension extends ControllerExtension {


  private Signal settingOpenWindows;
  private Signal settingCloseWindows;
  private ArrayList<Device> cursorTrackDeviceList = new ArrayList<>();
  private ArrayList<Device> bankDeviceList = new ArrayList<>();
  private Signal settingAllOpenWindows;
  private Signal settingAllCloseWindows;


  protected PluginWindowControlExtension(final PluginWindowControlExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  private int NUM_DEVICES = 12;
  private int NUM_LAYERS = 32;

  @Override
  public void init() {
    final ControllerHost host = getHost();

    // TODO: Perform your driver initialization here.
    // For now just show a popup notification for verification that it is running.
    host.showPopupNotification("Plugin Window Control Initialized");

    int NUM_TRACKS = 32;
    TrackBank trackBank = host.createTrackBank(NUM_TRACKS, 0, 0, true);

    for (int i = 0; i < NUM_TRACKS; i++) {
      Channel channel = trackBank.getItemAt(i);
      processChannel(channel, bankDeviceList);
    }
    MasterTrack masterTrack = host.createMasterTrack(0);
    processChannel(masterTrack, bankDeviceList);
    
    CursorTrack cursorTrack = host.createCursorTrack("Cursor Track", "Cursor Track", 0, 0, true);
    processChannel(cursorTrack, cursorTrackDeviceList);

    settingOpenWindows = host.getDocumentState().getSignalSetting("Open", "Plugin Window Management", "Open Plugins");
    settingCloseWindows = host.getDocumentState().getSignalSetting("Close", "Plugin Window Management", "Close Plugins");
    settingOpenWindows.addSignalObserver(() -> showPluginWindows(true, cursorTrackDeviceList));
    settingCloseWindows.addSignalObserver(() -> showPluginWindows(false, cursorTrackDeviceList));

    settingAllOpenWindows = host.getDocumentState().getSignalSetting("Open", "All Plugin Window Management", "Open All Plugins ");
    settingAllCloseWindows = host.getDocumentState().getSignalSetting("Close", "All Plugin Window Management", "Close All Plugins");
    settingAllOpenWindows.addSignalObserver(() -> showPluginWindows(true, bankDeviceList));
    settingAllCloseWindows.addSignalObserver(() -> showPluginWindows(false, bankDeviceList));
  }

  private void processChannel(Channel channel, ArrayList<Device> deviceList){
    DeviceBank  deviceBank  = channel.createDeviceBank(NUM_DEVICES);
    for (int i = 0; i < NUM_DEVICES; i++) {
      Device device = deviceBank.getDevice(i);
      processDevice(device, deviceList);
      
    }

  }

   private void processDevice(Device  device, ArrayList<Device> deviceList ) {
     addDevice(device, deviceList);
     DeviceLayerBank layerBank = device.createLayerBank(NUM_LAYERS);
     for (int j = 0; j < NUM_LAYERS; j++) {
        DeviceBank layerDeviceBank = layerBank.getItemAt(j).createDeviceBank(NUM_DEVICES);

        for (int k = 0; k < NUM_DEVICES; k++) {
           Device device2 = layerDeviceBank.getItemAt(k);
           addDevice(device2, deviceList);
        }
     }
  }

  private void showPluginWindows(boolean b, ArrayList<Device> devices) {
    for (Device device : devices) {
      if (device.isPlugin().get()) {
        if(device.isWindowOpen().get() != b) {
          device.isWindowOpen().set(b);
          getHost().println("Devices: " + device.name().get());
        }
      }
    }
  }


  private void addDevice(Device device, ArrayList<Device> deviceList) {
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
