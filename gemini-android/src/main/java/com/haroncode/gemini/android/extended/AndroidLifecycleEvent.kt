package com.haroncode.gemini.android.extended

enum class AndroidLifecycleEvent {
    /**
     * Constant for onCreate event of the {@link LifecycleOwner}.
     */
    ON_CREATE,

    /**
     * Constant for onStart event of the {@link LifecycleOwner}.
     */
    ON_START,

    /**
     * Constant for onResume event of the {@link LifecycleOwner}.
     */
    ON_RESUME,

    /**
     * Constant for onPause event of the {@link LifecycleOwner}.
     */
    ON_PAUSE,

    /**
     * Constant for onStop event of the {@link LifecycleOwner}.
     */
    ON_STOP,

    /**
     * Constant for onDestroy event of the {@link LifecycleOwner}.
     */
    ON_DESTROY,

    /**
     * Constant for real destroy event (it means destroy not after rotation) of the {@link LifecycleOwner}.
     */
    ON_FINISH
}
