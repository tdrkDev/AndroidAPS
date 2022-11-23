package info.nightscout.androidaps

import android.content.Context
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import info.nightscout.androidaps.data.ProfileSealed
import info.nightscout.androidaps.extensions.pureProfileFromJson
import info.nightscout.core.profile.ProfileStoreObject
import info.nightscout.database.impl.AppRepository
import info.nightscout.interfaces.Config
import info.nightscout.interfaces.plugin.ActivePlugin
import info.nightscout.interfaces.profile.DefaultValueHelper
import info.nightscout.interfaces.profile.Profile
import info.nightscout.interfaces.profile.ProfileFunction
import info.nightscout.interfaces.profile.ProfileStore
import info.nightscout.interfaces.utils.HardLimits
import info.nightscout.rx.bus.RxBus
import info.nightscout.shared.interfaces.ResourceHelper
import info.nightscout.shared.sharedPreferences.SP
import info.nightscout.shared.utils.DateUtil
import org.json.JSONObject
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`

@Suppress("SpellCheckingInspection")
open class TestBaseWithProfile : TestBase() {

    @Mock lateinit var activePluginProvider: ActivePlugin
    @Mock lateinit var rh: ResourceHelper
    @Mock lateinit var profileFunction: ProfileFunction
    @Mock lateinit var defaultValueHelper: DefaultValueHelper
    @Mock lateinit var dateUtil: DateUtil
    @Mock lateinit var config: Config
    @Mock lateinit var sp: SP
    @Mock lateinit var context: Context
    @Mock lateinit var repository: AppRepository

    private lateinit var hardLimits: HardLimits
    lateinit var testPumpPlugin: TestPumpPlugin

    val rxBus = RxBus(aapsSchedulers, aapsLogger)

    val profileInjector = HasAndroidInjector {
        AndroidInjector {
            if (it is ProfileStoreObject) {
                it.aapsLogger = aapsLogger
                it.activePlugin = activePluginProvider
                it.config = config
                it.rh = rh
                it.rxBus = rxBus
                it.hardLimits = hardLimits
            }
        }
    }

    private lateinit var invalidProfileJSON: String
    private lateinit var validProfileJSON: String
    lateinit var validProfile: Profile
    lateinit var invalidProfile: Profile
    @Suppress("PropertyName") val TESTPROFILENAME = "someProfile"

    @Before
    fun prepareMock() {
        invalidProfileJSON = "{\"dia\":\"1\",\"carbratio\":[{\"time\":\"00:00\",\"value\":\"30\"}],\"carbs_hr\":\"20\",\"delay\":\"20\",\"sens\":[{\"time\":\"00:00\",\"value\":\"3\"}," +
            "{\"time\":\"2:00\",\"value\":\"3.4\"}],\"timezone\":\"UTC\",\"basal\":[{\"time\":\"00:00\",\"value\":\"1\"}],\"target_low\":[{\"time\":\"00:00\",\"value\":\"4.5\"}]," +
            "\"target_high\":[{\"time\":\"00:00\",\"value\":\"7\"}],\"startDate\":\"1970-01-01T00:00:00.000Z\",\"units\":\"mmol\"}"
        validProfileJSON = "{\"dia\":\"5\",\"carbratio\":[{\"time\":\"00:00\",\"value\":\"30\"}],\"carbs_hr\":\"20\",\"delay\":\"20\",\"sens\":[{\"time\":\"00:00\",\"value\":\"3\"}," +
            "{\"time\":\"2:00\",\"value\":\"3.4\"}],\"timezone\":\"UTC\",\"basal\":[{\"time\":\"00:00\",\"value\":\"1\"}],\"target_low\":[{\"time\":\"00:00\",\"value\":\"4.5\"}]," +
            "\"target_high\":[{\"time\":\"00:00\",\"value\":\"7\"}],\"startDate\":\"1970-01-01T00:00:00.000Z\",\"units\":\"mmol\"}"
        validProfile = ProfileSealed.Pure(pureProfileFromJson(JSONObject(validProfileJSON), dateUtil)!!)
        testPumpPlugin = TestPumpPlugin(profileInjector)
        `when`(activePluginProvider.activePump).thenReturn(testPumpPlugin)
        hardLimits = HardLimitsMock(sp, rh)
    }

    fun getValidProfileStore(): ProfileStore {
        val json = JSONObject()
        val store = JSONObject()
        store.put(TESTPROFILENAME, JSONObject(validProfileJSON))
        json.put("defaultProfile", TESTPROFILENAME)
        json.put("store", store)
        return ProfileStoreObject(profileInjector, json, dateUtil)
    }

    fun getInvalidProfileStore1(): ProfileStore {
        val json = JSONObject()
        val store = JSONObject()
        store.put(TESTPROFILENAME, JSONObject(invalidProfileJSON))
        json.put("defaultProfile", TESTPROFILENAME)
        json.put("store", store)
        return ProfileStoreObject(profileInjector, json, dateUtil)
    }

    fun getInvalidProfileStore2(): ProfileStore {
        val json = JSONObject()
        val store = JSONObject()
        store.put(TESTPROFILENAME, JSONObject(validProfileJSON))
        store.put("invalid", JSONObject(invalidProfileJSON))
        json.put("defaultProfile", TESTPROFILENAME + "invalid")
        json.put("store", store)
        return ProfileStoreObject(profileInjector, json, dateUtil)
    }
}