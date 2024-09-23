package utility

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SecuredPreferenceHelper @Inject constructor(
    @ApplicationContext val context: Context,
    val sharedPreferences: SharedPreferences
) {

    fun saveStringSecuredPreference(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringSecuredPreference(key: String?, defaultValue: String?): String {
        return sharedPreferences.getString(key, defaultValue)!!
    }

    fun saveBooleanSecuredPreference(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBooleanSecuredPreference(key: String?,defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

}