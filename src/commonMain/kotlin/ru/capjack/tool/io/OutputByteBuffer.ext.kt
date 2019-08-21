package ru.capjack.tool.io

fun OutputByteBuffer.ensureWriteableArrayView(size: Int): OutputByteBuffer.ArrayView {
	ensureWrite(size)
	return writeableArrayView
}