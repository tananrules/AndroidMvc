Version: 1.1.10
* Bump up Android sdk level and support library version

Version: 1.1.9
Fix bug
* When a recovering fragment in view pager its onViewReady is not called when its holding activity resume by popping out from another above activity.
* Correct the logic how to detect onReturnFromBackground

Version: 1.1.8
Fix issue that onViewReady not called in nested fragment of the popping out fragment when the activity is not killed by OS
Make sure onStartUp is called after onViewReady is finished for fragment extends DelegateFragment

Version: 1.1.7
Once root fragment is restored, it should notify all nested fragments they are not managed by the root fragment any more
Fix issue that nested fragments of popping out fragment won't call onReturnFromBackground incorrectly

Version: 1.1.6
Fix issue that onViewReady is called with incorrect reason - RESTORE which should be FIRST_TIME when the fragment is a page and recreated by FragmentStatePagerAdapter

Version: 1.1.5
Allow remove preference key from SharedPreferenceService
Naming convention cleaning up

Version: 1.1.4
Fix the bug that navigation may crash the app when the app just resumes from other app such as facebook login

Version: 1.1.3
* Broke Android SDK dependency out from Android Mvc Controller module
* Update Espresso to 2.2

Version: 1.1.2
Prevent a warning about failed cast to save and restore controller state which is deliberate.

Version: 1.1.1
Refactor library Poke by moving Graph.OnFreedListener to Provider.OnFreedListener and some document updates.

Version: 1.1.0
Publish below extensions
* android-mvc-service-core
* android-mvc-controller-retrofit

Version: 1.0.2
Fix issue that exceptions during posting event are not thrown out properly

Version: 1.0.1
Bug fixes

version: 1.0
Initial release