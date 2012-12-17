package com.yixia.zi.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class ArrayAdapter<T> extends BaseAdapter {

	protected ArrayList<T> mObjects;
	protected LayoutInflater mInflater;

	public ArrayAdapter(final Context ctx, final ArrayList<T> l) {
		mObjects = l == null ? new ArrayList<T>() : l;
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayAdapter(final Context ctx, final T... l) {
		mObjects = new ArrayList<T>();
		mObjects.addAll(Arrays.asList(l));
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayAdapter(final Context ctx, final List<T> l) {
		mObjects = new ArrayList<T>();
		if (l != null)
			mObjects.addAll(l);
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public ArrayAdapter(final Context ctx, final Collection<T> l) {
		mObjects = new ArrayList<T>();
		if (l != null)
			mObjects.addAll(l);
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void add(T item) {
		this.mObjects.add(item);
	}

	public void addAll(T... items) {
		ArrayList<T> values = this.mObjects;
		for (T item : items) {
			values.add(item);
		}
		this.mObjects = values;
	}

	public void addAll(Collection<? extends T> collection) {
		mObjects.addAll(collection);
	}

	public void clear() {
		mObjects.clear();
	}

	public final ArrayList<T> getAll() {
		return mObjects;
	}
}
