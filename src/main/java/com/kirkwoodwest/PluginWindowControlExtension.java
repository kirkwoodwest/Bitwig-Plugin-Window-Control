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

  private int NUM_DEVICES = 4;
  private int NUM_LAYERS = 4;
  int NUM_TRACKS = 4;
  int NUM_SENDS = 1;

  @Override
  public void init() {
    final ControllerHost host = getHost();

    // TODO: Perform your driver initialization here.
    // For now just show a popup notification for verification that it is running.
    host.showPopupNotification("Plugin Window Control Initialized");

    Preferences prefrences = host.getPreferences();
    String category = "Track Count (Requires Restart)";

    NumberSetting numberTracks = new NumberSetting(host, prefrences,"# Tracks for All", category, 1, 64, 1, "Tracks", NUM_TRACKS, (value)->{host.println("number of tracks" + value );});
    NUM_TRACKS = numberTracks.getValue();

    NumberSetting numberSends = new NumberSetting(host, prefrences, "# Sends for All", category, 1, 16, 1, "Sends", NUM_SENDS, null);
    NUM_SENDS = numberSends.getValue();

    NumberSetting numberDevices = new NumberSetting(host, prefrences, "# Devices Per Channel", category, 1, 32, 1, "Devices", NUM_DEVICES, null);
    NUM_DEVICES = numberDevices.getValue();

    NumberSetting numberLayers = new NumberSetting(host, prefrences, "# Device Layers ", category, 1, 32, 1, "Device Layers", NUM_LAYERS, null);
    NUM_LAYERS = numberLayers.getValue();

    TrackBank trackBank = host.createTrackBank(NUM_TRACKS, NUM_SENDS, 0, true);

    for (int i = 0; i < NUM_TRACKS; i++) {
      Channel channel = trackBank.getItemAt(i);
      processChannel(channel, bankDeviceList);
    }

    MasterTrack masterTrack = host.createMasterTrack(0);
    processChannel(masterTrack, bankDeviceList);
    
    CursorTrack cursorTrack = host.createCursorTrack("Cursor Track", "Cursor Track", 0, 0, true);
    processChannel(cursorTrack, cursorTrackDeviceList);

    settingAllOpenWindows = host.getDocumentState().getSignalSetting("Open", "All Channels", "Open All Plugins ");
    settingAllCloseWindows = host.getDocumentState().getSignalSetting("Close", "All Channels", "Close All Plugins");
    settingAllOpenWindows.addSignalObserver(() -> showPluginWindows(true, bankDeviceList));
    settingAllCloseWindows.addSignalObserver(() -> showPluginWindows(false, bankDeviceList));

    settingOpenWindows = host.getDocumentState().getSignalSetting("Open", "Channel", "Open Plugins");
    settingCloseWindows = host.getDocumentState().getSignalSetting("Close", "Channel", "Close Plugins");
    settingOpenWindows.addSignalObserver(() -> showPluginWindows(true, cursorTrackDeviceList));
    settingCloseWindows.addSignalObserver(() -> showPluginWindows(false, cursorTrackDeviceList));
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
