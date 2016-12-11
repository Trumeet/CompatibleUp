package kh.android.xposed.comlatibleup;


import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * Created by liangyuteng on 16-6-21.
 * Main hook codes.
 */

public class ControlCore implements IXposedHookZygoteInit,
        IXposedHookLoadPackage{
    private XC_MethodHook checkSdkVersionHook;
    private XSharedPreferences prefs;
    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable{

        checkSdkVersionHook = new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param)
                    throws Throwable {

                    XposedHelpers.setObjectField(param.thisObject,
                            "SDK_VERSION", 25);

            }

        };
        prefs = new XSharedPreferences(ControlCore.class.getPackage().getName());
        prefs.makeWorldReadable();
        prefs.reload();

        if(!prefs.getBoolean("ENABLE",true)){
            XposedBridge.log("CompatibleUp is NOT-ENABLE");
        }else{
            XposedBridge.log("Compatible-Up ENABLE");
        }

    }

    private static final String sMethodA="parsePackage";
    private static final String sMethodB="parseBaseApk";
    private static final String sMethodC="parseClusterPackage";
    private static final String sMethodD="parseSplitApk";

    private final String [] Method={sMethodA,sMethodB,sMethodC,sMethodD};
    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        prefs.reload();
        if(prefs.getBoolean("ENABLE",false)) {
            Class<?> packageParserClass = XposedHelpers.findClass(
                    "android.content.pm.PackageParser", lpparam.classLoader);
            for (String aMethod : Method) {
                XposedBridge.hookAllMethods(packageParserClass, aMethod,
                        checkSdkVersionHook);
            }
        }
    }

}
