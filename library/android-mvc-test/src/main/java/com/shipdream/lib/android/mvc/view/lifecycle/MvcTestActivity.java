/*
 * Copyright 2015 Kejun Xia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shipdream.lib.android.mvc.view.lifecycle;

import android.os.Bundle;
import android.view.View;

import com.shipdream.lib.android.mvc.view.MvcActivity;
import com.shipdream.lib.android.mvc.view.MvcFragment;

public class MvcTestActivity extends MvcActivity {

    @Override
    protected Class<? extends MvcFragment> mapNavigationFragment(String locationId) {
        return MvcTestFragment.class;
    }

    @Override
    protected Class<? extends DelegateFragment> getDelegateFragmentClass() {
        return HomeFragment.class;
    }

    public static class HomeFragment extends DelegateFragment {
        private boolean onViewStateRestoredCalled = false;

        @Override
        protected void onStartUp() {
            getNavigationController().navigateTo(this, "TestFragment", null);
        }

        @Override
        public void onViewReady(View view, Bundle savedInstanceState, Reason reason) {
            super.onViewReady(view, savedInstanceState, reason);

            if (reason == Reason.RESTORE) {
                if (!onViewStateRestoredCalled) {
                    throw new IllegalStateException("When activity is restoring, onViewReady must be called after onViewStateRestored to guarantee all state of this fragment is ready to use.");
                }
            }
        }

        @Override
        public void onViewStateRestored(Bundle savedInstanceState) {
            onViewStateRestoredCalled = true;
            super.onViewStateRestored(savedInstanceState);
        }
    }

}
