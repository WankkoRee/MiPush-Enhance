package cn.wankkoree.xp.mipushenhance.hook

import android.content.Intent
import android.content.pm.ResolveInfo
import com.highcapable.kavaref.KavaRef.Companion.resolve
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit


@InjectYukiHookWithXposed
class HookEntry : IYukiHookXposedInit {

    override fun onInit() = configs {
        debugLog {
            tag = "MiPushEnhance"
        }
    }

    override fun onHook() = encase {
        loadSystem {
            "android.app.ApplicationPackageManager".toClass().resolve().method {
                name = "queryBroadcastReceivers"
                parameters(Intent::class, Int::class)
                returnType = List::class
            }.hookAll {
                after {
                    val list = result<MutableList<ResolveInfo>>() ?: mutableListOf()
                    if (list.isEmpty()) {
                        val r = ResolveInfo()
                        r.resolvePackageName = "com.miui.securitycenter"
                        list.add(r)
                    }
                    result = list
                }
            }
        }
    }
}