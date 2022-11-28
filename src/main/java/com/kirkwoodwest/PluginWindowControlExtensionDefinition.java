package com.kirkwoodwest;
import java.util.UUID;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

public class PluginWindowControlExtensionDefinition extends ControllerExtensionDefinition
{
   private static final UUID DRIVER_ID = UUID.fromString("3ea6d454-fc86-41b2-8dbc-081da0f89013");
   
   public PluginWindowControlExtensionDefinition()
   {
   }

   @Override
   public String getName()
   {
      return "Plugin Window Control";
   }
   
   @Override
   public String getAuthor()
   {
      return "kirkwoodwest";
   }

   @Override
   public String getVersion()
   {
      return "0.2";
   }

   @Override
   public UUID getId()
   {
      return DRIVER_ID;
   }
   
   @Override
   public String getHardwareVendor()
   {
      return "Kirkwood West";
   }
   
   @Override
   public String getHardwareModel()
   {
      return "Plugin Window Control";
   }

   @Override
   public int getRequiredAPIVersion()
   {
      return 17;
   }

   @Override
   public int getNumMidiInPorts()
   {
      return 0;
   }

   @Override
   public int getNumMidiOutPorts()
   {
      return 0;
   }

   @Override
   public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType)
   {
   }

   @Override
   public PluginWindowControlExtension createInstance(final ControllerHost host)
   {
      return new PluginWindowControlExtension(this, host);
   }
}
