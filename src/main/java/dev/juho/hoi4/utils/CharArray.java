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
		int newCapacity = capacity * 2;
		char[] newArr = new char[newCapacity];
		System.arraycopy(arr, 0, newArr, 0, arr.length);
		arr = newArr;
		capacity = newCapacity;
	}

}
