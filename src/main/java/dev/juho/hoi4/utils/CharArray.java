package dev.juho.hoi4.utils;

public class CharArray {

	private char[] arr;
	private int pos, capacity;

	public CharArray(int capacity) {
		this.arr = new char[capacity];
		this.capacity = capacity;
		this.pos = 0;
	}

	public void append(char c) {
		arr[pos++] = c;

		if (capacity == pos) {
			resize();
		}
	}

	public boolean startsWith(char... chars) {
		for (int i = 0; i < chars.length; i++) {
			if (arr[i] != chars[i]) {
				return false;
			}
		}

		return true;
	}

	public int size() {
		return pos;
	}

	public char[] chars() {
		return arr;
	}

	private void resize() {
		int newCapacity;
		if (capacity < 5) {
//			This would only be the case if the CharArray expects a boolean or an enum
//			and it got an enum and not a boolean so it needs to be resized to a way bigger array because enums tend to be longer than booleans
//			who would have thought
			newCapacity = (int) Math.pow(capacity, 3);
		} else {
			newCapacity = capacity * 2;
		}

		char[] newArr = new char[newCapacity];
		System.arraycopy(arr, 0, newArr, 0, arr.length);
		arr = newArr;
		capacity = newCapacity;
	}

}
