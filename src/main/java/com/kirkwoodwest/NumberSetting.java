package com.kirkwoodwest;


import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableRangedValue;

import java.util.function.Consumer;
import java.util.function.Function;

public class NumberSetting {
  private final int minValue;
  private final int maxValue;
  private final int resolution;
  private final SettableRangedValue setting;
  private Consumer<Integer> callback;

  public NumberSetting(ControllerHost host, Preferences preferences, String label, String category, int minValue, int maxValue, int resolution, String unit, int initValue, Consumer<Integer> callback){
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.resolution = resolution;
    this.callback = callback;
    setting = preferences.getNumberSetting(label, category, minValue, maxValue, resolution, unit, initValue);
    setting.addValueObserver(this::internalCallback);
  }

  public void internalCallback(Double value){
    if(callback != null) {
      Integer myValue = (int) translateValue(value);
      callback.accept(myValue);
    }
  }
  private int translateValue(Double value){
    return (int) Math.floor(((maxValue - minValue) * value)) + 1;
  }

  public SettableRangedValue getSetting(){
    return setting;
  }

  public int getValue(){
    return translateValue(setting.get());
  }
}
