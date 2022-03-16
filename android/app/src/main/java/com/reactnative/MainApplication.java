package com.reactnative;


import android.app.Application;
import android.content.Context;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import org.reactnative.camera.RNCameraPackage;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;  
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.ReactActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import android.widget.Toast; 
import com.facebook.react.modules.core.DeviceEventManagerModule;


import com.reactnative.CustomToastPackage; // <-- 引入你自己的包

public class MainApplication extends Application implements ReactApplication {


  private static final String INTENT_ACTION_SCAN_RESULT="com.honeywell.ezservice.uniqueaction";
  private void sendEvent(ReactContext reactContext,String abc) {
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("globalEmitter_honeyWell", abc);
  }

  private BroadcastReceiver barcodeReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
          if (INTENT_ACTION_SCAN_RESULT.equals(intent.getAction())) {
              //获取扫描数据，并将扫描数据存放在barcodeData中
              final String barcodeData = intent.getStringExtra("data");

              // Toast.makeText( getApplicationContext(),barcodeData,Toast.LENGTH_SHORT ).show();
              ReactContext reactContext = getReactNativeHost().getReactInstanceManager().getCurrentReactContext();
              sendEvent(reactContext,barcodeData);
            }
      }
  };

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          packages.add(new CustomToastPackage()); // <-- 添加这一行，类名替换成你的Package类的名字 name.
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());

    registerReceiver(barcodeReceiver, new IntentFilter(INTENT_ACTION_SCAN_RESULT));

  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.mesapp.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
