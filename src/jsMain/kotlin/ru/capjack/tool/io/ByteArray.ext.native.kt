package ru.capjack.tool.io

import org.khronos.webgl.Int8Array

@Suppress("NOTHING_TO_INLINE")
inline fun ByteArray.asNative(): Int8Array {
	return this.unsafeCast<Int8Array>()
}
