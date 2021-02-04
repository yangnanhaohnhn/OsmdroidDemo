package org.osmdroid.tileprovider.modules;

import org.osmdroid.tileprovider.IRegisterReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class MapTileFileStorageProviderBase extends MapTileModuleProviderBase {

	private final IRegisterReceiver mRegisterReceiver;
	private MyBroadcastReceiver mBroadcastReceiver;

	public MapTileFileStorageProviderBase(final IRegisterReceiver pRegisterReceiver,
			final int pThreadPoolSize, final int pPendingQueueSize) {
		super(pThreadPoolSize, pPendingQueueSize);

		mRegisterReceiver = pRegisterReceiver;
		mBroadcastReceiver = new MyBroadcastReceiver();

		final IntentFilter mediaFilter = new IntentFilter();
		mediaFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		mediaFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		mediaFilter.addDataScheme("file");
		pRegisterReceiver.registerReceiver(mBroadcastReceiver, mediaFilter);
	}

	@Override
	public void detach() {
		if (mBroadcastReceiver != null) {
			mRegisterReceiver.unregisterReceiver(mBroadcastReceiver);
			mBroadcastReceiver = null;
		}
		super.detach();
	}

	protected void onMediaMounted() {
		// Do nothing by default. Override to handle.
	}

	protected void onMediaUnmounted() {
		// Do nothing by default. Override to handle.
	}

	/**
	 * This broadcast receiver will recheck the sd card when the mount/unmount messages happen
	 *
	 */
	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(final Context aContext, final Intent aIntent) {

			final String action = aIntent.getAction();

			if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
				onMediaMounted();
			} else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
				onMediaUnmounted();
			}
		}
	}
}
